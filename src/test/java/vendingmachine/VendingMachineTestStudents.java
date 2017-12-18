package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import contract.VendingMachine;
import exceptions.*;
import impl.CoinUtil;
import impl.VendingMachineFactory;
import org.junit.Ignore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class VendingMachineTestStudents {

    private VendingMachine vm;

    @BeforeClass
    public void setUp() {
        vm = VendingMachineFactory.INSTANCE.createVendingMachine();
        vm.init();
    }

    @AfterClass
    public void tearDown() {
        vm.reset();
        vm = null;
    }

    @Test
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
        vm.insertCoin(Coin.C10);

        Bucket bucket = vm.collectItemAndChange();
        Item item = bucket.getItem();
        List<Coin> change = bucket.getChange();

        //should be Coke
        assertEquals(Item.COKE, item);
        //there should not be any change
        assertTrue(change.isEmpty());
    }

    @Test
    public void testBuyItemWithMorePrice() throws SoldOutException, ItemNotSelectedException, NotFullyPaidException,
            NotSufficientChangeException, TooMuchInsertedMoneyException {
        int price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C50);

        Bucket bucket = vm.collectItemAndChange();
        Item item = bucket.getItem();
        List<Coin> change = bucket.getChange();

        //should be Coke
        assertEquals(Item.SODA, item);
        //there should not be any change
        assertTrue(change.isEmpty());
        //comparing change
        assertEquals(100 - Item.SODA.getPrice(), CoinUtil.getTotal(change));

    }


    @Test
    public void testRefund() throws SoldOutException, TooMuchInsertedMoneyException {
        int price = vm.selectItemAndGetPrice(Item.PEPSI);
        assertEquals(Item.PEPSI.getPrice(), price);
        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C20);
        vm.insertCoin(Coin.C10);
        vm.insertCoin(Coin.C10);

        assertEquals(90, CoinUtil.getTotal(vm.refund()));
    }

    @Test(expectedExceptions = {SoldOutException.class})
    public void testReset() throws SoldOutException {
        VendingMachine vmachine = VendingMachineFactory.INSTANCE.createVendingMachine();
        vmachine.reset();
        vmachine.selectItemAndGetPrice(Item.COKE);
    }

    @Ignore
    public void testVendingMachineImpl() {
        VendingMachine vm = VendingMachineFactory.INSTANCE.createVendingMachine();
    }


}
