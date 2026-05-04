package com.example.training_project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.FragmentCastBinding

class CastFragment : Fragment() {

    private var _binding: FragmentCastBinding? = null
    private val binding get() = _binding!!
    private lateinit var castAdapter: CastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        castAdapter = CastAdapter()
        binding.rvCast.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = castAdapter
        }

        val activity = activity as? DetailActivity
        activity?.movieLiveData?.observe(viewLifecycleOwner) { movie ->
            val casts = movie.credits?.cast ?: emptyList()
            castAdapter.submitList(casts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}