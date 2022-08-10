package com.realityexpander.composepaging

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}