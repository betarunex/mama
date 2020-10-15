package com.mama.money.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DestinationUtil {


    public String destinationFromSelection(final int option){
        final Destination destination = Destination.get(option);
        if (destination == null) {
            return "";
        }
        switch (destination) {
            case KENYA:
                return "Kenya";
            case MALAWI:
                return "Malawi";
        }
        return "";
    }

    // unused, but this would've been easier
    // public String destinationFromSelection(final String option){
    //    // use enum with string values for country.
    // }

    enum Destination {
        KENYA(1),
        MALAWI(2);

        private int destination;
        private static Map<Integer, Destination> destinations = new HashMap<>();

        Destination(final int option) {
            this.destination = option;
        }

        static {
            for (Destination menu : Destination.values()) {
                destinations.put(menu.getDestinationNumber(), menu);
            }
        }

        public int getDestinationNumber() {
            return this.destination;
        }

        public static Destination get(final int destination) {
            return destinations.get(destination);
        }
    }
}
