package com.realityexpander.composepaging

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.composepaging.ui.theme.ComposePagingYTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposePagingYTTheme {
                val viewModel = viewModel<MainViewModel>()
                val state = viewModel.state

                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.items.size) { i ->
                        val item = state.items[i]

                        // Kick off the loading (why not in a launched effect?)
                        if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                            // yes its a side effect but its fine in this case because of the checks above
                            viewModel.loadNextItems()
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(item.description)
                        }
                    }

                    // Show loading indicator or error
                    item {
                        if (state.isLoading) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        state.errorMessage?.let { errorMessage ->
                            Text(
                                text = "Error: ${errorMessage}",
                                fontSize = 20.sp,
                                color = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}