package net.mkgiles.postmark.main

import android.app.Application
import net.mkgiles.postmark.models.PackageModel

class MainApp : Application() {
    val packages : MutableList<PackageModel> = mutableListOf()
}