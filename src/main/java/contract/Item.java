package contract;

public enum Item {

    COKE(80), FANTA(80), PEPSI(90), SPRITE(90), SODA (100), JUICE(100), WATER(50);

    private final int price;

    Item(int price){
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
