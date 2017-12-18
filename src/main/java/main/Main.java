package main;

import contract.Coin;
import impl.VendingMachineImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Main {
    public static void main(String[] args) {
        Coin[] values = {Coin.C01, Coin.C05, Coin.C10, Coin.C20, Coin.C50 };
        long[] available = { 3L, 4L, 8L, 6L, 4L };
        int change = 53;
        List<Coin> minCoins = new ArrayList<>();
        LinkedList<Coin> coins = new LinkedList<>();

        VendingMachineImpl.minCoins(0, change, values, available, minCoins, coins);

        System.out.println(minCoins);
        System.out.println(coins);
    }
}
