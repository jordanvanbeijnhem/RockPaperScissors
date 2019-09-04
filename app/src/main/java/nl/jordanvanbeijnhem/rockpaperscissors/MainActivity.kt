package nl.jordanvanbeijnhem.rockpaperscissors

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.jordanvanbeijnhem.rockpaperscissors.comparator.ChoiceComparator
import nl.jordanvanbeijnhem.rockpaperscissors.model.Game
import nl.jordanvanbeijnhem.rockpaperscissors.repository.GameRepository
import java.util.*

const val HISTORY_CLEAR_CODE = 100

class MainActivity : AppCompatActivity() {

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        gameRepository = GameRepository(this)
        initViews()
    }

    private fun initViews() {
        ivRock.setOnClickListener { playGame(Game.Choice.ROCK) }
        ivPaper.setOnClickListener { playGame(Game.Choice.PAPER) }
        ivScissors.setOnClickListener { playGame(Game.Choice.SCISSORS) }
        getStatisticsFromDatabase()
    }

    private fun playGame(playerChoice: Game.Choice) {
        val computerChoice = Game.Choice.values()[(Game.Choice.values().indices).random()]
        val game = Game(
            getDrawableId(playerChoice),
            getDrawableId(computerChoice),
            getResult(playerChoice, computerChoice),
            Date()
        )
        displayGame(game)
        addGameToDatabase(game)
    }

    private fun displayGame(game: Game) {
        ivComputer.setImageDrawable(getDrawable(game.computerChoice))
        ivYou.setImageDrawable(getDrawable(game.playerChoice))

        when (game.result) {
            Game.Result.LOSS -> tvResult.text = getString(R.string.you_lose)
            Game.Result.DRAW -> tvResult.text = getString(R.string.draw)
            Game.Result.WIN -> tvResult.text = getString(R.string.you_win)
        }
    }

    private fun getDrawableId(choice: Game.Choice): Int {
        return when (choice) {
            Game.Choice.ROCK -> R.drawable.rock
            Game.Choice.PAPER -> R.drawable.paper
            Game.Choice.SCISSORS -> R.drawable.scissors
        }
    }

    private fun getResult(playerChoice: Game.Choice, computerChoice: Game.Choice): Game.Result? {
        return Game.Result.values().associateBy(Game.Result::value)[ChoiceComparator().compare(
            playerChoice,
            computerChoice
        )]
    }

    private fun addGameToDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            getStatisticsFromDatabase()
        }
    }

    private fun getStatisticsFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            var wins = 0
            var draws = 0
            var losses = 0
            withContext(Dispatchers.IO) {
                wins = gameRepository.getNumberOfWins()
                draws = gameRepository.getNumberOfDraws()
                losses = gameRepository.getNumberOfLosses()
            }
            tvStatistics.text = getString(R.string.statistics, wins, draws, losses)
        }
    }

    private fun startHistoryActivity() {
        startActivityForResult(Intent(this, HistoryActivity::class.java), HISTORY_CLEAR_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                HISTORY_CLEAR_CODE -> {
                    getStatisticsFromDatabase()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_history -> {
                startHistoryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
