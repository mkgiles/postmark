package net.mkgiles.postmark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val auth = FirebaseAuth.getInstance()
            val authui = AuthUI.getInstance()
            if(auth.currentUser != null){
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                startActivityForResult(authui.createSignInIntentBuilder()
                    .setTheme(R.style.AppTheme)
                    .setLogo(R.drawable.cover)
                    .setIsSmartLockEnabled(false).setAvailableProviders(listOf(
                    AuthUI.IdpConfig.GoogleBuilder().build(),
                    AuthUI.IdpConfig.EmailBuilder().build(),
                    AuthUI.IdpConfig.AnonymousBuilder().build()
                )).build(),0)
            }
        },2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this,"Login failed!",Toast.LENGTH_SHORT).show()
            finish()
            return
        }
    }
}
