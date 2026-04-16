package com.example.training_project
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.training_project.databinding.ActivityDetailBinding
import androidx.core.content.ContextCompat

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
            Toast.makeText(this, R.string.save_movie, Toast.LENGTH_SHORT).show()
        }

        fun selectTab(
            selectedContent: View,
            selectedText: TextView,
            selectedIndicator: View
        ) {
            binding.contentAbout.visibility = View.GONE
            binding.contentReviews.visibility = View.GONE
            binding.contentCast.visibility = View.GONE

            val inactiveColor = ContextCompat.getColor(this, R.color.text_secondary_gray)
            binding.tvTabAbout.setTextColor(inactiveColor)
            binding.tvTabReviews.setTextColor(inactiveColor)
            binding.tvTabCast.setTextColor(inactiveColor)

            binding.indicatorAbout.visibility = View.INVISIBLE
            binding.indicatorReviews.visibility = View.INVISIBLE
            binding.indicatorCast.visibility = View.INVISIBLE

            selectedContent.visibility = View.VISIBLE
            selectedText.setTextColor(R.color.white)
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