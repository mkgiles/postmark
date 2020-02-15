package net.mkgiles.postmark.models

import java.util.Date

data class PackageModel(var name: String, var id: String, var carrier: Int, var delivered: Boolean, var updated: Date)