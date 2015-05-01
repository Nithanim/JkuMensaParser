package me.nithanim.mensaapi.common;

public class Meal {
    private final String desc;
    private final int price;

    public Meal(String desc, int price) {
        this.desc = desc;
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * This gets the price of an individual meal. Does not work for
     * Classic since there is only one price for the complete menu.
     * 
     * @return Price of the meal
     * @see Menu
     */
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return '<' + desc + '|' + price + '>';
    }
}
