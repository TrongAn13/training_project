package com.example.training_project.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.training_project.R
import com.example.training_project.databinding.FragmentAboutBinding
import com.example.training_project.utils.handleApiState

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val sharedViewModel: DetailViewModel by activityViewModels()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.movie.observe(viewLifecycleOwner) { resource ->
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