package com.spacex.rocket.spacexrocketinfo.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RocketListData : ArrayList<RocketInfo>()

data class RocketInfo(
    @SerializedName("height")
    @Expose
    val height: Dimens?,
    @SerializedName("diameter")
    val diameter: Dimens?,
    @SerializedName("mass")
    val mass: Mass?,
    @SerializedName("first_stage")
    val firstStage: FirstStage?,
    @SerializedName("second_stage")
    val secondStage: SecondStage?,
    @SerializedName("engines")
    val engines: Engines?,
    @SerializedName("landing_legs")
    val landingLegs: LandingLegs?,
    @SerializedName("payload_weights")
    val payloadWeights: List<PayloadWeight>?,
    @SerializedName("flickr_images")
    val flickrImages: List<String>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("active")
    val active: Boolean?,
    @SerializedName("stages")
    val stages: Int?,
    @SerializedName("boosters")
    val boosters: Int?,
    @SerializedName("cost_per_launch")
    val costPerLaunch: Int?,
    @SerializedName("success_rate_pct")
    val successRatePct: Int?,
    @SerializedName("first_flight")
    val firstFlight: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("wikipedia")
    val wikipedia: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Dimens::class.java.classLoader),
        parcel.readParcelable(Dimens::class.java.classLoader),
        parcel.readParcelable(Mass::class.java.classLoader),
        parcel.readParcelable(FirstStage::class.java.classLoader),
        parcel.readParcelable(SecondStage::class.java.classLoader),
        parcel.readParcelable(Engines::class.java.classLoader),
        parcel.readParcelable(LandingLegs::class.java.classLoader),
        parcel.createTypedArrayList(PayloadWeight),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(height, flags)
        parcel.writeParcelable(diameter, flags)
        parcel.writeParcelable(mass, flags)
        parcel.writeParcelable(firstStage, flags)
        parcel.writeParcelable(secondStage, flags)
        parcel.writeParcelable(engines, flags)
        parcel.writeParcelable(landingLegs, flags)
        parcel.writeTypedList(payloadWeights)
        parcel.writeStringList(flickrImages)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeValue(active)
        parcel.writeValue(stages)
        parcel.writeValue(boosters)
        parcel.writeValue(costPerLaunch)
        parcel.writeValue(successRatePct)
        parcel.writeString(firstFlight)
        parcel.writeString(country)
        parcel.writeString(company)
        parcel.writeString(wikipedia)
        parcel.writeString(description)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RocketInfo> {
        override fun createFromParcel(parcel: Parcel): RocketInfo {
            return RocketInfo(parcel)
        }

        override fun newArray(size: Int): Array<RocketInfo?> {
            return arrayOfNulls(size)
        }
    }
}

data class Dimens(
    @SerializedName("meters")
    val meters: Float?,
    @SerializedName("feet")
    val feet: Float?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(meters)
        parcel.writeValue(feet)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dimens> {
        override fun createFromParcel(parcel: Parcel): Dimens {
            return Dimens(parcel)
        }

        override fun newArray(size: Int): Array<Dimens?> {
            return arrayOfNulls(size)
        }
    }
}

data class Mass(
    @SerializedName("kg")
    val kg: Int?,
    @SerializedName("lb")
    val lb: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(kg)
        parcel.writeValue(lb)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mass> {
        override fun createFromParcel(parcel: Parcel): Mass {
            return Mass(parcel)
        }

        override fun newArray(size: Int): Array<Mass?> {
            return arrayOfNulls(size)
        }
    }
}

data class FirstStage(
    @SerializedName("thrust_sea_level")
    val thrustSeaLevel: ForceUnit?,
    @SerializedName("thrust_vacuum")
    val thrustVacuum: ForceUnit?,
    @SerializedName("reusable")
    val reusable: Boolean?,
    @SerializedName("engines")
    val engines: Int?,
    @SerializedName("fuel_amount_tons")
    val fuelAmountTons: Double?,
    @SerializedName("burn_time_sec")
    val burnTimeSec: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ForceUnit::class.java.classLoader),
        parcel.readParcelable(ForceUnit::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(thrustSeaLevel, flags)
        parcel.writeParcelable(thrustVacuum, flags)
        parcel.writeValue(reusable)
        parcel.writeValue(engines)
        parcel.writeValue(fuelAmountTons)
        parcel.writeValue(burnTimeSec)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FirstStage> {
        override fun createFromParcel(parcel: Parcel): FirstStage {
            return FirstStage(parcel)
        }

        override fun newArray(size: Int): Array<FirstStage?> {
            return arrayOfNulls(size)
        }
    }
}

data class ForceUnit(
    @SerializedName("kN")
    val kN: Int?,
    @SerializedName("lbf")
    val lbf: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(kN)
        parcel.writeInt(lbf)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ForceUnit> {
        override fun createFromParcel(parcel: Parcel): ForceUnit {
            return ForceUnit(parcel)
        }

        override fun newArray(size: Int): Array<ForceUnit?> {
            return arrayOfNulls(size)
        }
    }
}

