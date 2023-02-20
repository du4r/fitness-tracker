package com.example.fitnesstracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitnesstracker.models.CalcModel

@Dao
interface CalcDao {

    @Insert
    fun insert(calc: CalcModel)

    @Query("SELECT * FROM CalcModel WHERE type = :type")
    fun getRegisterByType(type: String): List<CalcModel>

}