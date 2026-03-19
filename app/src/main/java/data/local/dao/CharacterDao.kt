package data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import data.local.entity.CharacterDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {


    @Update
    suspend fun update(character: CharacterDto)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertOrUpdate(character: CharacterDto)

    @Upsert
    suspend fun upsertCharacter(character: CharacterDto): Long

    @Delete
    suspend fun deleteCharacter(character: CharacterDto)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<CharacterDto>>


    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: Int): Flow<CharacterDto?>

}