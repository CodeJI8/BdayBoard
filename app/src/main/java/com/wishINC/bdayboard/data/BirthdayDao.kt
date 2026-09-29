package com.wishINC.bdayboard.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BirthdayDao {
    @Query("SELECT * FROM birthdays")
    fun getAllBirthdays(): Flow<List<Birthday>>

    @Insert
    suspend fun insert(birthday: Birthday)

    @Update
    suspend fun update(birthday: Birthday)

    @Delete
    suspend fun delete(birthday: Birthday)

    @Query("DELETE FROM birthdays WHERE id = :id")
    suspend fun deleteById(id: Long)
}
