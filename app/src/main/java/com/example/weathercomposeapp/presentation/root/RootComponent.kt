package com.example.weathercomposeapp.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weathercomposeapp.presentation.details.DetailsComponent
import com.example.weathercomposeapp.presentation.favourite.FavouriteComponent
import com.example.weathercomposeapp.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {

        data class Favourite(val component: FavouriteComponent) : Child

        data class Details(val component: DetailsComponent) : Child

        data class Search(val component: SearchComponent) : Child

    }

}