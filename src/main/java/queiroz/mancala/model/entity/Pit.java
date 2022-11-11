package queiroz.mancala.model.entity;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Pit implements Receivable, Collectible {

    private final String id;
    private int stones;

    public Pit(String id) {
        this.id = id;
        stones = 4;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getStones() {
        return stones;
    }

    @Override
    public void addStone() {
        stones++;
    }

    @Override
    public void add(int stones) {
        if (stones > 0) {
            this.stones += stones;
        }
    }

    @Override
    public boolean isEmpty() {
        return this.stones == 0;
    }

    @Override
    public void collectStones() {
        stones = 0;
    }

}
