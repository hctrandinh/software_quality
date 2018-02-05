package impl;

import contract.Bucket;
import contract.Coin;
import contract.Item;

import java.util.ArrayList;
import java.util.List;

public final class BucketImpl implements Bucket {

    private Item item;
    private List<Coin> change;


    BucketImpl() {
        this.change = new ArrayList<>();
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public boolean containsItem() {
        return this.item != null;
    }

    @Override
    public List<Coin> getChange() {
        return change;
    }

    @Override
    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void addCoin(Coin coin) {
        this.change.add(coin);
    }

    @Override
    public void clearAll() {
        clearChange();
        clearItem();
    }

    @Override
    public void clearChange() {
        this.change.clear();
    }

    @Override
    public void clearItem() {
        this.item = null;
    }
}
