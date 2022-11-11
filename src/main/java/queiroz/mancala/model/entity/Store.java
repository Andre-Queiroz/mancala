package queiroz.mancala.model.entity;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class Store implements Receivable {

    private final String id;

    private int stones;

    public Store(String id) {
        this.id = id;
        this.stones = 0;
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

}
