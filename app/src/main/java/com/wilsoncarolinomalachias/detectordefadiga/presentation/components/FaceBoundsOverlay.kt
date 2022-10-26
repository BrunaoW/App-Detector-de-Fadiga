package com.wilsoncarolinomalachias.detectordefadiga.presentation.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.FaceLandmark

/** Overlay where face bounds are drawn.  */
class FaceBoundsOverlay constructor(context: Context?, attributeSet: AttributeSet?) :
    View(context, attributeSet) {

    private val faceBounds: MutableList<FaceLandmark> = mutableListOf()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context!!, android.R.color.black)
        strokeWidth = 10f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        faceBounds.forEach {
            canvas.drawCircle(it.position.x, it.position.y, 2f, paint)
        }
    }

    fun drawFaceBounds(faceBounds: List<FaceLandmark>) {
        this.faceBounds.clear()
        this.faceBounds.addAll(faceBounds)
        invalidate()
    }
}