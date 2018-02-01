package vendingmachine;

import contract.Coin;
import contract.Item;
import exceptions.*;
import org.testng.annotations.Test;

public class VendingMachineExceptionsTest extends VendingMachineAbstractTest {

    @Test(expectedExceptions = {SoldOutException.class})
    public void testReset() throws SoldOutException {
        vm.reset();
        vm.selectItemAndGetPrice(Item.COKE);
    }

    @Test(expectedExceptions = {SoldOutException.class})
    public void testSoldOut() throws SoldOutException, TooMuchInsertedMoneyException, ItemNotSelectedException,
            NotFullyPaidException, NotSufficientChangeException {
        for (int i = 0; i <= 5 ; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C20);
            vm.insertCoin(Coin.C10);
            vm.collectItemAndChange();
        }
    }

    @Test(expectedExceptions = {NotSufficientChangeException.class})
    public void testNotSufficientChangeException() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
        for (int i = 0; i < 5; i++) {
            vm.selectItemAndGetPrice(Item.COKE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            vm.collectItemAndChange();

            vm.selectItemAndGetPrice(Item.PEPSI);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            vm.collectItemAndChange();

            vm.selectItemAndGetPrice(Item.SPRITE);
            vm.insertCoin(Coin.C50);
            vm.insertCoin(Coin.C50);
            vm.collectItemAndChange();
        }
    }

}
