package net.mkgiles.postmark.ui.home

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.ListItemPackageBinding
import net.mkgiles.postmark.models.PackageModel

class PackageAdapter(private val list: MutableList<PackageModel>) : RecyclerView.Adapter<PackageAdapter.ViewHolder>() {
    class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        fun bind(parcel: PackageModel){
            val binding : ListItemPackageBinding = DataBindingUtil.getBinding(view)!!
            binding.parcel = parcel
            binding.packageImage.setImageResource(R.drawable.ic_launcher_foreground)
            binding.packageCarrier.text = binding.root.context.resources.getStringArray(R.array.carriers)[parcel.carrier]
            binding.packageStatus.text = if (parcel.delivered) "Delivered" else "Pending Delivery"
            binding.packageUpdated.text = DateFormat.getDateFormat(binding.root.context).format(parcel.updated)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : ListItemPackageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.list_item_package,parent, false)
        return ViewHolder(view.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}