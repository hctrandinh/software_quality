package vendingmachine;

import contract.VendingMachine;
import impl.VendingMachineFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

public abstract class VendingMachineAbstractTest {

    protected VendingMachine vm;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        vm = VendingMachineFactory.INSTANCE.createVendingMachine();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        vm = null;
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeEachTest() {
        vm.init();
    }

    @AfterMethod(alwaysRun = true)
    public void afterEachTest() {
        vm.reset();
    }

}
