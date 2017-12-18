package contract;

public enum Coin {

    C01(1), C05(5), C10(10), C20(20), C50(50), C100(100);

    private final int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
