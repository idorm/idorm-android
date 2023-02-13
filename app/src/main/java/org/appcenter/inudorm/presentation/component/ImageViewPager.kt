package org.appcenter.inudorm.presentation.component

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import org.appcenter.inudorm.R
import org.appcenter.inudorm.presentation.adapter.ImageViewPagerAdapter

class ImageViewPager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_image_viewer)

        val images = intent.getStringArrayListExtra("images")
        val position = intent.getIntExtra("initialPosition", 0)
        if ((images?.size ?: 0) > 0) {
            val pager = findViewById<ViewPager2>(R.id.imagePager)
            pager.adapter = ImageViewPagerAdapter(images!!)
            pager.setCurrentItem(position, false)
        } else assert(false) { "이미지가 없는데,, 어떻게 여기 들어오신 거에요..?" }

    }
}