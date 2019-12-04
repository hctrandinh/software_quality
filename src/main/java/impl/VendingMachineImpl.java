package impl;

import contract.*;
import exceptions.*;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toConcurrentMap;
import static java.util.stream.Collectors.toMap;

public final class VendingMachineImpl implements VendingMachine {

    private static final int INVENTORY_INIT_QUANTITY = 5;
    private static final int BUCKET_MAX = 500;
    private final Inventory<Coin> cashInventory;
    private final Inventory<Item> itemInventory;
    private final LinkedList<Coin> changeSolution;
    private final List<Coin> minChangeSolution;
    private Bucket currentBucket;
    private int totalSales;

    VendingMachineImpl() {
        this.cashInventory = new InventoryImpl<>();
        this.itemInventory = new InventoryImpl<>();
        currentBucket = VendingMachineFactory.INSTANCE.createBucket();
        changeSolution = new LinkedList<>();
        minChangeSolution = new ArrayList<>();
    }

    @Override
    public void init() {
        Arrays.stream(Item.values()).forEach(i -> itemInventory.add(i, INVENTORY_INIT_QUANTITY));
        Arrays.stream(Coin.values()).forEach(c -> cashInventory.add(c, INVENTORY_INIT_QUANTITY));
    }

    @Override
    public int selectItemAndGetPrice(Item item) throws SoldOutException {
        if (itemInventory.hasItem(item)) {
            currentBucket.setItem(item);
            return item.getPrice();
        } else {
            throw new SoldOutException(item.name() + " is no more available");
        }

    }

    @Override
    public void insertCoin(Coin coin) throws TooMuchInsertedMoneyException {
        // TODO: FIX
        if (CoinUtil.getTotal(currentBucket.getChange()) < BUCKET_MAX) {
            currentBucket.addCoin(coin);
        } else
            throw new TooMuchInsertedMoneyException("No money accepted after " + BUCKET_MAX + " threshold.");
    }

    @Override
    public List<Coin> refund() {
        List<Coin> toBeRefunded = new ArrayList<>(currentBucket.getChange());
        currentBucket.clearAll();
        return toBeRefunded;
    }

    @Override
    public Bucket collectItemAndChange() throws ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
        if (!isItemSelected()) {
            throw new ItemNotSelectedException("You must select an item");
        }
        if (!isFullyPaid()) {
            throw new NotFullyPaidException(currentBucket.getItem().name() + " is not yet fully paid. Remains: " + getRemainingTobeFullyPaid(), getRemainingTobeFullyPaid());
        }

        if (hasSufficientChangeForTransaction()) {
            totalSales += currentBucket.getItem().getPrice();
            addToCashInventory(currentBucket.getChange());
            // TODO: FIX
            deductFromItemInventory(currentBucket.getItem());
            currentBucket.clearChange();
            removeChangeFromCashInventory(minChangeSolution);
            minChangeSolution.forEach(c -> currentBucket.addCoin(c));
        } else {
            throw new NotSufficientChangeException("Not enough change to support transaction. Ask for refund.");
        }

        return currentBucket;
    }

    @Override
    public void reset() {
        cashInventory.clear();
        itemInventory.clear();
        totalSales = 0;
        currentBucket.clearAll();
        minChangeSolution.clear();
        changeSolution.clear();

    }

    @Override
    public int totalSales() {
        return totalSales;
    }

    @Override
    public Inventory<Coin> getCashInventory() {
        return cashInventory;
    }

    @Override
    public Inventory<Item> getItemInventory() {
        return itemInventory;
    }

    private void addToCashInventory(List<Coin> coins) {
        coins.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach(cashInventory::add);
    }

    private void deductFromItemInventory(Item item) {
        itemInventory.deduct(item, 1L);
    }

    private void removeChangeFromCashInventory(List<Coin> coins) {
        coins.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach(cashInventory::deduct);
    }

    private boolean isItemSelected() {
        return currentBucket.containsItem();
    }

    private boolean isFullyPaid() {
        return currentBucket.containsItem() && CoinUtil.getTotal(currentBucket.getChange()) >= currentBucket.getItem().getPrice();

    }

    private int getRemainingTobeFullyPaid() {
        return currentBucket.getItem().getPrice() - CoinUtil.getTotal(currentBucket.getChange());
    }

    private boolean hasSufficientChangeForTransaction() {
        int changeTobeReturned = CoinUtil.getTotal(currentBucket.getChange()) - currentBucket.getItem().getPrice();
        // trivial case = 0: customer has provided the exact amount for the price
        return changeTobeReturned == 0 || hasSufficientChangeForAmount(changeTobeReturned);
    }

    private boolean hasSufficientChangeForAmount(int amount) {
        List<Coin> allVirtualCoins = currentBucket.getChange();

        ConcurrentMap<Coin, Long> virtualInventory = cashInventory.getInventory().entrySet().stream()
                .collect(toConcurrentMap(Entry::getKey, Entry::getValue));

        allVirtualCoins.forEach(c -> virtualInventory.put(c, 1L + virtualInventory.getOrDefault(c, 0L)));

        //System.out.println("Virtual inventory contents : \n" + virtualInventory);

        Coin[] values = new Coin[virtualInventory.size()];
        long[] available = new long[virtualInventory.size()];
        int i = 0;
        for (Entry<Coin, Long> e : virtualInventory.entrySet()) {
            values[i] = e.getKey();
            available[i] = e.getValue();
            // TODO: FIX
            i++;
        }

        minChangeSolution.clear();
        changeSolution.clear();

        minCoins(0, amount, values, available, minChangeSolution, changeSolution);
        return !minChangeSolution.isEmpty();
    }

    /**
     * Given a finite multiset of coins, finds the minimum distribution of coins to reach an amount (for change).
     * @param pos
     * @param change the amount for which the distribution is sought
     * @param values the finite set of coin denominations from which to draw the change
     * @param available the list of available pieces for each denomination of coin
     * @param minCoins the minimal solution
     * @param coins the current solution
     */
    public static void minCoins(int pos, int change, Coin[] values, long[] available, List<Coin> minCoins, LinkedList<Coin> coins) {
        if (change == 0) {
            if (minCoins.isEmpty() || minCoins.size() > coins.size()) {
                minCoins.clear();
                minCoins.addAll(coins);
            }
        } else if (change < 0) {
            return;
        }

        for (int i = pos; i < values.length && values[i].getValue() <= change; i++) {
            if (available[i] > 0L) {
                available[i]--;
                coins.addLast(values[i]);
                minCoins(i, change - values[i].getValue(), values, available, minCoins, coins);
                coins.removeLast();
                available[i]++;
            }
        }
    }

}
