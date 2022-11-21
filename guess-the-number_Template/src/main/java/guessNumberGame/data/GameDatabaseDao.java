package guessNumberGame.data;

import guessNumberGame.models.Game;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class GameDatabaseDao implements GameDao {
     private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GameDatabaseDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public Game add(Game game) {

        final String sql = "INSERT INTO Game(answer, isFinished) VALUES(?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                sql,
                Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, game.getAnswer());
            statement.setBoolean(2, game.getIsFinished());
            return statement;

        }, keyHolder);

        game.setGameId(keyHolder.getKey().intValue());

        return game;
    }

    @Override
    public List<Game> getAll() {
       final String sql = "SELECT game_id, answer, isFinished FROM game;";
       return jdbcTemplate.query(sql, new GameMapper());
    }


    @Override
    public Game findById(int game_id) {
       final String sql = "SELECT game_id, answer, isFinished " + "FROM game WHERE game_id = ?;";
       return jdbcTemplate.queryForObject(sql, new GameMapper(), game_id);
    }

    @Override
    public boolean update(Game game) {
         final String sql = "UPDATE game SET "
                 + "answer = ?, "
                 + "isFinished = ? "
                 + "WHERE game_id = ?;";

         return jdbcTemplate.update(sql, game.getAnswer(), game.getIsFinished(), game.getGameId()) > 0;
    }

    @Override
    public boolean deleteById(int game_id) {
       final String sql = "DELETE FROM game WHERE game_id = ?;";
       return jdbcTemplate.update(sql, game_id) > 0;
    }

    private static final class GameMapper implements RowMapper<Game> {

        @Override
        public Game mapRow(ResultSet rs, int index) throws SQLException {
            Game game = new Game();
            game.setGameId(rs.getInt("game_id"));
            game.setAnswer(rs.getString("answer"));
            game.setIsFinished(rs.getBoolean("isFinished"));
            return game;
        }
    }
}
