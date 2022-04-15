package com.devtides.imageprocessingcoroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val IMAGE_URL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO){getOriginalBitmap()}
            val originalBitmap = originalDeferred.await()
            loadImage(originalBitmap)

            val filteredImageDeferred = coroutineScope.async(Dispatchers.IO){
                delay(1000L)// to show the change
                applyFilter(originalBitmap)
            }
            loadImage(filteredImageDeferred.await())

        }
    }

    private fun applyFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)

    private fun loadImage(originalBitmap: Bitmap) {
        progressBar.isVisible = false
        imageView.setImageBitmap(originalBitmap)
        imageView.isVisible = true
    }

    private fun getOriginalBitmap()=
        URL(IMAGE_URL).openStream().use {
            BitmapFactory.decodeStream(it)
        }
}
