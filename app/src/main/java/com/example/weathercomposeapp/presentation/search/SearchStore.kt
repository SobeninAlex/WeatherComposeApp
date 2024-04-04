package com.example.weathercomposeapp.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.usecase.ChangeFavouriteStateUseCase
import com.example.weathercomposeapp.domain.usecase.SearchCitiesUseCase
import com.example.weathercomposeapp.presentation.search.SearchStore.Intent
import com.example.weathercomposeapp.presentation.search.SearchStore.Label
import com.example.weathercomposeapp.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {

        //действия, которые может совершить пользователь

        data class ChangeSearchQuery(val searchQuery: String) : Intent

        data object ClickBack : Intent

        data object ClickSearch : Intent

        data class ClickCity(val city: City) : Intent

    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {

            data object Initial : SearchState

            data object Loading : SearchState

            data object Error : SearchState

            data object EmptyResult : SearchState

            data class SuccessLoaded(val cities: List<City>) : SearchState

        }

    }

    sealed interface Label {

        //действия при которых происходит навигация

        data object ClickBack : Label

        data object SavedToFavourite : Label

        data class OpenForecast(val city: City) : Label

    }

}


class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCitiesUseCase: SearchCitiesUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        //загрузка из репозитория при создании FavouriteStore
    }

    private sealed interface Msg {

        //действия при которых меняется стейт экрана

        data class ChangeSearchQuery(val searchQuery: String) : Msg

        data object LoadingSearchResult : Msg

        data object SearchResultError : Msg

        data class SearchResultLoaded(val cities: List<City>) : Msg

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        //загрузка из репозитория при создании FavouriteStore
        override fun invoke() {}
    }

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        //обработка интентов

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.searchQuery))
                }

                is Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.AddToFavourite -> {
                            scope.launch {
                                changeFavouriteStateUseCase.addToFavourite(intent.city)
                                publish(Label.SavedToFavourite)
                            }
                        }

                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
                }

                is Intent.ClickSearch -> {
                    searchJob?.cancel()

                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        try {
                            val searchQuery = getState().searchQuery
                            val cities = searchCitiesUseCase(searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }
                }
            }
        }

        //обработка экшенов
        override fun executeAction(action: Action, getState: () -> State) {
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {

        //обработка сообщений, изменение стейта

        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(
                    searchQuery = msg.searchQuery
                )
            }

            is Msg.LoadingSearchResult -> {
                copy(
                    searchState = State.SearchState.Loading
                )
            }

            is Msg.SearchResultError -> {
                copy(
                    searchState = State.SearchState.Error
                )
            }

            is Msg.SearchResultLoaded -> {
                val searchState = if (msg.cities.isEmpty()) {
                    State.SearchState.EmptyResult
                } else {
                    State.SearchState.SuccessLoaded(msg.cities)
                }

                copy(searchState = searchState)
            }
        }

    }

}
