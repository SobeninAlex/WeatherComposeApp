package com.example.weathercomposeapp.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.entity.Forecast
import com.example.weathercomposeapp.domain.usecase.ChangeFavouriteStateUseCase
import com.example.weathercomposeapp.domain.usecase.GetForecastUseCase
import com.example.weathercomposeapp.domain.usecase.ObserveFavouriteStateCitiesUseCase
import com.example.weathercomposeapp.presentation.details.DetailsStore.Intent
import com.example.weathercomposeapp.presentation.details.DetailsStore.Label
import com.example.weathercomposeapp.presentation.details.DetailsStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        //действия, которые может совершить пользователь

        data object ClickBack : Intent

        data object ClickChangeFavouriteStatus : Intent

    }

    data class State(
        val city: City,
        val isFavourite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Loaded(
                val forecast: Forecast
            ) : ForecastState

        }

    }

    sealed interface Label {

        //действия при которых происходит навигация

        data object ClickBack : Label

    }
}


class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val getForecastUseCase: GetForecastUseCase,
    private val observeFavouriteStateCitiesUseCase: ObserveFavouriteStateCitiesUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavourite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        //загрузка из репозитория при создании FavouriteStore

        data class FavouriteStatusChange(val isFavourite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastStartLoading : Action

        data object ForecastLoadingError : Action

    }

    private sealed interface Msg {

        //действия при которых меняется стейт экрана

        data class FavouriteStatusChange(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastStartLoading : Msg

        data object ForecastLoadingError : Msg

    }

    private inner class BootstrapperImpl(private val city: City) : CoroutineBootstrapper<Action>() {

        //загрузка из репозитория при создании FavouriteStore

        override fun invoke() {
            scope.launch {
                observeFavouriteStateCitiesUseCase(city.id).collect { isFavourite ->
                    dispatch(Action.FavouriteStatusChange(isFavourite))
                }
            }

            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        //обработка интентов

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                is Intent.ClickChangeFavouriteStatus -> {
                    scope.launch {
                        val state = getState()
                        if (state.isFavourite) {
                            changeFavouriteStateUseCase.removeFromFavourite(state.city.id)
                        } else {
                            changeFavouriteStateUseCase.addToFavourite(state.city)
                        }
                    }
                }
            }
        }

        //обработка экшенов

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteStatusChange -> {
                    dispatch(Msg.FavouriteStatusChange(action.isFavourite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                is Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }

                is Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {

        //обработка сообщений, изменение стейта

        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavouriteStatusChange -> {
                copy(
                    isFavourite = msg.isFavourite
                )
            }

            is Msg.ForecastLoaded -> {
                copy(
                    forecastState = State.ForecastState.Loaded(msg.forecast)
                )
            }

            is Msg.ForecastLoadingError -> {
                copy(
                    forecastState = State.ForecastState.Error
                )
            }

            is Msg.ForecastStartLoading -> {
                copy(
                    forecastState = State.ForecastState.Loading
                )
            }
        }
    }

}
