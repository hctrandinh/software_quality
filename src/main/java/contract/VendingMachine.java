package contract;

import exceptions.*;

import java.util.List;

public interface VendingMachine {

    void init();

    int selectItemAndGetPrice(Item item) throws SoldOutException;

    void insertCoin(Coin coin) throws TooMuchInsertedMoneyException;

    List<Coin> refund();

    Bucket collectItemAndChange() throws ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException;

    void reset();

    int totalSales();

    Inventory<Coin> getCashInventory();

    Inventory<Item> getItemInventory();

}
