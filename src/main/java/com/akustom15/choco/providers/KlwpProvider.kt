package com.akustom15.choco.providers

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.akustom15.choco.Choco
import com.akustom15.choco.utils.FileUtils
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileNotFoundException

/**
 * Content Provider para KLWP
 * 
 * Este provider permite que KLWP reconozca la aplicación como un proveedor
 * de wallpapers y pueda acceder a los archivos .klwp.
 */
class KlwpProvider : ContentProvider() {
    
    companion object {
        private const val AUTHORITY_SUFFIX = ".klwp"
        private const val WALLPAPERS = 1
        private const val WALLPAPER_FILE = 2
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("*$AUTHORITY_SUFFIX", "wallpapers", WALLPAPERS)
            addURI("*$AUTHORITY_SUFFIX", "wallpapers/*", WALLPAPER_FILE)
        }
        
        // Columnas que espera KLWP
        private val PROJECTION = arrayOf(
            "_id",
            "title",
            "description",
            "author",
            "email",
            "version",
            "width",
            "height",
            "features",
            "screens",
            "file_name"
        )
    }
    
    private lateinit var choco: Choco
    
    override fun onCreate(): Boolean {
        context?.let { ctx ->
            choco = Choco.getInstance(ctx)
        }
        return true
    }
    
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            WALLPAPERS -> {
                val cursor = MatrixCursor(PROJECTION)
                
                runBlocking {
                    val wallpapers = choco.loadWallpapersSync()
                    wallpapers.forEachIndexed { index, wallpaper ->
                        cursor.addRow(arrayOf(
                            index,
                            wallpaper.title,
                            wallpaper.description,
                            wallpaper.author,
                            wallpaper.email,
                            wallpaper.version,
                            wallpaper.width,
                            wallpaper.height,
                            wallpaper.features,
                            wallpaper.screens,
                            wallpaper.klwpFile.name
                        ))
                    }
                }
                
                cursor
            }
            else -> null
        }
    }
    
    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        return when (uriMatcher.match(uri)) {
            WALLPAPER_FILE -> {
                val fileName = uri.lastPathSegment
                fileName?.let { name ->
                    val tempFile = File(context?.cacheDir, name)
                    
                    // Copiar el archivo desde assets si no existe en caché
                    if (!tempFile.exists()) {
                        context?.let { ctx ->
                            FileUtils.copyAssetToFile(ctx, "wallpapers/$name", tempFile)
                        }
                    }
                    
                    if (tempFile.exists()) {
                        try {
                            return ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        }
                    }
                }
                null
            }
            else -> super.openFile(uri, mode)
        }
    }
    
    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            WALLPAPERS -> "vnd.android.cursor.dir/vnd.kustom.wallpaper"
            WALLPAPER_FILE -> "application/zip"
            else -> null
        }
    }
    
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = 0
}
