package com.example.training_project.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.training_project.ui.base.BaseFragment
import androidx.fragment.app.activityViewModels
import com.example.training_project.R
import com.example.training_project.databinding.FragmentAboutBinding

class AboutFragment : BaseFragment() {
    private var _binding: FragmentAboutBinding? = null
    override val viewModel: DetailViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initView() {}

    override fun initListener() {}
    override fun observeLiveData() {
        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movie ->
                if (!movie.overview.isNullOrEmpty()) {
                    binding.tvAboutDescription.text = movie.overview
                } else {
                    binding.tvAboutDescription.text = getString(R.string.detail_about_description)
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
