package com.example.domain.usecase

import com.example.domain.repository.MovieRepository

class ClearSearchHistoryUseCase(private val repository: MovieRepository){
    suspend operator fun invoke(){
        repository.clearSearchHistory()
    }
}
