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
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import net.mkgiles.postmark.MainActivity
import net.mkgiles.postmark.PackageActivity
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.FragmentHomeBinding
import net.mkgiles.postmark.main.MainApp
import net.mkgiles.postmark.models.PackageModel

class HomeFragment : Fragment() {

    private lateinit var app : MainApp
    private lateinit var parcels : DatabaseReference
    private lateinit var db : DatabaseReference
    private lateinit var filtered : DatabaseReference
    private lateinit var displaying: DatabaseReference
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
                val update: () -> Unit = {
                    filtered.removeValue()
                    checked.forEach{
                        parcels.orderByChild("carrier").equalTo(it.toDouble()).addChildEventListener(object : ChildEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                            }

                            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                            }

                            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                                filtered.child(p0.key!!).setValue(p0.value)
                            }

                            override fun onChildRemoved(p0: DataSnapshot) {
                            }
                        })
                    }
                    if(checked.isEmpty())
                        nofilter()
                    search.setQuery("",false)
                    displayAll()
                }
                if (pos != 0) {
                    val chip = Chip(filters.context)
                    chip.text = parent.getItemAtPosition(pos).toString()
                    chip.isCloseIconVisible = true
                    chip.setOnCloseIconClickListener {
                        filters.removeView(chip)
                        checked.remove(carriers.indexOf(chip.text))
                        update()
                    }
                    if(checked.find{it == carriers.indexOf(chip.text)} == null) {
                        filters.addView(chip)
                        checked.add(carriers.indexOf(chip.text))
                        update()
                    }
                    spinner.setSelection(0)
                }
            }
        }
        app = activity?.application as MainApp
        db = app.database.getReference(FirebaseAuth.getInstance().currentUser!!.uid)
        parcels = db.child("parcels")
        filtered = db.child("filter")
        displaying = db.child("display")
        nofilter()
        displayAll()
        recycler.adapter = PackageAdapter(PackageAdapter.ViewHolder.ClickListener { mainActivity.changeFragment(HomeFragmentDirections.actionNavigationHomeToViewFragment2(it)) }, FirebaseRecyclerOptions.Builder<PackageModel>().setQuery(displaying, PackageModel::class.java).build())
        adapter = recycler.adapter as PackageAdapter
        ItemTouchHelper(PackageAdapter.ViewHolder.SwipeHelper({position ->
            showDeletePrompt(position)
        },{position ->
            mainActivity.startActivityForResult(Intent(activity, PackageActivity::class.java).putExtra("parcel", adapter.getItem(position)),0)
            adapter.notifyItemChanged(position)
        })).attachToRecyclerView(recycler)
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query.isNullOrBlank())
                    displayAll()
                else
                    filtered.orderByChild("name").startAt(query).endAt(query + "\uff8f").addListenerForSingleValueEvent(
                        object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                            }
                            override fun onDataChange(p0: DataSnapshot) {
                                displaying.setValue(p0.value)
                            }
                        }
                    )
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText.isNullOrBlank())
                    displayAll()
                else
                    filtered.orderByChild("name").startAt(newText).endAt(newText + "\uff8f").addListenerForSingleValueEvent(
                        object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                displaying.setValue(p0.value)
                            }
                        }
                    )
                return true
            }
        })
        checked.clear()
        filters.removeAllViews()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
        nofilter()
        displayAll()
    }

    override fun onStop() {
        super.onStop()
        checked.clear()
        search.setQuery("",true)
        filters.removeAllViews()
        adapter.stopListening()
    }

    private fun showDeletePrompt(position: Int){
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.run{
            val parcel = adapter.getItem(position)
            displaying.child(parcel.uid.toString()).removeValue()
            adapter.notifyItemRemoved(position)
            setTitle("Delete Package")
            setMessage("Delete this package? This cannot be undone.")
            setPositiveButton(android.R.string.yes){_,_ ->
                parcels.child(parcel.uid.toString()).removeValue()
            }
            setNegativeButton(android.R.string.no){dialog,_ ->
                displaying.child(parcel.uid.toString()).setValue(parcel)
                adapter.notifyItemInserted(position)
                dialog.cancel()
            }
            val dialog:AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun displayAll(){
        displaying.removeValue()
        filtered.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                displaying.child(p0.key!!).setValue(p0.value)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
    private fun nofilter(){
        filtered.removeValue()
        parcels.addChildEventListener(object: ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                filtered.child(p0.key!!).setValue(p0.value)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}