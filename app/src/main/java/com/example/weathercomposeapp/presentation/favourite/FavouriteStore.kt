package com.example.weathercomposeapp.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weathercomposeapp.domain.entity.City
import com.example.weathercomposeapp.domain.usecase.GetCurrentWeatherUseCase
import com.example.weathercomposeapp.domain.usecase.GetFavouriteCitiesUseCase
import com.example.weathercomposeapp.presentation.favourite.FavouriteStore.*
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore : Store<Intent, State, Label> {

    sealed interface Intent {

        //действия, которые может совершить пользователь

        data object ClickSearch : Intent

        data object ClickAddToFavourite : Intent

        data class CityItemClicked(val city: City) : Intent

    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            data object Initial : WeatherState

            data object Loading : WeatherState

            data object Error : WeatherState

            data class Loaded(val tempC: Float, val iconUrl: String) : WeatherState

        }

    }

    sealed interface Label {

        //действия при которых происходит навигация

        data object ClickSearch : Label

        data object ClickToFavourite : Label

        data class CityItemClicked(val city: City) : Label

    }

}


class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavouriteStore",
            initialState = State(cityItems = listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        //загрузка из репозитория при создании FavouriteStore

        data class FavouriteCitiesLoaded(val cities: List<City>) : Action

    }

    private sealed interface Msg {

        //действия при которых меняется стейт экрана

        data class FavouriteCitiesLoaded(val cities: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Long,
            val tempC: Float,
            val iconUrl: String
        ) : Msg

        data class WeatherLoadingError(
            val cityId: Long
        ) : Msg

        data class WeatherIsLoading(
            val cityId: Long
        ) : Msg

    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {

        //загрузка из репозитория при создании FavouriteStore

        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect { cityList ->
                    dispatch(Action.FavouriteCitiesLoaded(cityList))
                }
            }
        }

    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        //обработка интентов

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.CityItemClicked -> {
                    publish(Label.CityItemClicked(intent.city))
                }

                is Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }

                is Intent.ClickAddToFavourite -> {
                    publish(Label.ClickToFavourite)
                }
            }
        }

        //обработка экшенов

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach { city ->
                        scope.launch {
                            loadWeatherForCity(city)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase(city.id)
                dispatch(
                    Msg.WeatherLoaded(
                        cityId = city.id,
                        tempC = weather.tempC,
                        iconUrl = weather.conditionUrl
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }

        }

    }

    private object ReducerImpl : Reducer<State, Msg> {

        //обработка сообщений, изменение стейта

        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavouriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            weatherState = State.WeatherState.Initial
                        )
                    }
                )
            }

            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                             it.copy(
                                 weatherState = State.WeatherState.Loading
                             )
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    iconUrl = msg.iconUrl
                                )
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Error
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }

    }

}