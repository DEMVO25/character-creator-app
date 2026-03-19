package data.local.entity

import androidx.room.TypeConverter
import com.example.character_creator_app.character_creation.equipment.InventoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromInventoryList(value: List<InventoryItem>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toInventoryList(value: String): List<InventoryItem> {
        val listType = object : TypeToken<List<InventoryItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}