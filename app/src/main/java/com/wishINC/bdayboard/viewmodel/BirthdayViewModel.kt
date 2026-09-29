package com.wishINC.bdayboard.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wishINC.bdayboard.data.Birthday
import com.wishINC.bdayboard.data.BirthdayDatabase
import com.wishINC.bdayboard.data.BirthdayRepository
import com.wishINC.bdayboard.util.BirthdayDisplayInfo
import com.wishINC.bdayboard.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class BirthdayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BirthdayRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _today = MutableStateFlow<LocalDate>(LocalDate.now())

    val displayList: StateFlow<List<BirthdayDisplayInfo>>

    init {
        val dao = BirthdayDatabase.getDatabase(application).birthdayDao()
        repository = BirthdayRepository(dao)

        displayList = combine(
            repository.allBirthdays,
            _searchQuery,
            _today
        ) { birthdays, query, today ->
            val filtered = if (query.isBlank()) {
                birthdays
            } else {
                birthdays.filter { it.name.contains(query, ignoreCase = true) }
            }
            DateUtils.sortByUpcoming(filtered, today)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Call when the app resumes to refresh day calculations. */
    fun refreshToday() {
        _today.value = LocalDate.now()
    }

    fun addBirthday(name: String, day: Int, month: Int, year: Int?) {
        viewModelScope.launch {
            repository.insert(Birthday(name = name, day = day, month = month, year = year))
        }
    }

    fun updateBirthday(birthday: Birthday) {
        viewModelScope.launch {
            repository.update(birthday)
        }
    }

    fun deleteBirthday(birthday: Birthday) {
        viewModelScope.launch {
            repository.delete(birthday)
        }
    }
}
