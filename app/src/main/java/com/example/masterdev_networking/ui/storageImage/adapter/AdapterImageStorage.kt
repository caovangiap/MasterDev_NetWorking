package com.example.masterdev_networking.ui.storageImage.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.masterdev_networking.R
import com.example.masterdev_networking.model.model_image_storage.ImageStorage
import com.example.masterdev_networking.model.model_image_storage.ParentImageStore

class AdapterImageStorage( var dataItems: MutableList<ImageStorage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val typeLoading = 1
    private val typeImage = 2
    var isLoading = true

    class ViewHolderItems(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val nameImage: TextView

        init {
            image = itemView.findViewById(R.id.Image)
            nameImage = itemView.findViewById(R.id.NameImage)
        }
    }

    class ViewHolderLoading(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val month: TextView

        init {
            month = itemView.findViewById(R.id.Loading)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            typeLoading -> {
                val viewLoading = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items_type_month, parent, false)
                return ViewHolderLoading(viewLoading)
            }
            else -> {
                val viewImage = LayoutInflater.from(parent.context)
                    .inflate(R.layout.items_type_image, parent, false)
                return ViewHolderItems(viewImage)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == typeImage) {
            val holderItems = holder as ViewHolderItems
            holderItems.nameImage.setText(dataItems.get(position).nameImage)
            Glide.with(holder.itemView.context).load(dataItems.get(position).column_index_data)
                .into(holderItems.image)
        }
        else{
            val holderLoading = holder as ViewHolderLoading
            holderLoading.month.text = dataItems.get(position).column_Time_data
        }
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position == dataItems.size-1 && isLoading){
            return typeLoading
        }
        return typeImage
    }

    // add ui loading va add items
    fun addLoading(addDataItems: MutableList<ImageStorage>){
        isLoading = true
        for (i in 0 until addDataItems.size){
            dataItems.add(addDataItems[i])
        }
    }
    // remove ui loading
    fun removeUiLoading(){
        isLoading= false
        val positionLastItems = dataItems.size.minus(1)
            dataItems.removeAt(positionLastItems)

    }
}