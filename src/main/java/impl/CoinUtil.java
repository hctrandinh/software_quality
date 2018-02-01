package impl;

import contract.Coin;

import java.util.List;

public final class CoinUtil {
    public static int getTotal(List<Coin> change){
        return change.stream().map(Coin::getValue).reduce(0, (a, b) -> a + b);
    }
}
