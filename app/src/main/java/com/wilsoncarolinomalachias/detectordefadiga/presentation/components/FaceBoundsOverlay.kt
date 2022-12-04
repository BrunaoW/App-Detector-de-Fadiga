package com.wilsoncarolinomalachias.detectordefadiga.presentation.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Size
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.face.FaceLandmark
import com.wilsoncarolinomalachias.detectordefadiga.R

/** Overlay where face bounds are drawn.  */
class FaceBoundsOverlay constructor(
    context: Context?,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {

    private val faceBounds: MutableList<PointF> = mutableListOf()
    private val paint = Paint().apply {
        style = Paint.Style.FILL
        color = ContextCompat.getColor(context!!, R.color.red)
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        faceBounds.forEach {
            val actualX = (width - (it.x * (3.5/4))).toFloat()
            val actualY = (it.y * (3.5/4)).toFloat()

            canvas.drawCircle(actualX, actualY, 5f, paint)
        }
    }

    fun drawFaceBounds(faceBounds: List<PointF>) {
        this.faceBounds.clear()
        this.faceBounds.addAll(faceBounds)
        invalidate()
    }
}