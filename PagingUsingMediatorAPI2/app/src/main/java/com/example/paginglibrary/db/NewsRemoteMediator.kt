package com.example.paginglibrary.db

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.paginglibrary.Constants.Companion.API
import com.example.paginglibrary.models.Article
import com.example.paginglibrary.models.NewsResponse
import com.example.paginglibrary.models.QuoteRemoteKeys
import com.example.paginglibrary.models.Source
import com.example.paginglibrary.retrofit.QuoteAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.InvalidObjectException
import javax.security.auth.callback.Callback

@ExperimentalPagingApi
class NewsRemoteMediator(
    private val newsDao: QuoteDao,
    private val newsInterface: QuoteAPI,
    private val initialPage: Int = 1
) : RemoteMediator<Int, Article>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {

        var response:List<Article> = listOf()

        return try {

            // Judging the page number
            val page = when (loadType) {
                LoadType.APPEND -> {

                    val remoteKey =
                        getLastRemoteKey(state) ?: throw InvalidObjectException("Inafjklg")
                    remoteKey.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)

                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.REFRESH -> {
                    val remoteKey = getClosestRemoteKeys(state)
                    remoteKey?.nextPage?.minus(1) ?: initialPage
                }
            }

            // make network request
           val call =  newsInterface.getAllSportsNews(
                 "in",
                 "sports",
                 API,
                 page,
                 state.config.pageSize
             )
            Log.d("listkjfkjkfj","Fail")

            call.enqueue(object : retrofit2.Callback<NewsResponse>{
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (response.isSuccessful)
                    {
                        val list = response.body()!!.articles
                        Log.d("listkjfkjkfj",list.toString())
                        MainScope().launch {
                            newsDao.insertArticles(list)

                        }
                    }
                    else
                    {
                        Log.d("listkjfkjkfj","Fail")

                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    Log.d("listkjfkjkfj","Fail")

                }

            })

            val endOfPagination = response.size!! < state.config.pageSize

            if (loadType == LoadType.REFRESH) {
                        newsDao.deleteAllArticles()
                        newsDao.deleteAllRemoteKeys()
                    }


                    val prev = if (page == initialPage) null else page - 1
                    val next = if (endOfPagination) null else page + 1


                    val list = response.map {
                        QuoteRemoteKeys(it.title, prev, next)
                    }


                    // make list of remote keys

                    if (list != null) {
                        newsDao.insertAllRemoteKeys(list)
                    }

                    // insert to the room






            MediatorResult.Success(endOfPagination)



        }
        catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }

    private suspend fun getClosestRemoteKeys(state: PagingState<Int, Article>): QuoteRemoteKeys? {

        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                newsDao.getAllREmoteKey(it.title)
            }
        }

    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Article>): QuoteRemoteKeys? {
        return state.lastItemOrNull()?.let {
            newsDao.getAllREmoteKey(it.title)
        }
    }

}