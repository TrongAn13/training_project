package com.example.training_project.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.training_project.ui.base.BaseFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.training_project.databinding.FragmentReviewsBinding

class ReviewsFragment : BaseFragment() {

    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewAdapter
    override val viewModel: DetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {
        reviewAdapter = ReviewAdapter()
        binding.rvReviews.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewAdapter
        }
    }

    override fun initListener() {}
    override fun observeLiveData() {

        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movie ->
                val reviewList = movie.reviews?.results ?: emptyList()
                reviewAdapter.submitList(reviewList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
