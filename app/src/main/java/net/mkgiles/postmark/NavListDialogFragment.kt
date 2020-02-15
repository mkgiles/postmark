package net.mkgiles.postmark

import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_navigation_list_dialog.*
class NavListDialogFragment(private val navController: NavController) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_navigation_list_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        list.setNavigationItemSelectedListener {menuItem ->
            navController.navigate(menuItem.itemId)
            dismiss()
            true

        }
    }

    companion object {
        fun newInstance(itemCount: Int, navController: NavController): NavListDialogFragment =
            NavListDialogFragment(navController).apply {
                arguments = Bundle().apply {
                    putInt("item_count", itemCount)
                }
            }
    }
}
