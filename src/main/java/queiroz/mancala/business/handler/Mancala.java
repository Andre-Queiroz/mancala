package queiroz.mancala.business.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;
import queiroz.mancala.business.core.PlayerContext;
import queiroz.mancala.business.core.PlayerContextInterface;
import queiroz.mancala.model.convention.Constant;
import queiroz.mancala.model.convention.Winner;
import queiroz.mancala.model.entity.Pit;
import queiroz.mancala.model.entity.Receivable;
import queiroz.mancala.model.entity.Store;
import queiroz.mancala.model.game.Turn;
import queiroz.mancala.model.game.Turn.PlayerTurn;

@Service
public class Mancala implements MancalaInterface {

    private List<Receivable> fullBoard;
    private PlayerContextInterface playerA;
    private PlayerContextInterface playerB;
    private Winner winner = Winner.UNKNOWN;

    public Mancala() {
        initializeMancala();
    }

    private void initializeMancala() {
        fullBoard = Arrays.asList(
            new Pit(Constant.PLAYER_A_PIT_1),
            new Pit(Constant.PLAYER_A_PIT_2),
            new Pit(Constant.PLAYER_A_PIT_3),
            new Pit(Constant.PLAYER_A_PIT_4),
            new Pit(Constant.PLAYER_A_PIT_5),
            new Pit(Constant.PLAYER_A_PIT_6),
            new Store(Constant.PLAYER_A_STORE),
            new Pit(Constant.PLAYER_B_PIT_1),
            new Pit(Constant.PLAYER_B_PIT_2),
            new Pit(Constant.PLAYER_B_PIT_3),
            new Pit(Constant.PLAYER_B_PIT_4),
            new Pit(Constant.PLAYER_B_PIT_5),
            new Pit(Constant.PLAYER_B_PIT_6),
            new Store(Constant.PLAYER_B_STORE)
        );

        playerA = new PlayerContext(
            Arrays.asList(fullBoard.get(0), fullBoard.get(1), fullBoard.get(2),
                fullBoard.get(3), fullBoard.get(4), fullBoard.get(5), fullBoard.get(6),
                fullBoard.get(7), fullBoard.get(8), fullBoard.get(9), fullBoard.get(10),
                fullBoard.get(11), fullBoard.get(12)));

        playerB = new PlayerContext(
            Arrays.asList(fullBoard.get(7), fullBoard.get(8), fullBoard.get(9),
                fullBoard.get(10), fullBoard.get(11), fullBoard.get(12), fullBoard.get(13),
                fullBoard.get(0), fullBoard.get(1), fullBoard.get(2), fullBoard.get(3),
                fullBoard.get(4), fullBoard.get(5)));
    }

    @Override
    public void executeTurn(String pitId) {
        if (winner != Winner.UNKNOWN) {
            return;
        }
        PlayerContextInterface playerContext = getContext();

        try {
            if (playerContext.isStore(pitId)) {
                throw new RuntimeException("Cannot select a store to be sowed");
            }
            if (playerContext.doesNotOwnThisPit(pitId)) {
                throw new RuntimeException("Invalid pit to sow");
            }

            playerContext.sow(pitId);

            int lastIndexSown = playerContext.getLastIndexSown();
            playerContext.checkCapture(lastIndexSown);

            if (gameEnded()) {
                winner = playerContext.getWinner(playerA.getStore().getStones(),
                    playerB.getStore().getStones());
                return;
            }

            if (!playerContext.hasAdditionalMove(lastIndexSown)) {
                Turn.changeTurn();
            }

            if (gameEnded()) {
                winner = playerContext.getWinner(playerA.getStore().getStones(),
                    playerB.getStore().getStones());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public List<Receivable> getFullBoard() {
        return fullBoard;
    }

    @Override
    public Winner getWinner() {
        return winner;
    }

    @Override
    public List<Store> getBothStores() {
        return new ArrayList<>(Arrays.asList(playerA.getStore(), playerB.getStore()));
    }

    @Override
    public void resetMancala() {
        winner = Winner.UNKNOWN;
        if (Turn.getTurn() == PlayerTurn.PLAYER_B) {
            Turn.changeTurn();
        }
        initializeMancala();
    }

    private boolean gameEnded() {
        if (getContext().playerStillHaveStones()) {
            return false;
        }

        if (Turn.getTurn() == PlayerTurn.PLAYER_A) {
            playerB.collectAllStonesToStore();
        } else {
            playerA.collectAllStonesToStore();
        }
        return true;
    }

    private PlayerContextInterface getContext() {
        if (Turn.getTurn() == PlayerTurn.PLAYER_A) {
            return playerA;
        }
        return playerB;
    }

}
