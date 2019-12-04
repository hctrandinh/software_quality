package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import contract.VendingMachine;
import exceptions.*;
import impl.CoinUtil;
import impl.VendingMachineFactory;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.*;

public final class VendingMachineTest extends VendingMachineAbstractTest {

    @Test(description = "Vending machine not null")
    public void testVendingMachine(){
        assertNotNull(vm);
    }

    @Test(description = "5 item of each in the machine inventory")
    public void testItemInit(){
        for (Item item : Item.values()) {
            assertEquals(5L, vm.getItemInventory().getQuantity(item));
        }
    }

    @Test(description = "Buy item with exact price")
    public void testBuyItemWithExactPrice() throws SoldOutException, TooMuchInsertedMoneyException,
            ItemNotSelectedException, NotFullyPaidException, NotSufficientChangeException {
        //select item, price in cents
        int price = vm.selectItemAndGetPrice(Item.COKE);
        //price should be Coke's price
        assertEquals(Item.COKE.getPrice(), price);
        //80 cents paid
        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C20);
        vm.insertCoin(Coin.C10);

        Bucket bucket = vm.collectItemAndChange();
        Item item = bucket.getItem();
        List<Coin> change = bucket.getChange();

        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change
        assertTrue(change.isEmpty());
    }

    @Test(description = "Buy item with more price")
    public void testBuyItemWithMorePrice() throws SoldOutException, ItemNotSelectedException, NotFullyPaidException,
            NotSufficientChangeException, TooMuchInsertedMoneyException {
        int price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C50);

        Bucket bucket = vm.collectItemAndChange();
        Item item = bucket.getItem();
        List<Coin> change = new ArrayList<>(bucket.getChange());

        //should be Soda
        assertEquals(Item.SODA, item);
        //there should be change
        assertTrue(!change.isEmpty());
        //comparing change
        assertEquals(150 - Item.SODA.getPrice(), CoinUtil.getTotal(change));
    }

    @Test(description = "Get refund")
    public void testRefund() throws SoldOutException, TooMuchInsertedMoneyException {
        int price = vm.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C20);
        vm.insertCoin(Coin.C10);
        vm.insertCoin(Coin.C10);

        assertEquals(90, CoinUtil.getTotal(vm.refund()));
    }

    @Test(description = "Factory creates a new vending machine each time")
    public void testVendingMachineFactory() {
        VendingMachine vmachine = VendingMachineFactory.INSTANCE.createVendingMachine();
        assertNotEquals(vm, vmachine);
    }

}
