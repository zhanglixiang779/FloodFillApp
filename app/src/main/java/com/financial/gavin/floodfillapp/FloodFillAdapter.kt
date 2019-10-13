package com.financial.gavin.floodfillapp

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.util.DisplayMetrics
import android.widget.FrameLayout
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class FloodFillAdapter(
    private var colors: ArrayList<Int>,
    private val activity: Activity
) : RecyclerView.Adapter<FloodFillAdapter.FloodFillViewHolder>() {

    private var grid = arrayOf<Array<Int>>()

    init {
        for (i in 0 until 10) {
            var array = arrayOf<Int>()
            for (j in 0 until 10) {
                array += 0
            }
            grid += array
        }
    }

    internal fun reset(colors: ArrayList<Int>) {
        this.colors = colors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FloodFillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false))

    override fun getItemCount() = colors.size

    override fun onBindViewHolder(holder: FloodFillViewHolder, position: Int) {
        holder.onBind(colors[position], position)
    }

    inner class FloodFillViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private fun getScreenWidth(activity: Activity): Int {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        private fun isInGrid(row: Int, col: Int) = row in 0 ..9 && col in 0 .. 9

        private fun floodFill(row: Int, col: Int) {
            val queue = LinkedList<Pair<Int, Int>>()
            queue.add(Pair(row, col))
            while (queue.isNotEmpty()) {
                val pair = queue.poll()
                val newRow = pair.first
                val newCol = pair.second
                colors[newRow * 10 + newCol] = Color.GREEN
                grid[newRow][newCol] = Color.GREEN

                //left
                if (isInGrid(newRow, newCol - 1)
                    && grid[newRow][newCol - 1] == Color.WHITE) {
                    queue.add(Pair(newRow, newCol - 1))
                }

                //top
                if (isInGrid(newRow - 1, newCol)
                    && grid[newRow - 1][newCol] == Color.WHITE) {
                    queue.add(Pair(newRow - 1, newCol))
                }

                //right
                if (isInGrid(newRow, newCol + 1)
                    && grid[newRow][newCol + 1] == Color.WHITE) {
                    queue.add(Pair(newRow, newCol + 1))
                }

                //bottom
                if (isInGrid(newRow + 1, newCol)
                    && grid[newRow + 1][newCol] == Color.WHITE) {
                    queue.add(Pair(newRow + 1, newCol))
                }
            }
        }

        fun onBind(color: Int, position: Int) {
            val row = position / 10
            val col = position % 10
            grid[row][col] = color

            (itemView as? FrameLayout)?.run {
                setBackgroundColor(color)
                layoutParams.width = getScreenWidth(activity) / 10
                layoutParams.height = getScreenWidth(activity) / 10
                setOnClickListener {
                    if (color == Color.WHITE) {
                        Executors.newSingleThreadExecutor().execute {
                            floodFill(row, col)
                            post {
                                notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        }
    }
}