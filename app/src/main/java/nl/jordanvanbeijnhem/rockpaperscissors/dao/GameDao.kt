package nl.jordanvanbeijnhem.rockpaperscissors.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import nl.jordanvanbeijnhem.rockpaperscissors.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(*) FROM game_table WHERE result = 1")
    suspend fun getNumberOfWins(): Int

    @Query("SELECT COUNT(*) FROM game_table WHERE result = 0")
    suspend fun getNumberOfDraws(): Int

    @Query("SELECT COUNT(*) FROM game_table WHERE result = -1")
    suspend fun getNumberOfLosses(): Int
}