package guessNumberGame.Service;

import guessNumberGame.TestApplicationConfiguration;
import guessNumberGame.data.GameDao;
import guessNumberGame.data.RoundDao;
import guessNumberGame.models.Game;
import guessNumberGame.models.Round;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class GameServiceTest extends TestCase {

    @Autowired
    RoundDao roundDao;

    @Autowired
    GameDao gameDao;

    @Before
    public void setUp() {
        List<Round> rounds = roundDao.getAll();
        for(Round round : rounds) {
            roundDao.deleteById(round.getId());
        }

        List<Game> games = gameDao.getAll();
        for(Game game : games) {
            gameDao.deleteById(game.getGameId());
        }
    }

    @Test
    public void testGetGames() {
        //create a new game
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());
        roundDao.add(round);

        Game endGame = gameService.getGames(game);
        assertFalse(endGame.getIsFinished());

        game.setIsFinished(true);
        gameDao.update(game);
        assertTrue(endGame.getIsFinished());
    }

    @Test
    public void testGetAllGames() {

        GameService gameService = new GameService();
        Game game1 = gameService.newGame();
        gameDao.add(game1);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game1.getGameId());
        roundDao.add(round);

        Game game2 = gameService.newGame();
        gameDao.add(game2);

        Round round2 = new Round();
        round2.setGuess("2222");
        round2.setGameId(game2.getGameId());
        roundDao.add(round2);

        //call get all games method
        List<Game> gettingGames = gameService.getAllGames(gameDao.getAll());

        //put in assert statements
        assertEquals(2, gettingGames.size());
        assertFalse(gettingGames.isEmpty());

    }

    @Test
    public void testGuessNumber() {
        //create a new game
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());
        roundDao.add(round);

        //get the game id
        Game fromDao = gameDao.findById(game.getGameId());

        //update the answer with an answer you choose
        fromDao.setAnswer("1111");
        gameDao.update(fromDao);

        Game updated = gameDao.findById(game.getGameId());

        Round guess = gameService.guessNumber(game, String.valueOf(updated), gameDao);
        //assert that check guess with updated answer is correct
        assertEquals(fromDao.getGameId(), guess.getGameId());

        //assert that check guess with random number is wrong
        assertNotEquals(game.getAnswer(), guess.getGuessResult());
    }

}