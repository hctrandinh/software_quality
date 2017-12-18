package impl;

import contract.Bucket;
import contract.VendingMachine;

public final class VendingMachineFactory {

    public static final VendingMachineFactory INSTANCE = new VendingMachineFactory();

    public VendingMachine createVendingMachine() {
        return new VendingMachineImpl();
    }

    public Bucket createBucket() {
        return new BucketImpl();
    }

}
