package net.mkgiles.postmark.ui.home

import android.graphics.Canvas
import android.graphics.Paint
import android.text.format.DateFormat
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import net.mkgiles.postmark.R
import net.mkgiles.postmark.databinding.ListItemPackageBinding
import net.mkgiles.postmark.models.PackageModel
import kotlin.math.abs
import kotlin.math.round

class PackageAdapter(private var list: MutableList<PackageModel>, private var listener: View.OnClickListener) : RecyclerView.Adapter<PackageAdapter.ViewHolder>(){
    private var fullList : List<PackageModel>  = list
    class ViewHolder(private val view : View) : RecyclerView.ViewHolder(view){
        private lateinit var binding : ListItemPackageBinding
        fun bind(parcel: PackageModel, listener: View.OnClickListener){
            binding = DataBindingUtil.getBinding(view)!!
            binding.parcel = parcel
            binding.root.setOnClickListener(listener)
            binding.packageImage.setImageResource(R.drawable.ic_launcher_foreground)
            binding.packageCarrier.text = binding.root.context.resources.getStringArray(R.array.carriers)[parcel.carrier]
            binding.packageStatus.text = if (parcel.delivered) "Delivered" else "Pending Delivery"
            binding.packageUpdated.text = DateFormat.getDateFormat(binding.root.context).format(parcel.updated)
        }

        class ClickListener(val listener: (PackageModel)->Unit) : View.OnClickListener{
            override fun onClick(view: View?) {
                val binding : ListItemPackageBinding = DataBindingUtil.getBinding(view!!)!!
                listener(binding.parcel!!)
            }
        }

        class SwipeHelper(val leftlistener: (Int)->Unit, val rightlistener: (Int)-> Unit) : ItemTouchHelper.SimpleCallback(0,(ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)){
            var direction: Int = 0

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                this.direction = direction
                when (direction) {
                    ItemTouchHelper.LEFT -> leftlistener(viewHolder.adapterPosition)
                    ItemTouchHelper.RIGHT -> rightlistener(viewHolder.adapterPosition)
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val item = viewHolder.itemView
                val p = Paint()
                val dp = item.resources.displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT
                if(dX > 0){
                    val icon = item.resources.getDrawable(R.drawable.ic_edit_black_24dp,item.resources.newTheme()).toBitmap()
                    p.color = item.resources.getColor(android.R.color.holo_green_dark,item.resources.newTheme())
                    c.drawRect(item.left.toFloat(),item.top.toFloat(),dX,item.bottom.toFloat(),p)
                    c.drawBitmap(icon,item.left.toFloat() + round(16 * dp), item.top.toFloat() + (item.bottom - item.top - icon.height)/2,p)
                }
                else{
                    val icon = item.resources.getDrawable(R.drawable.ic_delete_black_24dp,item.resources.newTheme()).toBitmap()
                    p.color = item.resources.getColor(R.color.design_default_color_error,item.resources.newTheme())
                    c.drawRect(item.right.toFloat() + dX,item.top.toFloat(),item.right.toFloat(),item.bottom.toFloat(),p)
                    c.drawBitmap(icon,item.right.toFloat() - icon.width - round(16 * dp), item.top.toFloat() + (item.bottom - item.top - icon.height)/2,p)
                }
                val alpha = 1.0f - abs(dX) / item.width
                item.alpha = alpha
                item.translationX = dX
            }
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
        holder.bind(list[position],listener)
    }

    fun getItem(position: Int): PackageModel{
        return(list[position])
    }

    fun removeItem(position: Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(parcel: PackageModel,position: Int){
        list.add(position,parcel)
        notifyItemInserted(position)
    }

    fun search(query: String?){
        list = if(query.isNullOrEmpty()){
            fullList.toMutableList()
        } else {
            fullList.filter{it.name.contains(query)}.toMutableList()
        }
        notifyDataSetChanged()
    }
    fun notifyFull(){
        list = fullList.toMutableList()
        notifyDataSetChanged()
    }
    fun filter(filter: List<Int>){
        list = fullList.toMutableList()
        if(filter.isNotEmpty())
            list = list.filter{
                filter.contains(it.carrier)
            }.toMutableList()
        notifyDataSetChanged()
    }
    fun newList(newlist: List<PackageModel>){
        list = newlist.toMutableList()
        fullList = list
        notifyDataSetChanged()
    }
}