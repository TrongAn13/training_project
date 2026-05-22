package com.example.training_project.domain.usecase

import com.example.training_project.domain.repository.MovieRepository

class ClearSearchHistoryUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(){
        repository.clearSearchHistory()
    }
}
