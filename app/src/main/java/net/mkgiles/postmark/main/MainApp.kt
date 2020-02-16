package net.mkgiles.postmark.main

import android.app.Application
import net.mkgiles.postmark.models.PackageJSONStore
import net.mkgiles.postmark.models.PackageStore

class MainApp : Application() {
    lateinit var parcels : PackageStore
    override fun onCreate() {
        super.onCreate()
        parcels = PackageJSONStore(applicationContext)
    }
}