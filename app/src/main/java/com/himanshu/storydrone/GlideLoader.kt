package com.himanshu.storydrone

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.IOException

class GlideLoader(val context : Context) {
    fun loadUserPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(Uri.parse(image.toString())) // URI of the image
//                .centerCrop() // Scale type of the image.
                .placeholder(R.drawable.ic_launcher_background) // A default place holder if image is failed to load.
                .into(imageView) // the view in which the image will be loaded.
//            Log.i("jod","failed")
            Log.i("jod","passed")

        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("jod","failed")
        }
    }
}