package com.loong.ihms.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntDef
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpaceItemDecoration(private val spacing: Int = 8.dp) : RecyclerView.ItemDecoration() {
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(
        DisplayMode.VERTICAL,
        DisplayMode.HORIZONTAL,
        DisplayMode.GRID
    )
    annotation class DisplayMode {
        companion object {
            const val VERTICAL = 0
            const val HORIZONTAL = 1
            const val GRID = 2
        }
    }

    private var displayMode: Int = DisplayMode.GRID

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        setSpacingForDirection(
            outRect,
            view,
            parent.layoutManager,
            parent.getChildViewHolder(view).adapterPosition,
            state.itemCount
        )
    }

    private fun setSpacingForDirection(
        outRect: Rect,
        childView: View,
        layoutManager: RecyclerView.LayoutManager?,
        position: Int,
        itemCount: Int
    ) {
        // Resolve display mode automatically
        displayMode = resolveDisplayMode(layoutManager)

        when (displayMode) {
            DisplayMode.VERTICAL -> {
                outRect.left = spacing
                outRect.right = spacing
                outRect.top = if (position == 0) spacing else spacing / 2
                outRect.bottom = if (position == itemCount - 1) spacing else spacing / 2
            }

            DisplayMode.HORIZONTAL -> {
                outRect.left = if (position == 0) spacing else spacing / 2
                outRect.right = if (position == itemCount - 1) spacing else spacing / 2
                outRect.top = spacing
                outRect.bottom = spacing
            }

            DisplayMode.GRID -> {
                var spanCount = 0
                var spanIndex = 0
                var isVertical = true

                if (layoutManager is GridLayoutManager) {
                    spanCount = layoutManager.spanCount
                    spanIndex = (childView.layoutParams as GridLayoutManager.LayoutParams).spanIndex
                    isVertical = layoutManager.orientation == RecyclerView.VERTICAL
                } else if (layoutManager is StaggeredGridLayoutManager) {
                    spanCount = layoutManager.spanCount
                    spanIndex = (childView.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
                    isVertical = layoutManager.orientation == RecyclerView.VERTICAL
                }

                if (isVertical) {
                    outRect.left = if (spanIndex == 0) spacing else spacing / 2
                    outRect.right = if ((spanIndex + 1) == spanCount) spacing else spacing / 2
                    outRect.top = if (position < spanCount) spacing else spacing / 2
                    outRect.bottom = if (position >= (itemCount - spanCount)) spacing else spacing / 2
                } else {
                    outRect.left = if (position < spanCount) spacing else spacing / 2
                    outRect.right = if (position >= (itemCount - spanCount)) spacing else spacing / 2
                    outRect.top = if (spanIndex == 0) spacing else spacing / 2
                    outRect.bottom = if ((spanIndex + 1) == spanCount) spacing else spacing / 2
                }
            }
        }
    }

    private fun resolveDisplayMode(layoutManager: RecyclerView.LayoutManager?): Int {
        return when (layoutManager) {
            is GridLayoutManager -> DisplayMode.GRID
            is LinearLayoutManager -> {
                if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) DisplayMode.HORIZONTAL
                else DisplayMode.VERTICAL
            }
            else -> DisplayMode.GRID
        }
    }
}