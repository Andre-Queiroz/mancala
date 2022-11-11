package queiroz.mancala.model.game;

import static queiroz.mancala.model.game.Turn.PlayerTurn.PLAYER_A;
import static queiroz.mancala.model.game.Turn.PlayerTurn.PLAYER_B;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Turn {

    private static PlayerTurn turn = PLAYER_A;

    public enum PlayerTurn {
        PLAYER_A,
        PLAYER_B
    }

    public static PlayerTurn getTurn() {
        return turn;
    }

    public static void changeTurn() {
        if (turn == PLAYER_A) {
            turn = PLAYER_B;
        } else {
            turn = PLAYER_A;
        }
    }

}
