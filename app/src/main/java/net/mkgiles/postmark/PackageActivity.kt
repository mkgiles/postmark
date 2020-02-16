package net.mkgiles.postmark

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import net.mkgiles.postmark.databinding.ActivityPackageBinding
import net.mkgiles.postmark.main.MainApp
import net.mkgiles.postmark.models.PackageModel
import java.util.*

class PackageActivity : AppCompatActivity() {
    private lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        var edit = false
        val binding: ActivityPackageBinding = DataBindingUtil.setContentView(this, R.layout.activity_package)
        binding.parcel = PackageModel("","",0,false, Date())
        intent.extras?.let{
            binding.parcel = it.get("parcel") as PackageModel
            edit = true
        }
        binding.packageSaveBtn.setOnClickListener {
            val parcel : PackageModel = binding.parcel!!
            if(binding.packageId.text.isNullOrBlank()){
                Toast.makeText(this,"Please enter a valid Package ID to save", Toast.LENGTH_LONG).show()
            }
            else {
                parcel.carrier = binding.packageProviderSpinner.selectedItemPosition
                if(edit)
                    app.packages.find{found ->  found.uid == parcel.uid}?.let{found ->
                        found.carrier = parcel.carrier
                        found.delivered = parcel.delivered
                        found.id = parcel.id
                        found.name = parcel.name
                        found.updated = parcel.updated
                    }
                else
                    app.packages.add(parcel)
                finish()
            }
        }
    }
}
