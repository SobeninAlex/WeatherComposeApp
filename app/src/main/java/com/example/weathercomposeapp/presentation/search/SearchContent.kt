package com.example.weathercomposeapp.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weathercomposeapp.R
import com.example.weathercomposeapp.domain.entity.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    component: SearchComponent,
) {

    val state by component.model.collectAsState()

    val focusRequest = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = Unit) {
        focusRequest.requestFocus()
    }

    SearchBar(
        modifier = modifier
            .focusRequester(focusRequest),
        query = state.searchQuery,
        onQueryChange = {
            component.changeSearchQuery(it)
        },
        onSearch = {
            component.onClickSearch()
        },
        active = true,
        onActiveChange = {},
        leadingIcon = {
            IconButton(onClick = { component.onClickBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { component.onClickSearch() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        placeholder = {
            Text(
                text = stringResource(R.string.enter_name_city),
            )
        }
    ) {
        when (val searchState = state.searchState) {
            is SearchStore.State.SearchState.EmptyResult -> {
                Text(
                    text = stringResource(R.string.nothing_search),
                    modifier = modifier.padding(8.dp)
                )
            }

            is SearchStore.State.SearchState.Error -> {
                Text(
                    text = stringResource(R.string.error_serach),
                    modifier = modifier.padding(8.dp)
                )
            }

            is SearchStore.State.SearchState.Initial -> {

            }

            is SearchStore.State.SearchState.Loading -> {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {
                SearchSuccess(
                    cities = searchState.cities,
                    onCityClick = {
                        component.onClickCity(it)
                    }
                )
            }
        }
    }

}

@Composable
private fun SearchSuccess(
    modifier: Modifier = Modifier,
    cities: List<City>,
    onCityClick: (City) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp),
    ) {
        items(
            items = cities,
            key = { it.id }
        ) { city ->
            CityCard(
                city = city,
                onCityClick = {
                    onCityClick(city)
                }
            )
        }
    }

}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: City,
    onCityClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCityClick() },
    ) {
        Column(
            modifier = modifier.padding(
                horizontal = 20.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = city.city
            )
            Text(text = city.country)
        }
    }
}