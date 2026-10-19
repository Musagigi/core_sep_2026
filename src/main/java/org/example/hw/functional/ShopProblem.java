package org.example.hw.functional;

import java.util.Comparator;
import java.util.List;

public class ShopProblem {

    public CoffeeShop findCoffeeNearMe(Location you, List<CoffeeShop> shops) {
        return shops.stream().min(Comparator.comparing(shop -> you.distanceBetween(shop.location()))).orElse(null);
    }

    interface Location {
        long distanceBetween(Location other);
    }

    interface CoffeeShop {
        Location location();
    }

}
