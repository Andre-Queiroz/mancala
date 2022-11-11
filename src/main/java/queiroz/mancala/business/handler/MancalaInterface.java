package queiroz.mancala.business.handler;

import java.util.List;
import queiroz.mancala.model.convention.Winner;
import queiroz.mancala.model.entity.Receivable;
import queiroz.mancala.model.entity.Store;

public interface MancalaInterface {

    void executeTurn(String pitId);

    List<Receivable> getFullBoard();

    Winner getWinner();

    List<Store> getBothStores();

    void resetMancala();
}
