package net.mkgiles.postmark.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import net.mkgiles.postmark.MainActivity
import net.mkgiles.postmark.PackageActivity
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.FragmentHomeBinding
import net.mkgiles.postmark.main.MainApp

class HomeFragment : Fragment() {

    private lateinit var app : MainApp
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: PackageAdapter
    private lateinit var search: SearchView
    private lateinit var checked : MutableList<Int>
    private lateinit var filters : ChipGroup
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false)
        mainActivity = activity as MainActivity
        mainActivity.changeBarBehavior(0)
        mainActivity.setFabAction(View.OnClickListener{startActivityForResult(Intent(activity,PackageActivity::class.java),0)},
            R.drawable.ic_add_black_24dp)
        recycler = binding.homeRecycler
        search = binding.searchBar
        checked = mutableListOf()
        filters = binding.filters
        val carriers = resources.getStringArray(R.array.carriers)
        val spinner = binding.filterSpinner
        spinner.prompt = "Filter"
        spinner.setSelection(-1)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                if (pos != 0) {
                    val chip = Chip(filters.context)
                    chip.text = parent.getItemAtPosition(pos).toString()
                    chip.isCloseIconVisible = true
                    chip.setOnCloseIconClickListener {
                        filters.removeView(chip)
                        checked.remove(carriers.indexOf(chip.text))
                        adapter.filter(checked)
                    }
                    if(checked.find{it == carriers.indexOf(chip.text)} == null) {
                        filters.addView(chip)
                        checked.add(carriers.indexOf(chip.text))
                        adapter.filter(checked)
                    }
                    spinner.setSelection(0)
                }
            }
        }
        app = activity?.application as MainApp
        recycler.adapter = PackageAdapter(app.parcels.findAll().toMutableList(),
            PackageAdapter.ViewHolder.ClickListener { mainActivity.changeFragment(HomeFragmentDirections.actionNavigationHomeToViewFragment2(it)) })
        adapter = recycler.adapter as PackageAdapter
        adapter.notifyDataSetChanged()
        ItemTouchHelper(PackageAdapter.ViewHolder.SwipeHelper({position ->
            showDeletePrompt(position)
        },{position ->
            mainActivity.startActivityForResult(Intent(activity, PackageActivity::class.java).putExtra("parcel", adapter.getItem(position)),0)
            adapter.notifyItemChanged(position)
        })).attachToRecyclerView(recycler)
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.search(newText)
                return true
            }
        })
        checked.clear()
        filters.removeAllViews()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        checked.clear()
        search.setQuery("",false)
        adapter.newList(app.parcels.findAll())
        adapter.notifyFull()
        filters.removeAllViews()
    }

    fun showDeletePrompt(position: Int){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.run{
            val parcel = adapter.getItem(position)
            adapter.removeItem(position)
            setTitle("Delete Package")
            setMessage("Delete this package? This cannot be undone.")
            setPositiveButton(android.R.string.yes){_,_ ->
                app.parcels.delete(parcel)
            }
            setNegativeButton(android.R.string.no){dialog,_ ->
                adapter.restoreItem(parcel, position)
                dialog.cancel()
            }
            val dialog:AlertDialog = builder.create()
            dialog.show()
        }
    }
}