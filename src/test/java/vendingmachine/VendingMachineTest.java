package vendingmachine;

import contract.Bucket;
import contract.Coin;
import contract.Item;
import contract.VendingMachine;
import exceptions.*;
import impl.CoinUtil;
import impl.VendingMachineFactory;
import impl.VendingMachineImpl;
import org.junit.Ignore;
import org.testng.annotations.*;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

public final class VendingMachineTest extends VendingMachineAbstractTest {

    /*private VendingMachine vm;

    @BeforeClass
    public void setUp() {
        vm = VendingMachineFactory.INSTANCE.createVendingMachine();
    }

    @AfterClass
    public void tearDown() {
        vm = null;
    }

    @BeforeMethod
    public void beforeEachTest(){
        vm.init();
    }

    @AfterMethod
    public void afterEachTest(){
        vm.reset();
    }
    */

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

    @Test
    public void testBuyItemWithMorePrice() throws SoldOutException, ItemNotSelectedException, NotFullyPaidException,
            NotSufficientChangeException, TooMuchInsertedMoneyException {
        int price = vm.selectItemAndGetPrice(Item.SODA);
        assertEquals(Item.SODA.getPrice(), price);

        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C50);
        vm.insertCoin(Coin.C50);

        Bucket bucket = vm.collectItemAndChange();
        Item item = bucket.getItem();
        List<Coin> change = bucket.getChange();

        //should be Soda
        assertEquals(Item.SODA, item);
        //there should not be change
        assertTrue(!change.isEmpty());
        //comparing change
        assertEquals(150 - Item.SODA.getPrice(), CoinUtil.getTotal(change));

    }


    @Test
    public void testVendingMachineFactory() {
        VendingMachine vmachine = VendingMachineFactory.INSTANCE.createVendingMachine();
        assertNotEquals(vm, vmachine);
    }


}
