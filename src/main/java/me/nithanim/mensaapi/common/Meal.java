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

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return '<' + desc + '|' + price + '>';
    }
}
