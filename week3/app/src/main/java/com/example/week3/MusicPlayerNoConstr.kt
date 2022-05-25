package com.example.week3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.week3.BlurBuilder.blur


class MusicPlayerNoConstr : AppCompatActivity() {
    lateinit var mImageView: ImageView // кнопка д
    lateinit var mLayout: ConstraintLayout
    lateinit var internalBitmap : Bitmap
    lateinit var internalCanvas : Canvas
    lateinit var mContext: Context
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player_no_constr)
        mLayout = findViewById(R.id.cl_music)
        mContext = this
        val imgs = listOf(R.drawable.ic_baseline_face_24, R.drawable.ic_baseline_explicit_24, R.drawable.ic_baseline_fastfood_24)
        val adapter = ImageSliderAdapter(imgs,this)
        val pager = findViewById<ViewPager>(R.id.pi_covers)
        pager.adapter = adapter


        //юрюр фона
        val blurredBitmap = blur( mContext, imgs[0])
        mLayout.setBackgroundDrawable( BitmapDrawable( resources, blurredBitmap))
        pager.addOnPageChangeListener(object : OnPageChangeListener {//скрол
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val blurredBitmap = blur( mContext, imgs[position])//если проскроли, задали фон
                mLayout.setBackgroundDrawable( BitmapDrawable( resources, blurredBitmap))
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }
}

