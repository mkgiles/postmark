package net.mkgiles.postmark.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class PackageModel(var name: String, var id: String, var carrier: Int, var delivered: Boolean, var updated: Date, val uid : Long = UUID.randomUUID().leastSignificantBits) : Parcelable