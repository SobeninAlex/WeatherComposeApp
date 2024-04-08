package com.example.weathercomposeapp.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weathercomposeapp.R
import com.example.weathercomposeapp.domain.entity.Forecast
import com.example.weathercomposeapp.domain.entity.Weather
import com.example.weathercomposeapp.presentation.ui.CardGradients
import com.example.weathercomposeapp.utill.formattedFullDate
import com.example.weathercomposeapp.utill.formattedShortDayOfWeek
import com.example.weathercomposeapp.utill.temperatureToFormattedString

@Composable
fun DetailsContent(
    modifier: Modifier = Modifier,
    component: DetailsComponent
) {

    val state by component.model.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = Color.White,
        modifier = modifier
            .fillMaxSize()
            .background(brush = CardGradients.gradients[1].primaryGradient),
        topBar = {
            TopBar(
                cityName = state.city.city,
                isCityFavourite = state.isFavourite,
                onBackClick = { component.onClickBack() },
                onClickChangeFavouriteStatus = { component.onClickChangeFavouriteStatus() }
            )
        },
    ) { paddingValues ->
        Box(
            modifier = modifier.padding(paddingValues)
        ) {
            when (val forecastState = state.forecastState) {
                is DetailsStore.State.ForecastState.Error -> {
                    Error()
                }

                is DetailsStore.State.ForecastState.Initial -> {
                    Initial()
                }

                is DetailsStore.State.ForecastState.Loaded ->
                    Forecast(forecast = forecastState.forecast)

                is DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    modifier: Modifier = Modifier,
    cityName: String,
    isCityFavourite: Boolean,
    onBackClick: () -> Unit,
    onClickChangeFavouriteStatus: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = cityName)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickChangeFavouriteStatus() }) {
                val icon = if (isCityFavourite) {
                    Icons.Default.Star
                } else {
                    Icons.Default.StarBorder
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
private fun Error(
    modifier: Modifier = Modifier,
) {

}

@Composable
private fun Initial(
    modifier: Modifier = Modifier,
) {

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(
    modifier: Modifier = Modifier,
    forecast: Forecast
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = modifier.weight(1f))

        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.temperatureToFormattedString(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 70.sp
                )
            )

            GlideImage(
                modifier = modifier.size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }

        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = modifier.weight(1f))

        AnimatedUpcomingWeather(upcoming = forecast.upcoming)

        Spacer(modifier = modifier.weight(0.5f))
    }
}

@Composable
private fun AnimatedUpcomingWeather(
    upcoming: List<Weather>
) {

    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(500)) + slideIn(
            animationSpec = tween(500),
            initialOffset = {
                IntOffset(x = 0, it.height)
            }
        )
    ) {
        UpcomingWeather(upcoming = upcoming)
    }
}

@Composable
private fun UpcomingWeather(
    modifier: Modifier = Modifier,
    upcoming: List<Weather>
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.24f)
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineMedium,
                modifier = modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach { weather ->
                    SmallWeatherCard(weather = weather)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(
    modifier: Modifier = Modifier,
    weather: Weather
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = modifier
            .height(128.dp)
            .weight(1f)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = weather.tempC.temperatureToFormattedString(),
                color = Color.Black
            )

            GlideImage(
                modifier = modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )

            Text(
                text = weather.date.formattedShortDayOfWeek(),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
