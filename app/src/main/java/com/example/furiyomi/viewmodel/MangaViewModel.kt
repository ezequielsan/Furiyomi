package com.example.furiyomi.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.furiyomi.model.Manga
import com.example.furiyomi.model.MangaFilters
import com.example.furiyomi.model.Tag
import com.example.furiyomi.service.MangaDexApiPagingSource
import com.example.furiyomi.service.RetrofitClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class MangaViewModel: ViewModel() {
    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> get() = _tags

    private val _demographic = MutableStateFlow<List<String>?>(emptyList())
    val demographic: StateFlow<List<String>?> get() = _demographic

    private val _contentRating = MutableStateFlow<List<String>?>(emptyList())
    val contentRating: StateFlow<List<String>?> get() = _contentRating

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _mangasById = MutableStateFlow<List<Manga>>(emptyList())
    val mangasById: StateFlow<List<Manga>> get() = _mangasById

    private val _mangaById = MutableStateFlow<List<Manga>>(emptyList())
    val mangaById: StateFlow<List<Manga>> get() = _mangaById

    private val _status = MutableStateFlow<List<String>?>(emptyList())
    val status: StateFlow<List<String>?> get() = _status

    fun updateStatus(status: List<String>?) {
        _status.value = status
        Log.d("ViewModel", _status.value.toString())
    }

    init {
        fetchTags()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val mangasPagedListFlow = combine(
        title,
        demographic,
        contentRating,
        status
    ) { query, demo, contRat, stat ->
        MangaFilters(query, demo, contRat, stat)
    }.flatMapLatest { filters ->
        Pager(
            PagingConfig(pageSize = MangaDexApiPagingSource.PAGE_SIZE)
        ) {
            MangaDexApiPagingSource(
                title = filters.title,
                mangadexApi = RetrofitClient.api,
                demographic = filters.demographic,
                contentRating = filters.contentRating,
                status = filters.status,
                orderFollows = "desc"
            )
        }.flow.cachedIn(viewModelScope)
    }


    // Função única para atualizar todos os filtros
    fun updateFilters(
        title: String? = null,
        demographic: List<String>? = null,
        contentRating: List<String>? = null,
        status: List<String>? = null
    ) {
        // Atualiza todos os filtros de uma vez, se não for nulo
        title?.let { _title.value = it }
        demographic?.let { _demographic.value = it }
        contentRating?.let { _contentRating.value = it }
        status?.let { _status.value = it }

        // Aqui você pode adicionar qualquer lógica adicional que você queira para atualizar filtros.
        Log.d("ViewModel", "Filtros atualizados: Title: $title, Demographic: $demographic, ContentRating: $contentRating")
    }


    fun fetchMangasById(ids: List<String>) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getManga(ids = ids)
                _mangasById.value = response.data.ifEmpty { emptyList() }
                Log.d("Buscando manga por ID", "${ mangasById.value}")
            }
            catch (e: Exception) {
                e.printStackTrace()
                _mangasById.value = emptyList()
            }
        }
    }

    fun fetchMangaById(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getManga(ids = listOf(id))
                _mangaById.value = response.data.ifEmpty { emptyList() }
                Log.d("Buscando manga por ID", "${ mangasById.value}")
            }
            catch (e: Exception) {
                e.printStackTrace()
                _mangaById.value = emptyList()
            }
        }
    }

    private fun fetchTags() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getTags()
                _tags.value = response.tags
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}