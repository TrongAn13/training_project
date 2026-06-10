package com.example.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.data.mapper.MovieMapper.toDomain
import com.example.domain.model.Movie
import com.example.domain.model.MovieCategory
import com.example.network.network.TmdbApi.TmdbApi

class MoviePagingSource(
    private val apiService: TmdbApi,
    private val category: MovieCategory
    ) : PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie>{
        val position =params.key ?: 1
        return try {
            val response = when (category) {
                MovieCategory.POPULAR -> apiService.getPopularMovies(position)
                MovieCategory.UPCOMING -> apiService.getUpcomingMovies(position)
                MovieCategory.TOP_RATED -> apiService.getTopRatedMovies(position)
                MovieCategory.NOW_PLAYING -> apiService.getNowPlayingMovies(position)
                MovieCategory.TRENDING -> apiService.getTrendingMovies()
            }
            val movies = response.results?.map { it.toDomain() } ?: emptyList()
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty() || category == MovieCategory.TRENDING) null else position + 1

            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
                closestPage?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)
                    closestPage?.nextKey?.minus(1)
        }
    }
}