package queiroz.mancala.business.core;

import static java.util.Map.entry;
import static queiroz.mancala.model.convention.Constant.BOARD_EDGE_INDEX;
import static queiroz.mancala.model.convention.Constant.BOARD_START_INDEX;
import static queiroz.mancala.model.convention.Constant.STORE_INDEX;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import queiroz.mancala.model.convention.Constant;
import queiroz.mancala.model.convention.Winner;
import queiroz.mancala.model.entity.Pit;
import queiroz.mancala.model.entity.Receivable;
import queiroz.mancala.model.entity.Store;
import queiroz.mancala.model.game.Turn;
import queiroz.mancala.model.game.Turn.PlayerTurn;

public class PlayerContext implements PlayerContextInterface {

    private final List<Receivable> playerBoard;

    private int lastIndexSown;

    private final Map<String, String> opponentPits = new HashMap<>(Map.ofEntries(
        entry("PLAYER_A_PIT_1", "PLAYER_B_PIT_6"),
        entry("PLAYER_A_PIT_2", "PLAYER_B_PIT_5"),
        entry("PLAYER_A_PIT_3", "PLAYER_B_PIT_4"),
        entry("PLAYER_A_PIT_4", "PLAYER_B_PIT_3"),
        entry("PLAYER_A_PIT_5", "PLAYER_B_PIT_2"),
        entry("PLAYER_A_PIT_6", "PLAYER_B_PIT_1"),
        entry("PLAYER_B_PIT_1", "PLAYER_A_PIT_6"),
        entry("PLAYER_B_PIT_2", "PLAYER_A_PIT_5"),
        entry("PLAYER_B_PIT_3", "PLAYER_A_PIT_4"),
        entry("PLAYER_B_PIT_4", "PLAYER_A_PIT_3"),
        entry("PLAYER_B_PIT_5", "PLAYER_A_PIT_2"),
        entry("PLAYER_B_PIT_6", "PLAYER_A_PIT_1")
    ));

    @Autowired
    public PlayerContext(List<Receivable> board) {
        playerBoard = board;
    }

    @Override
    public void sow(String pitId) {
        int pitIndex = getDigIndexById(pitId);

        if (pitIndex == -1) {
            throw new RuntimeException("Invalid pit.");
        }
        Pit pit = (Pit) playerBoard.get(pitIndex);

        if (pit.isEmpty()) {
            throw new RuntimeException("This pit does not contain any seeds to sow.");
        }
        int seedsToSow = pit.getStones();
        pit.collectStones();

        while (seedsToSow > 0) {
            pitIndex = getNextDigIndex(pitIndex);
            playerBoard.get(pitIndex).addStone();
            seedsToSow--;
        }
        this.lastIndexSown = pitIndex;
    }

    @Override
    public int getLastIndexSown() {
        return lastIndexSown;
    }

    @Override
    public boolean hasAdditionalMove(int lastIndexSown) {
        String lastPitId = playerBoard.get(lastIndexSown).getId();

        return lastPitId.equals(Constant.PLAYER_A_STORE) ||
            lastPitId.equals(Constant.PLAYER_B_STORE);
    }

    @Override
    public void checkCapture(int lastIndexSown) {
        int seedsInLastPit = playerBoard.get(lastIndexSown).getStones();
        String lastPitId = playerBoard.get(lastIndexSown).getId();

        if (isStore(lastPitId) || doesNotOwnThisPit(lastPitId)) {
            return;
        }

        if (seedsInLastPit == 1) {
            captureOpponentStones(lastPitId);
        }
    }

    @Override
    public boolean doesNotOwnThisPit(String chosenPit) {
        if (PlayerTurn.PLAYER_A == Turn.getTurn()) {
            Pattern pattern = Pattern.compile(Constant.PLAYER_A_PATTERN);
            Matcher correctPattern = pattern.matcher(chosenPit);
            return !correctPattern.matches();
        }
        Pattern pattern = Pattern.compile(Constant.PLAYER_B_PATTERN);
        Matcher correctPattern = pattern.matcher(chosenPit);
        return !correctPattern.matches();
    }

    @Override
    public boolean isStore(String pitId) {
        return pitId.equals("FARMER_A_STORE") || pitId.equals("FARMER_B_STORE");
    }

    @Override
    public boolean playerStillHaveStones() {
        int count = 0;
        int i = 0;

        while (count == 0 && i < STORE_INDEX) {
            count = playerBoard.get(i).getStones();
            i++;
        }
        return count != 0;
    }

    @Override
    public void collectAllStonesToStore() {
        int stones = 0;
        Pit pit;
        for (int i = BOARD_START_INDEX; i < STORE_INDEX; i++) {
            stones += playerBoard.get(i).getStones();
            pit = (Pit) playerBoard.get(i);
            pit.collectStones();
        }
        getStore().add(stones);
    }

    @Override
    public Store getStore() {
        return (Store) playerBoard.get(STORE_INDEX);
    }

    @Override
    public Winner getWinner(int playerAStones, int playerBStones) {
        if (playerAStones == playerBStones) {
            return Winner.DRAW;
        }
        return playerAStones > playerBStones ? Winner.PLAYER_A : Winner.PLAYER_B;
    }

    private void captureOpponentStones(String lastPitId) {
        Pit lastPitSown = getPitById(lastPitId);
        int lastPitSeed = lastPitSown.getStones();

        Pit opponentPit = getPitById(opponentPits.get(lastPitId));

        if (!opponentPit.isEmpty()) {
            int opponentStones = opponentPit.getStones();
            opponentPit.collectStones();
            lastPitSown.collectStones();

            Store store = getStore();
            store.add(opponentStones + lastPitSeed);
        }
    }

    private Pit getPitById(String id) {
        int pitIndex = getDigIndexById(id);
        if (pitIndex != -1) {
            return (Pit) playerBoard.get(pitIndex);
        }
        throw new RuntimeException("Pit not found.");
    }

    private int getDigIndexById(String id) {
        for (int i = BOARD_START_INDEX; i < playerBoard.size(); i++) {
            if (playerBoard.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private int getNextDigIndex(int pitIndex) {
        if (pitIndex == BOARD_EDGE_INDEX) {
            return BOARD_START_INDEX;
        }
        return pitIndex + 1;
    }

}
