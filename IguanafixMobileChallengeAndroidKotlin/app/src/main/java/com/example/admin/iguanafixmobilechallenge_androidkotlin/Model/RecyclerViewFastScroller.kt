package com.example.admin.iguanafixmobilechallenge_androidkotlin.Model

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class RecyclerViewFastScroller : LinearLayout {

    private var bubble: TextView? = null
    private var handle: View? = null
    private var recyclerView: RecyclerView? = null
    private var height1: Int = 0
    private var isInitialized = false
    private var currentAnimator: ObjectAnimator? = null

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            updateBubbleAndHandlePosition()
        }
    }

    interface BubbleTextGetter {
        fun getTextToShowInBubble(pos: Int): String
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    protected fun init(context: Context) {
        if (isInitialized)
            return
        isInitialized = true
        orientation = LinearLayout.HORIZONTAL
        clipChildren = false
    }

    fun setViewsToUse(@LayoutRes layoutResId: Int, @IdRes bubbleResId: Int, @IdRes handleResId: Int) {
        val inflater = LayoutInflater.from(context)
        inflater.inflate(layoutResId, this, true)
        bubble = findViewById<View>(bubbleResId) as TextView
        if (bubble != null)
            bubble!!.visibility = View.INVISIBLE
        handle = findViewById(handleResId)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        height1 = h
        updateBubbleAndHandlePosition()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x < handle!!.x - ViewCompat.getPaddingStart(handle!!))
                    return false
                if (currentAnimator != null)
                    currentAnimator!!.cancel()
                if (bubble != null && bubble!!.visibility == View.INVISIBLE)
                    showBubble()
                handle!!.isSelected = true
                val y = event.y
                setBubbleAndHandlePosition(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val y = event.y
                setBubbleAndHandlePosition(y)
                setRecyclerViewPosition(y)
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                handle!!.isSelected = false
                hideBubble()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        if (this.recyclerView !== recyclerView) {
            if (this.recyclerView != null)
                this.recyclerView!!.removeOnScrollListener(onScrollListener)
            this.recyclerView = recyclerView
            if (this.recyclerView == null)
                return
            recyclerView.addOnScrollListener(onScrollListener)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (recyclerView != null) {
            recyclerView!!.removeOnScrollListener(onScrollListener)
            recyclerView = null
        }
    }

    private fun setRecyclerViewPosition(y: Float) {
        if (recyclerView != null) {
            val itemCount = recyclerView!!.adapter!!.itemCount
            val proportion: Float
            if (handle!!.y == 0f)
                proportion = 0f
            else if (handle!!.y + handle!!.height >= height1 - TRACK_SNAP_RANGE)
                proportion = 1f
            else
                proportion = y / height1.toFloat()
            val targetPos = getValueInRange(0, itemCount - 1, (proportion * itemCount.toFloat()).toInt())
            (recyclerView!!.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(targetPos, 0)
            val bubbleText = (recyclerView!!.adapter as BubbleTextGetter).getTextToShowInBubble(targetPos)
            if (bubble != null) {
                bubble!!.text = bubbleText
                if (TextUtils.isEmpty(bubbleText)) {
                    hideBubble()
                } else if (bubble!!.visibility == View.INVISIBLE) {
                    showBubble()
                }
            }
        }
    }

    private fun getValueInRange(min: Int, max: Int, value: Int): Int {
        val minimum = Math.max(min, value)
        return Math.min(minimum, max)
    }

    private fun updateBubbleAndHandlePosition() {
        if (bubble == null || handle!!.isSelected || recyclerView == null)
            return

        val verticalScrollOffset = recyclerView!!.computeVerticalScrollOffset()
        val verticalScrollRange = recyclerView!!.computeVerticalScrollRange()
        val proportion = verticalScrollOffset!!.toFloat() / (verticalScrollRange.toFloat() - height1)
        setBubbleAndHandlePosition(height1 * proportion)
    }

    private fun setBubbleAndHandlePosition(y: Float) {
        val handleHeight = handle!!.height
        handle!!.y = getValueInRange(0, height1 - handleHeight, (y - handleHeight / 2).toInt()).toFloat()
        if (bubble != null) {
            val bubbleHeight = bubble!!.height
            bubble!!.y =
                    getValueInRange(0, height1 - bubbleHeight - handleHeight / 2, (y - bubbleHeight).toInt()).toFloat()
        }
    }

    private fun showBubble() {
        if (bubble == null)
            return
        bubble!!.visibility = View.VISIBLE
        if (currentAnimator != null)
            currentAnimator!!.cancel()
        currentAnimator =
                ObjectAnimator.ofFloat(bubble, "alpha", 0f, 1f).setDuration(BUBBLE_ANIMATION_DURATION.toLong())
        currentAnimator!!.start()
    }

    private fun hideBubble() {
        if (bubble == null)
            return
        if (currentAnimator != null)
            currentAnimator!!.cancel()
        currentAnimator =
                ObjectAnimator.ofFloat(bubble, "alpha", 1f, 0f).setDuration(BUBBLE_ANIMATION_DURATION.toLong())
        currentAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                bubble!!.visibility = View.INVISIBLE
                currentAnimator = null
            }

            override fun onAnimationCancel(animation: Animator) {
                super.onAnimationCancel(animation)
                bubble!!.visibility = View.INVISIBLE
                currentAnimator = null
            }
        })
        currentAnimator!!.start()
    }

    companion object {
        private val BUBBLE_ANIMATION_DURATION = 100
        private val TRACK_SNAP_RANGE = 5
    }
}
