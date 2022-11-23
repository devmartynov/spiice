package io.devmartynov.spiice.ui.notesList

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.TextPaint
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.devmartynov.spiice.R

class SwiperCallback(
    private val context: Context,
    private val config: Config,
    dragDirs: Int,
    swipeDirs: Int,
): ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        config.deleteNote(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        try {
            if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
                return
            }

            if (dX < 0) { // Свайп влево
                val dimensions = arrayOf(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX,
                        0F,
                        recyclerView.context.resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX,
                        0F,
                        recyclerView.context.resources.displayMetrics
                    ).toInt(),
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_PX,
                        0F,
                        recyclerView.context.resources.displayMetrics
                    ).toInt()
                )

                canvas.clipRect(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top,
                    viewHolder.itemView.right,
                    viewHolder.itemView.bottom
                )
                val background = ColorDrawable(context.getColor(R.color.red))
                background.setBounds(
                    viewHolder.itemView.right + dX.toInt(),
                    viewHolder.itemView.top + dimensions[0],
                    viewHolder.itemView.right - dimensions[1],
                    viewHolder.itemView.bottom - dimensions[2]
                )
                background.draw(canvas)
                var iconSize = 0
                var imgLeft = viewHolder.itemView.right
                val iconHorizontalMargin = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 24f, recyclerView.context.resources.displayMetrics
                ).toInt();

                if (dX < -iconHorizontalMargin) {
                    val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete)
                    if (icon != null) {
                        iconSize = icon.intrinsicHeight
                        val halfIcon = iconSize / 2
                        val top =
                            viewHolder.itemView.top + ((viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfIcon)
                        imgLeft = viewHolder.itemView.right - iconHorizontalMargin - dimensions[1] - halfIcon * 2
                        icon.setBounds(
                            imgLeft,
                            top,
                            viewHolder.itemView.right - iconHorizontalMargin - dimensions[1],
                            top + icon.intrinsicHeight
                        )
                        icon.draw(canvas)
                    }
                }
                if (dX < -iconHorizontalMargin - dimensions[1] - iconSize) {
                    val textPaint = TextPaint()
                    textPaint.isAntiAlias = true
                    textPaint.textSize = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16f, recyclerView.context.resources.displayMetrics
                    )
                    textPaint.color = context.getColor(R.color.white)
                    textPaint.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    val removeLabel = context.getString(R.string.remove_label)
                    val width = textPaint.measureText(removeLabel)
                    val textTop =
                        (viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2.0 + textPaint.textSize / 2).toInt()
                    canvas.drawText(
                        removeLabel,
                        imgLeft - width - if (imgLeft == viewHolder.itemView.right) iconHorizontalMargin else iconHorizontalMargin / 2,
                        textTop.toFloat(),
                        textPaint
                    )
                }
            }
        } catch (e: Exception) {

        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    interface Config {
        fun deleteNote(position: Int)
    }
}