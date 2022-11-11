package queiroz.mancala.business.core;

import queiroz.mancala.model.convention.Winner;
import queiroz.mancala.model.entity.Store;

public interface PlayerContextInterface {

    void sow(String pitId);

    int getLastIndexSown();

    void checkCapture(int lastIndexSown);

    boolean hasAdditionalMove(int lastIndexSown);

    boolean doesNotOwnThisPit(String chosenPit);

    boolean playerStillHaveStones();

    void collectAllStonesToStore();

    boolean isStore(String pitId);

    Store getStore();

    Winner getWinner(int playerAStones, int playerBStones);
}
