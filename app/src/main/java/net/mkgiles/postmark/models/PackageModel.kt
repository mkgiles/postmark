package net.mkgiles.postmark.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PackageModel(var name: String = "", var id: String = "", var carrier: Int = 0, var delivered: Boolean = false, var updated: Date = Date(), val uid : Long = UUID.randomUUID().leastSignificantBits) : Parcelable