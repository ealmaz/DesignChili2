package com.design2.chili2.extensions

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.design2.chili2.util.GlideBitmapScaleTransformation
import java.io.File

fun ImageView.loadImageFormFilePath(filePath: String) {
    if (tag == filePath) return
    Glide.with(context)
        .load(File(filePath))
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .transform(GlideBitmapScaleTransformation())
        .override(Target.SIZE_ORIGINAL)
        .into(this)
}

fun ImageView.loadImage(imageUrl: String) {
    Glide.with(this.context)
        .load(imageUrl)
        .into(this)
}