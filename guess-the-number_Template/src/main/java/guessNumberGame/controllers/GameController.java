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


    public GameController(GameDao gameDao, RoundDao roundDao) {
       this.gameDao = gameDao;
       this.roundDao = roundDao;
    }

    @PostMapping("/begin")
    @ResponseStatus(HttpStatus.CREATED)
    public Game create() {
         //implement create gameService object and game object
        
        //add to database
       
        //getGame will hide answer before returning it to the user
        
    }


    @PostMapping("/guess")
    @ResponseStatus(HttpStatus.CREATED)
    public Round guessNumber(@RequestBody Round body) {
       //implement
    }

    @GetMapping("/game")
    public List<Game> all() {
      //implement
    }

    @GetMapping("game/{id}")
    public Game getGameById(@PathVariable int id) {
        //implement
    }

    @GetMapping("rounds/{gameId}")
    //implement
    {}

}