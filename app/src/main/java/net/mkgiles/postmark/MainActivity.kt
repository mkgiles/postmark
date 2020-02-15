package net.mkgiles.postmark

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.mkgiles.postmark.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navBar: BottomAppBar = binding.navBar
        val navHost: FragmentContainerView = binding.navHostFragment
        navBar.setNavigationOnClickListener {
            NavListDialogFragment.newInstance(3,navHost.findNavController()).show(supportFragmentManager, "dialog")
        }
        navBar.replaceMenu(R.menu.main_app_menu)
        val fab : FloatingActionButton = binding.navBtn
        fab.setOnClickListener{
            val intent = Intent(this,PackageActivity::class.java)
            startActivityForResult(intent,0)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.app_bar_search -> {
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }
}
