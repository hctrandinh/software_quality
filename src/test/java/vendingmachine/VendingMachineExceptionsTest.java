package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import exceptions.*;
import org.testng.annotations.Test;

import java.util.List;

public class VendingMachineExceptionsTest extends VendingMachineAbstractTest {


    @Test(expectedExceptions = {ItemNotSelectedException.class})
    public void testItemNotSelectedException() throws ItemNotSelectedException,
            NotFullyPaidException, NotSufficientChangeException {
        vm.collectItemAndChange();
    }


    @Test(expectedExceptions = {NotFullyPaidException.class})
    public void testNotFullyPaidException() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotSufficientChangeException, NotFullyPaidException {
        vm.selectItemAndGetPrice(Item.COKE);
        vm.insertCoin(Coin.C50);
        Bucket bucket = vm.collectItemAndChange();

    }

    @Test(expectedExceptions = {NotSufficientChangeException.class})
    public void testNotSufficientChangeException() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
        Bucket bucket;
        List<Coin> refund;
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            bucket = vm.collectItemAndChange();
            bucket.clearAll();
            refund = vm.refund();

            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            bucket = vm.collectItemAndChange();
            bucket.clearAll();
            refund = vm.refund();

            vm.selectItemAndGetPrice(Item.SPRITE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            bucket = vm.collectItemAndChange();
            bucket.clearAll();
            refund = vm.refund();
        }
    }


    @Test(expectedExceptions = {SoldOutException.class})
    public void testSoldOut() throws SoldOutException, TooMuchInsertedMoneyException, ItemNotSelectedException,
            NotFullyPaidException, NotSufficientChangeException {
        Bucket bucket;
        for (int i = 0; i <= 5; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C20);
            vm.insertCoin(Coin.C10);
            bucket = vm.collectItemAndChange();
            bucket.clearAll();
        }
    }

    @Test(expectedExceptions = {TooMuchInsertedMoneyException.class})
    public void testTooMuchInsertedMoneyException() throws SoldOutException, ItemNotSelectedException,
            NotSufficientChangeException, TooMuchInsertedMoneyException, NotFullyPaidException {
        vm.selectItemAndGetPrice(Item.WATER);
        for (int i = 0; i < 6; i++) {
            vm.insertCoin(Coin.C100);
        }
        Bucket bucket = vm.collectItemAndChange();
    }

    @Test(expectedExceptions = {SoldOutException.class})
    public void testReset() throws SoldOutException {
        vm.reset();
        vm.selectItemAndGetPrice(Item.COKE);
    }
}


