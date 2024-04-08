package com.example.weathercomposeapp.presentation.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.example.weathercomposeapp.presentation.details.DetailsContent
import com.example.weathercomposeapp.presentation.favourite.FavouriteContent
import com.example.weathercomposeapp.presentation.search.SearchContent
import com.example.weathercomposeapp.presentation.ui.theme.WeatherComposeAppTheme


@Composable
fun RootContent(
    component: RootComponent
) {

    WeatherComposeAppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            Children(
                stack = component.stack
            ) {
                when (val instance = it.instance) {
                    is RootComponent.Child.Details -> {
                        DetailsContent(component = instance.component)
                    }

                    is RootComponent.Child.Favourite -> {
                        FavouriteContent(component = instance.component)
                    }

                    is RootComponent.Child.Search -> {
                        SearchContent(component = instance.component)
                    }
                }
            }
        }
    }

}