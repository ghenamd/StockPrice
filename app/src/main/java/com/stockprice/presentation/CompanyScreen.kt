package com.stockprice.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@RootNavGraph(start = true)
@Destination
@Composable
fun CompanyScreen(
    navigator: DestinationsNavigator,
    viewModel: CompaniesViewModel = hiltViewModel()
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = viewModel.state.isRefreshing
    )

    val state = viewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.searchQuery, onValueChange = {
                viewModel.onEvent(
                    CompanyEvent.OnSearchQueryChange(it)
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), placeholder = {
                Text(text = "Search...")
            }, maxLines = 1, singleLine = true
        )

        SwipeRefresh(state = swipeRefreshState, onRefresh = {
            viewModel.onEvent(CompanyEvent.Refresh)
        }) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.companies.size) { position ->
                    CompanyItem(
                        company = state.companies[position],
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO
                            }
                            .padding(16.dp)
                    )
                    if (position < state.companies.size) {
                        Divider(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        }
    }
}