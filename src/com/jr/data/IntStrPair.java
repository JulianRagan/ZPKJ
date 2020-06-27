package com.jr.data;

import java.util.Comparator;

/**
 * Class to store single int - String pair
 *
 * @author Julian Ragan
 *
 */
public class IntStrPair {

    private String value;
    private int key;

    public IntStrPair(int key, String value) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }
}

class IntStrPairSortByValue implements Comparator<IntStrPair> {

    private boolean order;

    public IntStrPairSortByValue(boolean order) {
        this.order = order;
    }

    public IntStrPairSortByValue() {
        this.order = true;
    }

    @Override
    public int compare(IntStrPair o1, IntStrPair o2) {
        if (order) {
            return o1.getValue().compareTo(o2.getValue());
        } else {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

}

class IntStrPairSortByKey implements Comparator<IntStrPair> {

    private boolean order;

    public IntStrPairSortByKey(boolean order) {
        this.order = order;
    }

    public IntStrPairSortByKey() {
        this.order = true;
    }

    @Override
    public int compare(IntStrPair o1, IntStrPair o2) {
        if (order) {
            return o1.getKey() - o2.getKey();
        } else {
            return o2.getKey() - o1.getKey();
        }
    }

}
