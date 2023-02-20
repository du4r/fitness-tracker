package com.example.fitnesstracker.utils

import androidx.room.TypeConverter
import java.util.*

public class DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if(dateLong != null) Date(dateLong) else null
    }

    @TypeConverter
    fun fromDate(date: Date?): Long?{
        return date?.time
    }

}