data class SecondStage(
    @SerializedName("thrust")
    val thrust: ForceUnit?,
    @SerializedName("payloads")
    val payloads: Payloads?,
    @SerializedName("reusable")
    val reusable: Boolean?,
    @SerializedName("engines")
    val engines: Int?,
    @SerializedName("fuel_amount_tons")
    val fuelAmountTons: Double?,
    @SerializedName("burn_time_sec")
    val burnTimeSec: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(ForceUnit::class.java.classLoader),
        parcel.readParcelable(Payloads::class.java.classLoader),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(thrust, flags)
        parcel.writeParcelable(payloads, flags)
        parcel.writeValue(reusable)
        parcel.writeValue(engines)
        parcel.writeValue(fuelAmountTons)
        parcel.writeValue(burnTimeSec)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SecondStage> {
        override fun createFromParcel(parcel: Parcel): SecondStage {
            return SecondStage(parcel)
        }

        override fun newArray(size: Int): Array<SecondStage?> {
            return arrayOfNulls(size)
        }
    }
}

data class Payloads(
    @SerializedName("composite_fairing")
    val compositeFairing: CompositeFairing?,
    @SerializedName("option_1")
    val option1: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(CompositeFairing::class.java.classLoader),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(compositeFairing, flags)
        parcel.writeString(option1)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Payloads> {
        override fun createFromParcel(parcel: Parcel): Payloads {
            return Payloads(parcel)
        }

        override fun newArray(size: Int): Array<Payloads?> {
            return arrayOfNulls(size)
        }
    }
}

data class CompositeFairing(
    @SerializedName("height")
    val height: Dimens?,
    @SerializedName("diameter")
    val diameter: Dimens?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Dimens::class.java.classLoader),
        parcel.readParcelable(Dimens::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(height, flags)
        parcel.writeParcelable(diameter, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CompositeFairing> {
        override fun createFromParcel(parcel: Parcel): CompositeFairing {
            return CompositeFairing(parcel)
        }

        override fun newArray(size: Int): Array<CompositeFairing?> {
            return arrayOfNulls(size)
        }
    }
}

data class Engines(
    @SerializedName("isp")
    val isp: Isp?,
    @SerializedName("thrust_sea_level")
    val thrustSeaLevel: ForceUnit?,
    @SerializedName("thrust_vacuum")
    val thrustVacuum: ForceUnit?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("version")
    val version: String?,
    @SerializedName("layout")
    val layout: String?,
    @SerializedName("engine_loss_max")
    val engineLossMax: Int?,
    @SerializedName("propellant_1")
    val propellant1: String?,
    @SerializedName("propellant_2")
    val propellant2: String?,
    @SerializedName("thrust_to_weight")
    val thrustToWeight: Float?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Isp::class.java.classLoader),
        parcel.readParcelable(ForceUnit::class.java.classLoader),
        parcel.readParcelable(ForceUnit::class.java.classLoader),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(isp, flags)
        parcel.writeParcelable(thrustSeaLevel, flags)
        parcel.writeParcelable(thrustVacuum, flags)
        parcel.writeValue(number)
        parcel.writeString(type)
        parcel.writeString(version)
        parcel.writeString(layout)
        parcel.writeValue(engineLossMax)
        parcel.writeString(propellant1)
        parcel.writeString(propellant2)
        parcel.writeValue(thrustToWeight)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Engines> {
        override fun createFromParcel(parcel: Parcel): Engines {
            return Engines(parcel)
        }

        override fun newArray(size: Int): Array<Engines?> {
            return arrayOfNulls(size)
        }
    }
}

data class Isp(
    @SerializedName("sea_level")
    val seaLevel: Int?,
    @SerializedName("vacuum")
    val vacuum: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(seaLevel)
        parcel.writeValue(vacuum)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Isp> {
        override fun createFromParcel(parcel: Parcel): Isp {
            return Isp(parcel)
        }

        override fun newArray(size: Int): Array<Isp?> {
            return arrayOfNulls(size)
        }
    }
}

data class LandingLegs(
    @SerializedName("number")
    val number: Int??,
    @SerializedName("material")
    val material: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(number)
        parcel.writeString(material)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LandingLegs> {
        override fun createFromParcel(parcel: Parcel): LandingLegs {
            return LandingLegs(parcel)
        }

        override fun newArray(size: Int): Array<LandingLegs?> {
            return arrayOfNulls(size)
        }
    }
}

data class PayloadWeight(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("kg")
    val kg: Int?,
    @SerializedName("lb")
    val lb: Int?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeValue(kg)
        parcel.writeValue(lb)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PayloadWeight> {
        override fun createFromParcel(parcel: Parcel): PayloadWeight {
            return PayloadWeight(parcel)
        }

        override fun newArray(size: Int): Array<PayloadWeight?> {
            return arrayOfNulls(size)
        }
    }
}
