package com.sanghm2.newapp.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.sanghm2.newapp.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String{
        return source.name
    }
    @TypeConverter
    fun toSource(name : String): Source{
        return Source(name, name)
    }
}