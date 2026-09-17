package br.com.uri.meuprojeto

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class MatchstickScoreView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val matchstickPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C98B4D")
        strokeCap = Paint.Cap.ROUND
    }

    private val matchHeadPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C84335")
        strokeCap = Paint.Cap.ROUND
    }

    var score = 0
        set(value) {
            field = value.coerceIn(0, MAX_SCORE)
            contentDescription = "$field pontos"
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val horizontalPadding = width * 0.1f
        val verticalPadding = height * 0.08f
        val availableWidth = width - (horizontalPadding * 2)
        val availableHeight = height - (verticalPadding * 2)
        val cellWidth = availableWidth / GROUP_COLUMNS
        val cellHeight = availableHeight / GROUP_ROWS
        val squareSize = min(cellWidth * 0.68f, cellHeight * 0.7f)

        matchstickPaint.strokeWidth = min(squareSize * 0.14f, 7f * resources.displayMetrics.density)
        matchHeadPaint.strokeWidth = matchstickPaint.strokeWidth * 1.2f

        repeat(score) { index ->
            val groupIndex = index / POINTS_PER_GROUP
            val matchstickIndex = index % POINTS_PER_GROUP
            val column = groupIndex % GROUP_COLUMNS
            val row = groupIndex / GROUP_COLUMNS
            val centerX = horizontalPadding + (column * cellWidth) + (cellWidth / 2)
            val centerY = verticalPadding + (row * cellHeight) + (cellHeight / 2)
            val left = centerX - (squareSize / 2)
            val top = centerY - (squareSize / 2)
            val right = centerX + (squareSize / 2)
            val bottom = centerY + (squareSize / 2)

            when (matchstickIndex) {
                0 -> drawMatchstick(canvas, left, top, right, top)
                1 -> drawMatchstick(canvas, right, top, right, bottom)
                2 -> drawMatchstick(canvas, right, bottom, left, bottom)
                3 -> drawMatchstick(canvas, left, bottom, left, top)
                4 -> drawMatchstick(canvas, left, bottom, right, top)
            }
        }
    }

    private fun drawMatchstick(
        canvas: Canvas,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float
    ) {
        canvas.drawLine(startX, startY, endX, endY, matchstickPaint)

        val headPosition = 0.2f
        val headEndX = startX + ((endX - startX) * headPosition)
        val headEndY = startY + ((endY - startY) * headPosition)
        canvas.drawLine(startX, startY, headEndX, headEndY, matchHeadPaint)
    }

    companion object {
        private const val POINTS_PER_GROUP = 5
        private const val GROUP_COLUMNS = 2
        private const val GROUP_ROWS = 3
        private const val MAX_SCORE = 30
    }
}
