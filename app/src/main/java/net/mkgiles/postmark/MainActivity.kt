package net.mkgiles.postmark

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import net.mkgiles.postmark.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var navBar: BottomAppBar
    private lateinit var fab : FloatingActionButton
    private lateinit var navHost : FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navBar = binding.navBar
        navHost = binding.navHostFragment
        navBar.setNavigationOnClickListener {
            NavListDialogFragment.newInstance(3,navHost.findNavController()).show(supportFragmentManager, "dialog")
        }
        fab = binding.navBtn
        fab.setOnClickListener {
            val intent = Intent(this, PackageActivity::class.java)
            startActivityForResult(intent, 0)
        }
    }

    fun changeBarBehavior(behavior: Int){
        when (behavior) {
            0 -> {
                if(!fab.isShown)
                    fab.show()
                navBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
            }
            1 -> {
                if(!fab.isShown)
                    fab.show()
                navBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
            }
            2 -> {
                fab.hide()
            }
        }
    }

    fun setFabAction(action : View.OnClickListener){
        fab.setOnClickListener(action)
    }
    fun setFabAction(action : View.OnClickListener, icon : Int){
        setFabAction(action)
        fab.setImageResource(icon)
    }
    fun changeFragment(direction: NavDirections){
        navHost.findNavController().navigate(direction)
    }
    fun getNavComponent() : FragmentContainerView{
        return navHost
    }
}
