package guessNumberGame.controllers;

import guessNumberGame.Service.GameService;
import guessNumberGame.data.GameDao;
import guessNumberGame.data.RoundDao;
import guessNumberGame.models.Round;
import junit.framework.TestCase;
import guessNumberGame.TestApplicationConfiguration;
import guessNumberGame.models.Game;
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
public class GameControllerTest extends TestCase {

    @Autowired
    RoundDao roundDao;

    @Autowired
    GameDao gameDao;

    @Autowired
    GameController controller;

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
    public void testCreate() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game endGame = gameService.getGames(game);

        Game newGame = controller.create(game);

        assertEquals(newGame.getGameId(), endGame.getGameId());
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
        //don't add the round because it is being pulled in from the database

        //get the game id
        Game fromDao = gameDao.findById(game.getGameId());

        Round guess = controller.guessNumber(round);

        assertEquals(fromDao.getGameId(), guess.getGameId());
        assertNotEquals(game.getAnswer(), guess.getGuessResult());

    }

    @Test
    public void testAll() {
        GameService gameService1 = new GameService();
        Game game1 = gameService1.newGame();
        gameDao.add(game1);

        GameService gameService2 = new GameService();
        Game game2 = gameService2.newGame();
        gameDao.add(game2);

        List<Game> allGames = controller.all();

        assertEquals(allGames.size(), 2);
        assertTrue(allGames.contains(game1));
        assertTrue(allGames.contains(game2));

    }

    @Test
    public void testGetGameById() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game getGame = controller.getGameById(game.getGameId());
        assertEquals(game.getGameId(), getGame.getGameId());
    }

    @Test
    public void testGetAllOfGame() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());
        roundDao.add(round);

        Round round2 = new Round();
        round2.setGuess("2222");
        round2.setGameId(game.getGameId());
        roundDao.add(round2);

        List<Round> getRounds = controller.getAllOfGame(game.getGameId());
        assertEquals(2, getRounds.size());
        assertEquals("1111", getRounds.get(0).getGuess());
        assertEquals("2222", getRounds.get(1).getGuess());

    }
}