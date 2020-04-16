package net.mkgiles.postmark.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.mkgiles.postmark.MainActivity
import net.mkgiles.postmark.PackageActivity
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.FragmentViewBinding
import net.mkgiles.postmark.models.PackageModel

/**
 * A simple [Fragment] subclass.
 * Use the [ViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var parcel : PackageModel? = null
    private lateinit var mainActivity: MainActivity
    private lateinit var binding : FragmentViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parcel = it.getParcelable("parcel")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewBinding.inflate(inflater,container,false)
        mainActivity = activity as MainActivity
        mainActivity.changeBarBehavior(1)
        mainActivity.setFabAction(View.OnClickListener {startActivityForResult(Intent(activity, PackageActivity::class.java).putExtra("parcel", parcel),0)},R.drawable.ic_edit_black_24dp)
        updateParcel(parcel)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(parcel: PackageModel) =
            ViewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("parcel", parcel)
                }
            }
    }

    private fun updateParcel(parcel: PackageModel?){
        parcel?.let {
            binding.viewName.text = it.name
            binding.viewId.text = it.id
            binding.viewProvider.text = resources.getStringArray(R.array.carriers)[it.carrier]
            binding.viewDelivered.text = if(it.delivered)resources.getString(R.string.delivered) else resources.getString(R.string.not_delivered)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.extras?.let{
            updateParcel(it.getParcelable<PackageModel>("parcel"))
        }
    }
}
