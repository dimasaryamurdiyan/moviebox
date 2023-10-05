package com.singaludra.movieflix.utils

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import com.singaludra.movieflix.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun ImageView.loadImage(url: String) {
    Glide
        .with(this)
        .load(url)
        .into(this)
}

fun Context.shortToast(message : CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Suppress("DEPRECATION")
fun Context?.showLoadingDialog(): ProgressDialog? {
    this?.let {
        val progressDialog = ProgressDialog(this)
        progressDialog.let {
            it.show()
            it.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
            it.setContentView(R.layout.view_progress_dialog)
            it.isIndeterminate = true
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }
    return null
}

fun SearchView.getQueryTextChangeStateFlow(): StateFlow<String> {

    val query = MutableStateFlow("")

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            query.value = newText
            return true
        }
    })

    return query

}