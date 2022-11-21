package guessNumberGame.data;

import guessNumberGame.data.GameDao;
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
public class GameDatabaseDaoTest extends TestCase {
    @Autowired
    GameDao gameDao;

   @Autowired
   RoundDao roundDao;
   public GameDatabaseDaoTest() {}
 
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
    public void testAddGetGames() {
        // adds new game using dao
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game fromDao = gameDao.findById(game.getGameId());
        assertEquals(game.getGameId(), fromDao.getGameId());
    }

    @Test
    public void testGetAll() {
        GameService gameService1 = new GameService();
        Game game1 = gameService1.newGame();
        gameDao.add(game1);

        GameService gameService2 = new GameService();
        Game game2 = gameService2.newGame();
        gameDao.add(game2);

        List<Game> games = gameDao.getAll();

        assertEquals(games.size(), 2);
        assertTrue(games.contains(game1));
        assertTrue(games.contains(game2));
    }

    @Test
    public void testUpdate() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);
        game.setIsFinished(true);
        gameDao.update(game);
        Game updated = gameDao.findById(game.getGameId());
        assertTrue(updated.getIsFinished());
    }

    @Test
    public void testDeleteByIdAndFindById() {
        GameService gameService = new GameService();
        Game game = gameService.newGame();
        gameDao.add(game);

        Game fromDao = gameDao.findById(game.getGameId());
        assertEquals(game.getGameId(), fromDao.getGameId());

        gameDao.deleteById(game.getGameId());
    }

}
