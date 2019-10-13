package com.financial.gavin.floodfillapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floodFillAdapter = FloodFillAdapter(initColors(), this)
        grid.run {
            adapter = floodFillAdapter
            layoutManager = GridLayoutManager(this@MainActivity, GRID_SIZE)
        }

        retry.setOnClickListener {
            floodFillAdapter.reset(initColors())
        }
    }

    private fun initColors(): ArrayList<Int> {
        val colors = ArrayList<Int>()
        for (index in 0 until GRID_SIZE * GRID_SIZE) {
            when(Random().nextBoolean()) {
                true -> colors.add(Color.WHITE)
                false -> colors.add(Color.BLACK)
            }
        }

        return colors
    }
}
