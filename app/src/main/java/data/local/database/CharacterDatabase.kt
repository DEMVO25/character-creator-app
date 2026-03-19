package data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import data.local.dao.CharacterDao
import data.local.entity.CharacterDto
import data.local.entity.Converters

@Database(
    entities = [CharacterDto::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract  val dao: CharacterDao
}
