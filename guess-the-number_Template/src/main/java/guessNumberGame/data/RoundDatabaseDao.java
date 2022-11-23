package guessNumberGame.data;

import guessNumberGame.models.Round;

import java.sql.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;


@Repository
public class RoundDatabaseDao implements  RoundDao{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoundDatabaseDao(JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    @Override
    public Round add(Round round) {

        final String sql = "INSERT INTO Round(game_id, guess_time, guess, result)" +
                " VALUES(?,?,?,?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update((Connection conn) -> {

            PreparedStatement statement = conn.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, round.getGameId());
            statement.setTimestamp(2, round.getTimeStamp());
            statement.setString(3, round.getGuess());
            statement.setString(4, round.getGuessResult());
            return statement;

        }, keyHolder);

        round.setId(keyHolder.getKey().intValue());

        return round;
    }

    @Override
    public List<Round> getAll() {
        final String sql = "SELECT round_id, game_id, guess_time, guess, result FROM round ORDER BY round_id;";
        return jdbcTemplate.query(sql, new RoundMapper());
    }

    @Override
    public List<Round> getAllOfGame(int game_id) {
      final String sql = "SELECT round_id, game_id, guess_time, guess, result FROM round WHERE game_id = ? ORDER BY round_id;";
      return jdbcTemplate.query(sql, new RoundMapper(), game_id);
    }

    @Override
    public Round findById(int round_id) {
      final String sql = "SELECT round_id, game_id, guess_time, guess, result " + "FROM round WHERE round_id = ?;";
      return jdbcTemplate.queryForObject(sql, new RoundMapper(), round_id);
    }

    @Override public boolean update(Round round) {
        //create a sql string to update the items in the round table
        //don't include round_id in update statement because we don't need to update it
        final String sql = "UPDATE round SET "
                + "game_id = ?, "
                + "guess_time = ?, "
                + "guess = ?, "
                + "result = ? "
                + "WHERE round_id = ?;";
        return jdbcTemplate.update(sql, round.getGameId(), round.getTimeStamp(), round.getGuess(), round.getGuessResult(), round.getId()) > 0;
    }

    @Override
    public boolean deleteById(int round_id) {
      final String sql = "DELETE FROM round WHERE round_id = ?;";
      return jdbcTemplate.update(sql, round_id) > 0;
    }

    private static final class RoundMapper implements RowMapper<Round> {
        @Override
        public Round mapRow(ResultSet rs, int index) throws SQLException {
            Round round = new Round();
            round.setId(rs.getInt("round_id"));
            round.setGameId(rs.getInt("game_id"));
            round.setTimeStamp(rs.getTimestamp("guess_time"));
            round.setGuess(rs.getString("guess"));
            round.setGuessResult(rs.getString("result"));
            return round;
        }
    }
}
