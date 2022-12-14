package guessNumberGame.data;

import junit.framework.TestCase;
import guessNumberGame.Service.GameService;
import guessNumberGame.TestApplicationConfiguration;
import guessNumberGame.models.Game;
import guessNumberGame.models.Round;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class RoundDatabaseDaoTest extends TestCase {
    @Autowired
    RoundDao roundDao;

    @Autowired
    GameDao gameDao;
    public RoundDatabaseDaoTest () {}
    
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
    public void testAdd() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGameId(game.getGameId());
        gameService.setTimeStamp(round);
        round.setGuess("1234");
        round.setGuessResult("e:2:p:1");
        roundDao.add(round);
        Round fromDao = roundDao.findById(round.getId());

        assertEquals(round.getId(), fromDao.getId());
    }

    @Test
    public void testGetAll() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game game2 = gameService.newGame();
        gameDao.add(game2);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());

        Round round2 = new Round();
        round2.setGuess("2222");
        round2.setGameId(game2.getGameId());

        roundDao.add(round);
        roundDao.add(round2);

        List<Round> rounds = roundDao.getAll();
       assertEquals(2, rounds.size());
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

        List<Round> rounds = roundDao.getAllOfGame(game.getGameId());
        assertEquals(2, rounds.size());
        assertEquals("1111", rounds.get(0).getGuess());
        assertEquals("2222", rounds.get(1).getGuess());
    }

    @Test
    public void testDeleteByIdAndFindById () {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());
        roundDao.add(round);

        Round fromDao = roundDao.findById(round.getId());
        assertEquals(round.getId(), fromDao.getId());

        roundDao.deleteById(round.getId());
    }

    @Test
    public void testUpdateRound() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Round round = new Round();
        round.setGuess("1111");
        round.setGameId(game.getGameId());
        roundDao.add(round);

        round.setGuess("2222");
        roundDao.update(round);

        Round roundUpdated = roundDao.findById(round.getId());
        assertEquals(roundUpdated.getGuess(), "2222");
    }
}
