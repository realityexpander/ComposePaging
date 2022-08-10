package com.realityexpander.composepaging

import kotlinx.coroutines.delay

class Repository {

    // Simulates the database
    private val remoteDataSource = (1..100).map {
        ListItem(
            title = "Item $it",
            description = "Description $it"
        )
    }

    // Simulate call to database or API
    suspend fun getItems(page: Int /* or key */, pageSize: Int): Result<List<ListItem>> {
        val startingIndex = page * pageSize
        delay(2000L)

        return if(startingIndex + pageSize <= remoteDataSource.size) {
            Result.success(
                remoteDataSource.slice(startingIndex until startingIndex + pageSize)
            )
        } else
            Result.success(emptyList())
    }
}