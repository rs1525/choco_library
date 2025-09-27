package com.akustom15.choco.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akustom15.choco.R
import com.akustom15.choco.models.ChocoWidget
import com.bumptech.glide.Glide

/**
 * Adapter para mostrar widgets en un RecyclerView
 * 
 * @param widgets Lista de widgets a mostrar
 * @param onWidgetClick Callback cuando se hace click en un widget
 */
class WidgetAdapter(
    private var widgets: List<ChocoWidget>,
    private val onWidgetClick: (ChocoWidget) -> Unit
) : RecyclerView.Adapter<WidgetAdapter.WidgetViewHolder>() {
    
    /**
     * ViewHolder para widgets
     */
    class WidgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val previewImage: ImageView = itemView.findViewById(R.id.iv_widget_preview)
        val titleText: TextView = itemView.findViewById(R.id.tv_widget_title)
        val descriptionText: TextView = itemView.findViewById(R.id.tv_widget_description)
        val authorText: TextView = itemView.findViewById(R.id.tv_widget_author)
        val sizeText: TextView = itemView.findViewById(R.id.tv_widget_size)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WidgetViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_widget, parent, false)
        return WidgetViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WidgetViewHolder, position: Int) {
        val widget = widgets[position]
        
        holder.titleText.text = widget.title
        holder.descriptionText.text = widget.description
        holder.authorText.text = "Por ${widget.author}"
        holder.sizeText.text = widget.getSizeString()
        
        // Cargar imagen de vista previa
        val previewBitmap = widget.previewPortrait ?: widget.previewLandscape
        if (previewBitmap != null) {
            Glide.with(holder.itemView.context)
                .load(previewBitmap)
                .placeholder(R.drawable.ic_widget_placeholder)
                .error(R.drawable.ic_widget_placeholder)
                .into(holder.previewImage)
        } else {
            holder.previewImage.setImageResource(R.drawable.ic_widget_placeholder)
        }
        
        // Click listener
        holder.itemView.setOnClickListener {
            onWidgetClick(widget)
        }
    }
    
    override fun getItemCount(): Int = widgets.size
    
    /**
     * Actualiza la lista de widgets
     * 
     * @param newWidgets Nueva lista de widgets
     */
    fun updateWidgets(newWidgets: List<ChocoWidget>) {
        widgets = newWidgets
        notifyDataSetChanged()
    }
    
    /**
     * Filtra los widgets por texto
     * 
     * @param query Texto de búsqueda
     * @param originalWidgets Lista original de widgets
     */
    fun filter(query: String, originalWidgets: List<ChocoWidget>) {
        val filteredWidgets = if (query.isEmpty()) {
            originalWidgets
        } else {
            originalWidgets.filter { widget ->
                widget.title.contains(query, ignoreCase = true) ||
                widget.description.contains(query, ignoreCase = true) ||
                widget.author.contains(query, ignoreCase = true)
            }
        }
        updateWidgets(filteredWidgets)
    }
}
