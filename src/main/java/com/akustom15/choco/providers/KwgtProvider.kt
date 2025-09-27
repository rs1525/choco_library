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
 * Content Provider para KWGT
 * 
 * Este provider permite que KWGT reconozca la aplicación como un proveedor
 * de widgets y pueda acceder a los archivos .kwgt.
 */
class KwgtProvider : ContentProvider() {
    
    companion object {
        private const val AUTHORITY_SUFFIX = ".kwgt"
        private const val WIDGETS = 1
        private const val WIDGET_FILE = 2
        
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("*$AUTHORITY_SUFFIX", "widgets", WIDGETS)
            addURI("*$AUTHORITY_SUFFIX", "widgets/*", WIDGET_FILE)
        }
        
        // Columnas que espera KWGT
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
            WIDGETS -> {
                val cursor = MatrixCursor(PROJECTION)
                
                runBlocking {
                    val widgets = choco.loadWidgetsSync()
                    widgets.forEachIndexed { index, widget ->
                        cursor.addRow(arrayOf(
                            index,
                            widget.title,
                            widget.description,
                            widget.author,
                            widget.email,
                            widget.version,
                            widget.width,
                            widget.height,
                            widget.features,
                            widget.kwgtFile.name
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
            WIDGET_FILE -> {
                val fileName = uri.lastPathSegment
                fileName?.let { name ->
                    val tempFile = File(context?.cacheDir, name)
                    
                    // Copiar el archivo desde assets si no existe en caché
                    if (!tempFile.exists()) {
                        context?.let { ctx ->
                            FileUtils.copyAssetToFile(ctx, "widgets/$name", tempFile)
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
            WIDGETS -> "vnd.android.cursor.dir/vnd.kustom.widget"
            WIDGET_FILE -> "application/zip"
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
