package com.himanshu.storydrone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class StoryScreenActivty : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_screen_activty)

        val imageView = findViewById<ImageView>(R.id.iv_full_story)

        val uri = intent.getStringExtra("URI")

        GlideLoader(this).loadUserPicture(uri!!, imageView)



    }
}