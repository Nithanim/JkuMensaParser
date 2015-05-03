package me.nithanim.mensaapi.common;

import java.util.List;

public class Menu {
    private final Type type;
    private final String subtype;
    private final List<Meal> meals;
    private final int price;
    private final int oehBonus;
    private final String date;
    private final boolean isVegetarian;
    
    public Menu(Type type, String subtype, List<Meal> meals, int price, int oehBonus, String date, boolean isVegetarian) {
        this.type = type;
        this.subtype = subtype;
        this.meals = meals;
        this.price = price;
        this.oehBonus = oehBonus;
        this.date = date;
        this.isVegetarian = isVegetarian;
    }

    
    /*
     * Classic/Choice
     */
    public Type getType() {
        return type;
    }

    /**
     * The subtype applies for Choice only and can conatin:
     * Grill/Snack/...
     * 
     * @return 
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * Returns the meals in this menu.
     * For classic, it will contain the parts (soup, main, dessert).
     * For choice it will return the meals of every subtype.
     * 
     * @return 
     * @see #getSubtype()
     */
    public List<Meal> getMeals() {
        return meals;
    }
    
    /**
     * This gets the price of the complete menu for classic but will
     * be -1 for Choice, since there are individual meal prices only.
     * 
     * @return Price of the meal,
     * -1 if it has none on purpose or
     * -2 if not found
     * @see Meal
     */
    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(type).append('|');
        sb.append(subtype).append('|');
        sb.append('<');
        for(Meal meal : meals) {
            sb.append(meal).append('|');
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append('>').append('|');
        sb.append(price).append('|');
        sb.append(date).append('|');
        sb.append(isVegetarian);
        sb.append('>');
        return sb.toString();
    }
}
