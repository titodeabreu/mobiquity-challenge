package com.mobiquity.packer.dto;

// Challenge Comment: Since its already on dto package, I don't think its required to be explicit in class name that it's a DTO
public class Deliverable {
    private final Integer index;
    private final Float weight;
    private final Float cost;
    private final String currency;


    // Challenge Comment: these values can't be changed after the class is created, so it's immutable
    // and this is important because we don't want to change these values during the build process or any other step
    public Deliverable(Integer index, Float weight, Float cost, String currency) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
        this.currency = currency;
    }

    public Deliverable(Integer index, Float weight, Float cost) {
        this.index = index;
        this.weight = weight;
        this.cost = cost;
        this.currency = null;
    }

    public Integer getIndex() {
        return index;
    }

    public Float getWeight() {
        return weight;
    }

    public Float getCost() {
        return cost;
    }

    public Float getAverage() {
        return cost / weight;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Deliverable{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                ", currency='" + currency + '\'' +
                '}';
    }
}
