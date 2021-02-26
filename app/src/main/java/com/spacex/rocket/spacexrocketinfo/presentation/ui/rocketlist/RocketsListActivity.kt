package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.spacex.rocket.spacexrocketinfo.R
import com.spacex.rocket.spacexrocketinfo.SpaceXApp
import com.spacex.rocket.spacexrocketinfo.data.model.Response
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.data.model.Status
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.di.DaggerRocketListActivityComponent
import com.spacex.rocket.spacexrocketinfo.presentation.ui.BaseActivity
import com.spacex.rocket.spacexrocketinfo.presentation.ui.SpaceItemDecoration
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketListViewModelFactory
import com.spacex.rocket.spacexrocketinfo.presentation.viewmodel.RocketsListViewModel
import com.spacex.rocket.spacexrocketinfo.utils.Constants.PREF_KEY
import com.spacex.rocket.spacexrocketinfo.utils.Constants.PREF_NAME
import javax.inject.Inject


class RocketsListActivity : BaseActivity() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var rocketListViewModelFactory: RocketListViewModelFactory

    private lateinit var viewModel: RocketsListViewModel

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorMessageTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val filterItems = arrayOf("All", "Active", "Inactive")
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInjector()
        initUI()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            showOptionsDialog()
        }
        return true
    }

    private fun showOptionsDialog() {
        var selectedOption = filterItems[0]
        AlertDialog.Builder(this).setTitle("Filter options")
            .setSingleChoiceItems(filterItems, 0) { _, which ->
                selectedOption = filterItems[which]
            }
            .setPositiveButton("Apply") { dialog, _ ->
                run {
                    val items = viewModel.getRocketsList(selectedOption)
                    items?.let {
                        (recyclerView.adapter as RocketListAdapter).updateList(it)
                    }
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun initInjector() {
        val applicationComponent = SpaceXApp.get(this).getApplicationComponent()
        DaggerRocketListActivityComponent.builder()
            .spaceXAppComponent(applicationComponent)
            .build().injectListActivity(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            rocketListViewModelFactory
        ).get(RocketsListViewModel::class.java)
        viewModel.rocketListData.observe(this, { response: Response ->
            when (response.status) {
                Status.LOADING -> swipeRefreshLayout.isRefreshing = true
                Status.ERROR -> run {
                    recyclerView.visibility = View.GONE
                    errorMessageTextView.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
                else -> updateRecyclerViewData(response)
            }
        })
        viewModel.getAllRocketsListFromRepo()
    }

    private fun initUI() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        errorMessageTextView = findViewById(R.id.error_message_view)
        swipeRefreshLayout.isRefreshing = true
        recyclerView = findViewById(R.id.recyclerView)
        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = llm
        recyclerView.adapter = RocketListAdapter(RocketListData())
        recyclerView.addItemDecoration(SpaceItemDecoration(LinearLayoutManager.VERTICAL))
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllRocketsListFromRepo()
        }

        checkAndShowWelcomeDialogForFirstTimeUser()

    }

    private fun checkAndShowWelcomeDialogForFirstTimeUser() {

        if (isFirstTimeUser()) {
            val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert)
            } else {
                AlertDialog.Builder(this)
            }
            builder.setMessage(R.string.first_launch_message)
                .setCancelable(false)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()

                }
            val alert = builder.create()
            alert.show()

        }
    }

    private fun isFirstTimeUser(): Boolean {
        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val firstTimeOpened = prefs.getBoolean(PREF_KEY, false)

        if (!firstTimeOpened) {
            saveFirstTimeLaunch()
            return true
        }
        return false
    }

    private fun saveFirstTimeLaunch() {
        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putBoolean(PREF_KEY, true)
        editor.apply()
    }

    private fun updateRecyclerViewData(response: Response) {
        response.data?.let {
            (recyclerView.adapter as RocketListAdapter).updateList(it)
        }
        recyclerView.scheduleLayoutAnimation()
        recyclerView.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false

    }
}

