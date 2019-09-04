package nl.jordanvanbeijnhem.rockpaperscissors.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import nl.jordanvanbeijnhem.rockpaperscissors.converter.Converters
import nl.jordanvanbeijnhem.rockpaperscissors.dao.GameDao
import nl.jordanvanbeijnhem.rockpaperscissors.model.Game

@Database(entities = [Game::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {

    abstract fun gameDao(): GameDao

    companion object {

        private const val DATABASE_NAME = "GAME_DATABASE"

        @Volatile
        private var gameDatabaseInstance: GameDatabase? = null

        fun getDatabase(context: Context): GameDatabase? {
            if (gameDatabaseInstance == null) {
                synchronized(GameDatabase::class.java) {
                    if (gameDatabaseInstance == null) {
                        gameDatabaseInstance =
                            Room.databaseBuilder(
                                context.applicationContext,
                                GameDatabase::class.java,
                                DATABASE_NAME
                            ).build()
                    }
                }
            }
            return gameDatabaseInstance
        }
    }
}