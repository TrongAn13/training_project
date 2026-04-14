package com.example.training_project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        val btnBookmark = findViewById<ImageView>(R.id.btnBookmark)

        val tabAbout = findViewById<LinearLayout>(R.id.tabAbout)
        val tabReviews = findViewById<LinearLayout>(R.id.tabReviews)
        val tabCast = findViewById<LinearLayout>(R.id.tabCast)

        val tvTabAbout = findViewById<TextView>(R.id.tvTabAbout)
        val tvTabReviews = findViewById<TextView>(R.id.tvTabReviews)
        val tvTabCast = findViewById<TextView>(R.id.tvTabCast)

        val indicatorAbout = findViewById<View>(R.id.indicatorAbout)
        val indicatorReviews = findViewById<View>(R.id.indicatorReviews)
        val indicatorCast = findViewById<View>(R.id.indicatorCast)

        val contentAbout = findViewById<LinearLayout>(R.id.contentAbout)
        val contentReviews = findViewById<LinearLayout>(R.id.contentReviews)
        val contentCast = findViewById<LinearLayout>(R.id.contentCast)

        btnBack.setOnClickListener {
            finish()
        }

        btnBookmark.setOnClickListener {
            Toast.makeText(this, "Đã lưu phim vào danh sách!", Toast.LENGTH_SHORT).show()
        }

        fun selectTab(
            selectedContent: LinearLayout,
            selectedText: TextView,
            selectedIndicator: View
        ) {
            contentAbout.visibility = View.GONE
            contentReviews.visibility = View.GONE
            contentCast.visibility = View.GONE

            tvTabAbout.setTextColor(Color.parseColor("#92929D"))
            tvTabReviews.setTextColor(Color.parseColor("#92929D"))
            tvTabCast.setTextColor(Color.parseColor("#92929D"))

            indicatorAbout.visibility = View.INVISIBLE
            indicatorReviews.visibility = View.INVISIBLE
            indicatorCast.visibility = View.INVISIBLE

            selectedContent.visibility = View.VISIBLE
            selectedText.setTextColor(Color.parseColor("#FFFFFF"))
            selectedIndicator.visibility = View.VISIBLE
        }


        tabAbout.setOnClickListener {
            selectTab(contentAbout, tvTabAbout, indicatorAbout)
        }

        tabReviews.setOnClickListener {
            selectTab(contentReviews, tvTabReviews, indicatorReviews)
        }

        tabCast.setOnClickListener {
            selectTab(contentCast, tvTabCast, indicatorCast)
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}