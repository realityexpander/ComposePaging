package com.realityexpander.composepaging

class PaginatorImpl<Key, Item>(
    private val initialKey: Key,
    private inline val onIsLoading: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isRequestInProgress = false

    override suspend fun loadNextItems() {
        // Return if there is already a request in progress
        if(isRequestInProgress) {
            return
        }

        isRequestInProgress = true
        onIsLoading(true)

        // Call the api/database
        val result = onRequest(currentKey)
        isRequestInProgress = false
        val items = result.getOrElse {
            onError(it)
            onIsLoading(false)
            return
        }

        // Get the key for the next page
        currentKey = getNextKey(items)

        // Success
        onSuccess(items, currentKey)
        onIsLoading(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}