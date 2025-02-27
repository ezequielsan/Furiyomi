package com.example.furiyomi.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.furiyomi.model.Manga
import com.example.furiyomi.service.RetrofitClient.api

class MangaDexApiPagingSource(
    private val mangadexApi: MangaApiService,
    private val title: String? = null,
    private val tags: List<String>? = null,
    private val demographic: List<String>? = null,
    private val status: List<String>? = null,
    private val contentRating: List<String>? = null,
    private val orderFollows: String? = null
): PagingSource<Int, Manga>() {
    override fun getRefreshKey(state: PagingState<Int, Manga>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(
                anchorPosition
            )
            anchorPage?.prevKey?.plus(1) ?:
            anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Manga> {
        return try {
            val page = params.key ?: 0
            val offset = page * PAGE_SIZE
            val response = api.getManga(
                title = title,
                tags = tags,
                demographic = demographic,
                status = status,
                contentRating = contentRating,
                offset = offset,
                order = orderFollows
            )
            val nextKey =
                if (offset >= response.total) null
                else page + 1
            return LoadResult.Page(
                data = response.data,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}