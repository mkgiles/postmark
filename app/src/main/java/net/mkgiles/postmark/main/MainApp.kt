package net.mkgiles.postmark.main

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import net.mkgiles.postmark.models.PackageJSONStore
import net.mkgiles.postmark.models.PackageStore

class MainApp : Application() {
    lateinit var parcels : PackageStore
    lateinit var database : FirebaseDatabase
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        database = FirebaseDatabase.getInstance()
        parcels = PackageJSONStore(applicationContext)
    }
}