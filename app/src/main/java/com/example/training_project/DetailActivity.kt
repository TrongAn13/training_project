package com.example.training_project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.training_project.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnBookmark.setOnClickListener {
            Toast.makeText(this, "Đã lưu phim vào danh sách!", Toast.LENGTH_SHORT).show()
        }

        fun selectTab(
            selectedContent: View,
            selectedText: TextView,
            selectedIndicator: View
        ) {
            binding.contentAbout.visibility = View.GONE
            binding.contentReviews.visibility = View.GONE
            binding.contentCast.visibility = View.GONE

            binding.tvTabAbout.setTextColor(Color.parseColor("#92929D"))
            binding.tvTabReviews.setTextColor(Color.parseColor("#92929D"))
            binding.tvTabCast.setTextColor(Color.parseColor("#92929D"))

            binding.indicatorAbout.visibility = View.INVISIBLE
            binding.indicatorReviews.visibility = View.INVISIBLE
            binding.indicatorCast.visibility = View.INVISIBLE

            selectedContent.visibility = View.VISIBLE
            selectedText.setTextColor(Color.parseColor("#FFFFFF"))
            selectedIndicator.visibility = View.VISIBLE
        }


        binding.tvTabAbout.setOnClickListener {
            selectTab(binding.contentAbout, binding.tvTabAbout, binding.indicatorAbout)
        }

        binding.tvTabReviews.setOnClickListener {
            selectTab(binding.contentReviews, binding.tvTabReviews, binding.indicatorReviews)
        }

        binding.tvTabCast.setOnClickListener {
            selectTab(binding.contentCast, binding.tvTabCast, binding.indicatorCast)
        }
    }
}