package com.akustom15.choco.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akustom15.choco.R
import com.akustom15.choco.models.ChocoWallpaper
import com.bumptech.glide.Glide

/**
 * Adapter para mostrar wallpapers en un RecyclerView
 * 
 * @param wallpapers Lista de wallpapers a mostrar
 * @param onWallpaperClick Callback cuando se hace click en un wallpaper
 */
class WallpaperAdapter(
    private var wallpapers: List<ChocoWallpaper>,
    private val onWallpaperClick: (ChocoWallpaper) -> Unit
) : RecyclerView.Adapter<WallpaperAdapter.WallpaperViewHolder>() {
    
    /**
     * ViewHolder para wallpapers
     */
    class WallpaperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val previewImage: ImageView = itemView.findViewById(R.id.iv_wallpaper_preview)
        val titleText: TextView = itemView.findViewById(R.id.tv_wallpaper_title)
        val descriptionText: TextView = itemView.findViewById(R.id.tv_wallpaper_description)
        val authorText: TextView = itemView.findViewById(R.id.tv_wallpaper_author)
        val resolutionText: TextView = itemView.findViewById(R.id.tv_wallpaper_resolution)
        val screensText: TextView = itemView.findViewById(R.id.tv_wallpaper_screens)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_wallpaper, parent, false)
        return WallpaperViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallpaper = wallpapers[position]
        
        holder.titleText.text = wallpaper.title
        holder.descriptionText.text = wallpaper.description
        holder.authorText.text = "Por ${wallpaper.author}"
        holder.resolutionText.text = wallpaper.getResolutionString()
        
        // Mostrar número de pantallas si es multi-pantalla
        if (wallpaper.isMultiScreen()) {
            holder.screensText.visibility = View.VISIBLE
            holder.screensText.text = "${wallpaper.screens} pantallas"
        } else {
            holder.screensText.visibility = View.GONE
        }
        
        // Cargar imagen de vista previa
        val previewBitmap = wallpaper.previewPortrait ?: wallpaper.previewLandscape
        if (previewBitmap != null) {
            Glide.with(holder.itemView.context)
                .load(previewBitmap)
                .placeholder(R.drawable.ic_wallpaper_placeholder)
                .error(R.drawable.ic_wallpaper_placeholder)
                .into(holder.previewImage)
        } else {
            holder.previewImage.setImageResource(R.drawable.ic_wallpaper_placeholder)
        }
        
        // Click listener
        holder.itemView.setOnClickListener {
            onWallpaperClick(wallpaper)
        }
    }
    
    override fun getItemCount(): Int = wallpapers.size
    
    /**
     * Actualiza la lista de wallpapers
     * 
     * @param newWallpapers Nueva lista de wallpapers
     */
    fun updateWallpapers(newWallpapers: List<ChocoWallpaper>) {
        wallpapers = newWallpapers
        notifyDataSetChanged()
    }
    
    /**
     * Filtra los wallpapers por texto
     * 
     * @param query Texto de búsqueda
     * @param originalWallpapers Lista original de wallpapers
     */
    fun filter(query: String, originalWallpapers: List<ChocoWallpaper>) {
        val filteredWallpapers = if (query.isEmpty()) {
            originalWallpapers
        } else {
            originalWallpapers.filter { wallpaper ->
                wallpaper.title.contains(query, ignoreCase = true) ||
                wallpaper.description.contains(query, ignoreCase = true) ||
                wallpaper.author.contains(query, ignoreCase = true)
            }
        }
        updateWallpapers(filteredWallpapers)
    }
}
