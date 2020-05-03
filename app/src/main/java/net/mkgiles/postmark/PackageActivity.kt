package net.mkgiles.postmark

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import net.mkgiles.postmark.databinding.ActivityPackageBinding
import net.mkgiles.postmark.main.MainApp
import net.mkgiles.postmark.models.PackageModel

class PackageActivity : AppCompatActivity() {
    private lateinit var app : MainApp
    private lateinit var user : FirebaseUser
    private lateinit var dbref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as MainApp
        user = FirebaseAuth.getInstance().currentUser!!
        dbref = FirebaseDatabase.getInstance().getReference(user.uid).child("parcels")
        var edit = false
        val binding: ActivityPackageBinding = DataBindingUtil.setContentView(this, R.layout.activity_package)
        binding.parcel = PackageModel()
        intent.extras?.let{
            binding.parcel = it.get("parcel") as PackageModel
            edit = true
        }
        if(edit){
            binding.packageTitle.text = getString(R.string.editPackage)
        }
        binding.packageProviderSpinner.setSelection(binding.parcel!!.carrier)
        binding.packageSaveBtn.setOnClickListener {
            val parcel : PackageModel = binding.parcel!!
            if(binding.packageId.text.isNullOrBlank()){
                Toast.makeText(this,"Please enter a valid Package ID to save", Toast.LENGTH_LONG).show()
            }
            else {
                parcel.carrier = binding.packageProviderSpinner.selectedItemPosition
                dbref.child(parcel.uid.toString()).setValue(parcel)
                setResult(0, Intent().putExtra("parcel", parcel))
                finish()
            }
        }
    }
}
