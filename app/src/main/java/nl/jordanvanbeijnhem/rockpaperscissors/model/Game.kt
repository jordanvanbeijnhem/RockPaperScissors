package nl.jordanvanbeijnhem.rockpaperscissors.model

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "game_table")
data class Game(

    @ColumnInfo(name = "player_choice")
    @DrawableRes val playerChoice: Int,

    @ColumnInfo(name = "computer_choice")
    @DrawableRes val computerChoice: Int,

    @ColumnInfo(name = "result")
    val result: Result?,

    @ColumnInfo(name = "date")
    val date: Date,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
) {

    enum class Result(val value: Int) {
        WIN(1),
        DRAW(0),
        LOSS(-1)
    }

    enum class Choice {
        ROCK,
        PAPER,
        SCISSORS
    }
}