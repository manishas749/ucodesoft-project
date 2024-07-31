package com.example.paginglibrary.db

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.paginglibrary.retrofit.QuoteAPI
import com.example.paginglibrary.models.Product
import com.example.paginglibrary.models.QuoteRemoteKeys

@OptIn(ExperimentalPagingApi::class)
class QuoteRemoteMediator(
    private val quoteAPI: QuoteAPI,
    private val quoteDataBase: QuoteDataBase
) : RemoteMediator<Int, Product>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Product>
    ): MediatorResult {
        //fetch Quotes from API
        //saves these quotes + RemoteKeys Data into DB
        //Logic for states -REFRESH,PREPEND,APPEND

        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1

                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    val nextPage = remoteKeys?.nextPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextPage
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysForFirstItem(state)
                    val prevPage = remoteKeys?.prevPage ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    prevPage

                }
            }
            val response = quoteAPI.getQuotes(currentPage).products
            val endOfPaginationReached = response.size < state.config.pageSize

            quoteDataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    quoteDataBase.remoteKeysDao().deleteAllRemoteKeys()
                    quoteDataBase.quoteDao().deleteQuote()
                }
                val prevPage = if (currentPage == 1) null else currentPage - 1
                val nextPage = if (endOfPaginationReached) null else currentPage + 1

                val keys = response.map { product ->
                    QuoteRemoteKeys(
                        id = product.id,
                        prevPage = prevPage,
                        nextPage = nextPage
                    )

                }
                quoteDataBase.remoteKeysDao().addAllRemoteKeys(keys)
                quoteDataBase.quoteDao().addQuotes(response)
            }
            MediatorResult.Success(endOfPaginationReached)

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }


    }


    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Product>): QuoteRemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                quoteDataBase.withTransaction { quoteDataBase.remoteKeysDao().getRemoteKeys(id) }
            }
        }
    }


    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, Product>): QuoteRemoteKeys? {
        return state.pages.firstOrNull() { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { product ->
                quoteDataBase.withTransaction {
                    quoteDataBase.remoteKeysDao().getRemoteKeys(id = product.id)

                }
            }

    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, Product>): QuoteRemoteKeys? {
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { product ->
                quoteDataBase.withTransaction {
                    quoteDataBase.remoteKeysDao().getRemoteKeys(id = product.id)

                }
            }

    }

}
