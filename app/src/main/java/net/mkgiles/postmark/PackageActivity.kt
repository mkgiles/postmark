package net.mkgiles.postmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import net.mkgiles.postmark.databinding.ActivityPackageBinding
import net.mkgiles.postmark.main.MainApp
import net.mkgiles.postmark.models.PackageModel
import java.util.Date

class PackageActivity : AppCompatActivity() {
    private lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        val binding: ActivityPackageBinding = DataBindingUtil.setContentView(this, R.layout.activity_package)
        binding.parcel = PackageModel("","",0,false, Date())
        binding.packageSaveBtn.setOnClickListener {
            if(binding.packageId.text.isNullOrBlank()){
                Toast.makeText(this,"Please enter a valid Package ID to save", Toast.LENGTH_LONG).show()
            }
            else {
                binding.parcel?.carrier = binding.packageProviderSpinner.selectedItemPosition
                app.packages.add(binding.parcel!!)
                finish()
            }
        }
    }
}
