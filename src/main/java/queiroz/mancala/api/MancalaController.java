package queiroz.mancala.api;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import queiroz.mancala.business.handler.MancalaInterface;
import queiroz.mancala.model.convention.Winner;
import queiroz.mancala.model.entity.Receivable;
import queiroz.mancala.model.entity.Store;
import queiroz.mancala.model.game.Turn;
import queiroz.mancala.model.game.Turn.PlayerTurn;

@RestController
@RequestMapping("/mancala")
public class MancalaController {

    @Autowired
    private MancalaInterface mancala;

    @PostMapping("/{pitId}")
    public List<Receivable> sow(@PathVariable("pitId") String pitId) {
        mancala.executeTurn(pitId);
        return mancala.getFullBoard();
    }

    @PostMapping("/reset")
    public void resetGame() {
        mancala.resetMancala();
    }

    @GetMapping("/board")
    public List<Receivable> getFullBoard() {
        return mancala.getFullBoard();
    }

    @GetMapping("/turn")
    public PlayerTurn getTurn() {
        return Turn.getTurn();
    }

    @GetMapping("/winner")
    public Winner getWinner() {
        return mancala.getWinner();
    }

    @GetMapping("/stores")
    public List<Store> getBothStores() {
        return mancala.getBothStores();
    }

}
