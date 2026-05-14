package com.example.training_project.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.training_project.ui.base.BaseFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.training_project.databinding.FragmentCastBinding
class CastFragment: BaseFragment() {
    private var _binding: FragmentCastBinding? = null
    private val binding get() = _binding!!
    private lateinit var castAdapter: CastAdapter
    override val viewModel: DetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCastBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initView() {
        castAdapter = CastAdapter()
        binding.rvCast.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = castAdapter
        }
    }

    override fun initListener() {}

    override fun observeLiveData() {
        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            handleApiState(resource) { movie ->
                val castList = movie.credits?.cast ?: emptyList()
                castAdapter.submitList(castList)
            }
        }
    }
}
