package com.stockprice.presentation.companyinfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.stockprice.R
import com.stockprice.presentation.companyinfo.CompanyInfoViewModel
import com.stockprice.ui.theme.DarkBlue

@Composable
@Destination
fun CompanyInfoScreen(
    symbol: String,
    viewModel: CompanyInfoViewModel = hiltViewModel()
) {

    val state = viewModel.state

    if (state.error == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(16.dp)
        ) {
            state.companyInfo?.let { companyInfo ->

                // Name
                Text(
                    text = companyInfo.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Symbol
                Text(
                    text = companyInfo.symbol ?: "",
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                // Industry
                Text(
                    text = stringResource(R.string.info_industry) + (companyInfo.industry ?: ""),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Country
                Text(
                    text = stringResource(R.string.info_contry) + (companyInfo.industry ?: ""),
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    fontStyle = FontStyle.Italic,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Divider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))

                // Description
                Text(
                    text = companyInfo.description ?: "",
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                if (state.stockInfo.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(R.string.market_summary))
                    Spacer(modifier = Modifier.height(24.dp))
                    StockChart(
                        infos = state.stockInfo,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .align(CenterHorizontally)
                    )
                }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Text(text = state.error, color = MaterialTheme.colors.error)
            }
        }
    }
}
