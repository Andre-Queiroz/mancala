package queiroz.mancala.model.entity;

/**
 * Entities that can lose stones.
 */
public interface Collectible {

    void collectStones();

    boolean isEmpty();

}
