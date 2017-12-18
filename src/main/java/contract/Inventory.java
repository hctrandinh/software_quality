package contract;

import java.util.Map;

public interface Inventory<T> {

    long getQuantity(T item);

    void add(T item, long quantity);

    void deduct(T item, long quantity);

    boolean hasItem(T item);

    Map<T, Long> getInventory();

    void clear();
}
