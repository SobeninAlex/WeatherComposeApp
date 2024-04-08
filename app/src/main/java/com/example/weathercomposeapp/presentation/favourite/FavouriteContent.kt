package com.example.weathercomposeapp.presentation.favourite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weathercomposeapp.R
import com.example.weathercomposeapp.presentation.ui.CardGradients
import com.example.weathercomposeapp.presentation.ui.Gradient
import com.example.weathercomposeapp.presentation.ui.theme.Orange
import com.example.weathercomposeapp.utill.temperatureToFormattedString


@Composable
fun FavouriteContent(
    modifier: Modifier = Modifier,
    component: FavouriteComponent
) {

    val state by component.model.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item(
                span = { GridItemSpan(2) } //данный item займет две колонки грида
            ) {
                SearchCard(
                    onClick = { component.onClickSearch() }
                )
            }

            itemsIndexed(
                items = state.cityItems,
                key = { _, item ->
                    item.city.id
                }
            ) { index, item ->
                CityCard(
                    cityItem = item,
                    index = index,
                    onClick = { component.onCityItemClick(item.city) }
                )
            }

            item {
                AddFavouriteCityCard(
                    onClick = { component.onClickAddFavourite() }
                )
            }
        }
    }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    cityItem: FavouriteStore.State.CityItem,
    index: Int,
    onClick: () -> Unit
) {
    val gradient = getGradientByIndex(index)

    Card(
        modifier = modifier
            .fillMaxSize()
            .shadow(
                elevation = 12.dp,
                spotColor = gradient.shadowColor,
                shape = MaterialTheme.shapes.extraLarge
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Blue
        ),
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Box(modifier = modifier
            .background(brush = gradient.primaryGradient)
            .fillMaxSize()
            .sizeIn(minHeight = 176.dp)
            //позволяет рисовать на элементе Box как на Canvas
            .drawBehind {
                drawCircle(
                    brush = gradient.secondaryGradient,
                    center = Offset(
                        x = center.x - size.width / 4,
                        y = center.y - size.height / 2
                    ),
                    radius = size.maxDimension / 2
                )
            }
            .clickable { onClick() }
            .padding(20.dp)
        ) {
            when (val weatherState = cityItem.weatherState) {
                is FavouriteStore.State.WeatherState.Error -> {}

                is FavouriteStore.State.WeatherState.Initial -> {}

                is FavouriteStore.State.WeatherState.Loaded -> {
                    GlideImage(
                        modifier = modifier
                            .align(alignment = Alignment.TopEnd)
                            .size(56.dp),
                        model = weatherState.iconUrl,
                        contentDescription = null
                    )
                    Text(
                        modifier = modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 24.dp),
                        text = weatherState.tempC.temperatureToFormattedString(),
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 48.sp
                    )
                }

                is FavouriteStore.State.WeatherState.Loading -> {
                    CircularProgressIndicator(
                        modifier = modifier
                            .align(Alignment.Center),
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

            Text(
                modifier = modifier.align(Alignment.BottomStart),
                text = cityItem.city.city,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.background
            )
        }
    }
}


@Composable
private fun AddFavouriteCityCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            modifier = modifier
                .sizeIn(minHeight = 176.dp)
                .fillMaxSize()
                .clickable { onClick() }
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(
                modifier = modifier
                    .padding(16.dp)
                    .size(48.dp),
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = Orange
            )

            Spacer(modifier = modifier.weight(1f))

            Text(
                text = stringResource(R.string.add_favourite),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@Composable
private fun SearchCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val gradient = CardGradients.gradients[3]

    Card(
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Row(
            modifier = modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .background(brush = gradient.primaryGradient),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = null,
                modifier = modifier.padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
            )

            Text(
                modifier = modifier.padding(end = 16.dp),
                text = stringResource(R.string.search_button),
                color = MaterialTheme.colorScheme.surface,
            )
        }
    }
}


private fun getGradientByIndex(index: Int): Gradient {
    val gradients = CardGradients.gradients
    return gradients[index % gradients.size]
}
