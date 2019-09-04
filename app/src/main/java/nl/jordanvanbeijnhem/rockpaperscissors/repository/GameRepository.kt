package nl.jordanvanbeijnhem.rockpaperscissors.repository

import android.content.Context
import nl.jordanvanbeijnhem.rockpaperscissors.dao.GameDao
import nl.jordanvanbeijnhem.rockpaperscissors.database.GameDatabase
import nl.jordanvanbeijnhem.rockpaperscissors.model.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> = gameDao.getAllGames()

    suspend fun insertGame(game: Game) = gameDao.insertGame(game)

    suspend fun deleteAllGames() = gameDao.deleteAllGames()

    suspend fun getNumberOfWins() = gameDao.getNumberOfWins()

    suspend fun getNumberOfDraws() = gameDao.getNumberOfDraws()

    suspend fun getNumberOfLosses() = gameDao.getNumberOfLosses()
}