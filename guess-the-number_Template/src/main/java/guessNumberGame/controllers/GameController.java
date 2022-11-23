package guessNumberGame.controllers;

import guessNumberGame.Service.GameService;
import guessNumberGame.data.GameDao;
import guessNumberGame.data.RoundDao;
import guessNumberGame.models.Game;
import java.util.List;

import guessNumberGame.models.Round;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class GameController {

    private final GameDao gameDao;
    private final RoundDao roundDao;
    private final GameService service = new GameService();


    public GameController(GameDao gameDao, RoundDao roundDao) {
       this.gameDao = gameDao;
       this.roundDao = roundDao;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create(@RequestBody Game game) {

        //add to database
        gameDao.add(game);

        //getGames will hide answer before returning it to the user
        service.getGames(game);

        return game;
    }

    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guessNumber(@RequestBody Round body) {
        return service.guessNumber(gameDao.findById(body.getGameId()), body.getGuess(), gameDao);
    }

    @GetMapping("/game")
    public List<Game> all() {
        return gameDao.getAll();
    }

    @GetMapping("game/{id}")
    public Game getGameById(@PathVariable int id) {
        return gameDao.findById(id);
    }

    @GetMapping("rounds/{gameId}")
    public List<Round> getAllOfGame(@PathVariable int gameId) {
        return roundDao.getAllOfGame(gameId);
    }
}