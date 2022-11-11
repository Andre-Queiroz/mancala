package queiroz.mancala.model.entity;

/**
 * Entities that can receive stones
 */
public interface Receivable {

    String getId();

    int getStones();

    void addStone();

    void add(int stones);

}
