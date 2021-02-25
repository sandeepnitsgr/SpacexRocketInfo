package com.spacex.rocket.spacexrocketinfo.presentation.ui.rocketlist

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
import com.spacex.rocket.spacexrocketinfo.utils.Constants.ERROR_WHILE_LOADING_TEXT
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initInjector()
        initViewModel()
        initUI()
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
                    swipeRefreshLayout.visibility = View.GONE
                    errorMessageTextView.text = ERROR_WHILE_LOADING_TEXT
                    errorMessageTextView.visibility = View.VISIBLE
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

    }

    private fun updateRecyclerViewData(response: Response) {
        response.data?.let {
            (recyclerView.adapter as RocketListAdapter).updateList(it)
        }
        recyclerView.scheduleLayoutAnimation()
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.GONE

    }
}

