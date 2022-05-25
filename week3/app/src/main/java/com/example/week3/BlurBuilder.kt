package com.example.week3

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.DisplayMetrics
import androidx.core.graphics.drawable.toBitmap


object BlurBuilder {
    private const val BITMAP_SCALE = 0.1f
    private const val BLUR_RADIUS = 10f
    fun blur(context: Context, drawableId: Int): Bitmap {
        //val image = BitmapFactory.decodeResource(context.resources, drawableId)

        /*val drawable: Drawable = context.resources.getDrawable(drawableId)

        if (drawable is BitmapDrawable) {
            return (drawable as BitmapDrawable).bitmap
        }

        val image = Bitmap.createBitmap(
            drawable.getIntrinsicWidth(),
            drawable.getIntrinsicHeight(),
            Bitmap.Config.ARGB_8888
        )*/
        val drawable = context.resources.getDrawable( drawableId)
        val image = drawable.toBitmap(width = 100, height = 100, config = null)

        val width = Math.round(image.width * BITMAP_SCALE)
        val height = Math.round(image.height * BITMAP_SCALE)
        val inputBitmap = Bitmap.createScaledBitmap(image, width, height, false)
        val outputBitmap = Bitmap.createBitmap(inputBitmap)

        val rs = RenderScript.create(context)
        val theIntrinsic = ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs))
        val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
        val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)
        theIntrinsic.setRadius(BLUR_RADIUS)
        theIntrinsic.setInput(tmpIn)
        theIntrinsic.forEach(tmpOut)
        tmpOut.copyTo(outputBitmap)

        return outputBitmap
    }
}