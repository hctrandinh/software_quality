package contract;

import java.util.List;

public interface Bucket {

    Item getItem();

    boolean containsItem();

    List<Coin> getChange();

    void setItem(Item item);

    void addCoin(Coin coin);

    void clearAll();

    void clearChange();

    void clearItem();

}
