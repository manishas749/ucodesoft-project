package com.example.paginglibrary.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paginglibrary.retrofit.QuoteAPI
import com.example.paginglibrary.models.Product

class QuotePagingSource(val quoteAPI: QuoteAPI): PagingSource<Int, Product>()
{
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
       return try {
            val position = params.key?:1
            val response = quoteAPI.getQuotes(position)
            return LoadResult.Page(
                data = response.products,
                prevKey = if (position ==1) null else position-1,
                nextKey = if (position == response.total) null else position+1
            )

        }catch (e:Exception) {
            LoadResult.Error(e)
        }
    }

}