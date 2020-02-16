package net.mkgiles.postmark.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import net.mkgiles.postmark.PackageActivity
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.FragmentHomeBinding
import net.mkgiles.postmark.main.MainApp
import net.mkgiles.postmark.models.PackageModel

class HomeFragment : Fragment() {

    private lateinit var app : MainApp
    private lateinit var recycler: RecyclerView
    private val viewModel : HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        recycler = binding.homeRecycler
        app = activity?.application as MainApp
        viewModel.list.value = app.packages
        recycler.adapter = PackageAdapter(viewModel.list.value!!, object: PackageAdapter.OnItemClickListener {
            override fun onItemClick(parcel: PackageModel) {
                startActivityForResult(Intent(activity,PackageActivity::class.java).putExtra("parcel", parcel),0)
            }
        })
        recycler.adapter?.notifyDataSetChanged()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        recycler.adapter!!.notifyDataSetChanged()
    }
}