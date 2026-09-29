package com.wishINC.bdayboard.data

import kotlinx.coroutines.flow.Flow

class BirthdayRepository(private val dao: BirthdayDao) {
    val allBirthdays: Flow<List<Birthday>> = dao.getAllBirthdays()

    suspend fun insert(birthday: Birthday) = dao.insert(birthday)

    suspend fun update(birthday: Birthday) = dao.update(birthday)

    suspend fun delete(birthday: Birthday) = dao.delete(birthday)

    suspend fun deleteById(id: Long) = dao.deleteById(id)
}
