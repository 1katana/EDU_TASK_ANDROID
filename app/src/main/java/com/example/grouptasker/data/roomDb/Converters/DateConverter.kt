package com.example.grouptasker.data.roomDb.Converters
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return value?.let {
            try {
                LocalDateTime.parse(it, formatter)
            } catch (e: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun localDateTimeToString(localDateTime: LocalDateTime?): String? {
        return localDateTime?.let { formatter.format(it) }
    }
}
