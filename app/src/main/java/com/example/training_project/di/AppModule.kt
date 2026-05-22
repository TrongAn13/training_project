package com.example.training_project.di

import com.example.training_project.BuildConfig
import com.example.training_project.data.local.AppDatabase
import com.example.training_project.data.network.TmdbApi
import com.example.training_project.data.repository.AuthRepositoryImpl
import com.example.training_project.data.repository.MovieRepositoryImpl
import com.example.training_project.domain.repository.AuthRepository
import com.example.training_project.domain.repository.MovieRepository
import com.example.training_project.domain.usecase.ClearSearchHistoryUseCase
import com.example.training_project.domain.usecase.DeleteFavoriteMovieUseCase
import com.example.training_project.domain.usecase.GetCachedMoviesUseCase
import com.example.training_project.domain.usecase.GetFavoriteMoviesUseCase
import com.example.training_project.domain.usecase.GetMovieDetailsUseCase
import com.example.training_project.domain.usecase.GetMoviesUseCase
import com.example.training_project.domain.usecase.GetSearchHistoryUseCase
import com.example.training_project.domain.usecase.IncreaseDetailViewCount
import com.example.training_project.domain.usecase.IsMovieSavedUseCase
import com.example.training_project.domain.usecase.LoginUseCase
import com.example.training_project.domain.usecase.MovieUseCases
import com.example.training_project.domain.usecase.RefreshMoviesUseCase
import com.example.training_project.domain.usecase.SaveFavoriteMovieUseCase
import com.example.training_project.domain.usecase.SaveSearchHistoryUseCase
import com.example.training_project.domain.usecase.SearchMoviesUseCase
import com.example.training_project.ui.auth.LoginViewModel
import com.example.training_project.ui.auth.PreferenceManager
import com.example.training_project.ui.detail.DetailViewModel
import com.example.training_project.ui.favorite.FavoriteViewModel
import com.example.training_project.ui.home.HomeViewModel
import com.example.training_project.ui.search.SearchViewModel
import com.example.training_project.utils.ResourceProvider
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"
val appModule = module {
    single {
        PreferenceManager(androidApplication())
    }
    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("Accept-Encoding", "identity")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_TOKEN}")
                    .addHeader("accept", "application/json")
                    .build()
                chain.proceed(request)
            }.build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<TmdbApi> {
        get<Retrofit>().create(TmdbApi::class.java)
    }
    single {
        ResourceProvider(androidApplication())
    }
    single {
        AppDatabase.getDatabase(androidApplication())
    }
    single {
        get<AppDatabase>().movieDao()
        }
    single {
        get<AppDatabase>().searchHistoryDao()
    }
    single {
        get<AppDatabase>().favoriteMovieDao()
    }
    single<MovieRepository> {
        MovieRepositoryImpl(apiService = get(), movieDao = get(), searchHistoryDao = get(), favoriteMovieDao = get())
    }
    single<AuthRepository> {
        AuthRepositoryImpl(get())
    }
    single { LoginUseCase(get()) }
    single { GetMoviesUseCase(get()) }
    single { SearchMoviesUseCase(get()) }
    single { GetMovieDetailsUseCase(get()) }
    single { GetCachedMoviesUseCase(get()) }
    single { RefreshMoviesUseCase(get()) }
    single { ClearSearchHistoryUseCase(get()) }
    single { GetSearchHistoryUseCase(get()) }
    single { SaveSearchHistoryUseCase(get()) }

    single { GetFavoriteMoviesUseCase(get()) }
    single { DeleteFavoriteMovieUseCase(get()) }
    single { SaveFavoriteMovieUseCase(get()) }
    single { IsMovieSavedUseCase(get()) }
    single { IncreaseDetailViewCount(get()) }

    single {
        MovieUseCases(
            getMovies = get(),
            searchMovies = get(),
            getMovieDetails = get(),
            getCachedMovies = get(),
            refreshMovies = get(),
            getSearchHistory = get(),
            saveSearchHistory = get(),
            clearSearchHistory = get(),
            getFavoriteMovies = get(),
            deleteFavoriteMovie = get(),
            saveFavoriteMovie = get(),
            isMovieSaved = get()
            ,increaseDetailViewCount = get()
        )
    }
    viewModel {
        LoginViewModel(resourceProvider = get(), loginUseCase = get())
    }
    viewModel {
        HomeViewModel(resourceProvider = get(), useCases = get())
    }
    viewModel {
        DetailViewModel(resourceProvider = get(), useCases = get())
    }
    viewModel {
        SearchViewModel(resourceProvider = get(), useCases = get())
    }
    viewModel {
        LoginViewModel(resourceProvider = get(), loginUseCase = get())
    }
    viewModel {
        FavoriteViewModel(resourceProvider = get(), useCases = get())
    }
}