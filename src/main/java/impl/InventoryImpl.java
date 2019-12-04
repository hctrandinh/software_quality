package impl;

import contract.Inventory;
import contract.Item;

import java.util.HashMap;
import java.util.Map;

public final class InventoryImpl<T> implements Inventory<T> {

    // TODO: FIX
    private static final long DEFAULT_VALUE = 0L;
    private final Map<T, Long> inventory;

    InventoryImpl() {
        inventory = new HashMap<>();
    }

    @Override
    public long getQuantity(T item) {
        return inventory.getOrDefault(item, DEFAULT_VALUE);
    }

    @Override
    public void add(T item, long quantity) {
        long newQuantity = quantity + inventory.getOrDefault(item, DEFAULT_VALUE);
        if(newQuantity >= inventory.getOrDefault(item, DEFAULT_VALUE)) {
            inventory.put(item, newQuantity);
        }
    }

    @Override
    public void deduct(T item, long quantity) {
        long newQuantity;
        long oldQuantity = inventory.getOrDefault(item, DEFAULT_VALUE);
        if (inventory.containsKey(item)){
            //TODO: FIX
            newQuantity =  oldQuantity - quantity;
            if (newQuantity > DEFAULT_VALUE) {
                inventory.put(item, newQuantity);
            } else if (newQuantity == DEFAULT_VALUE) {
                inventory.remove(item);
            }
        }
    }

    @Override
    public boolean hasItem(T item) {
        return inventory.containsKey(item);
    }

    @Override
    public Map<T, Long> getInventory() {
        return new HashMap<>(inventory);
    }

    @Override
    public void clear() {
        inventory.clear();
    }
}
