package com.mobiquity.packer.dto;

import java.util.List;

public class Package {

    private final Integer maxPackageWeight;
    private final List<Deliverable> deliverables;

    public Package(Integer maxPackageWeight, List<Deliverable> deliverables) {
        this.maxPackageWeight = maxPackageWeight;
        this.deliverables = deliverables;
    }

    public Integer getMaxPackageWeight() {
        return maxPackageWeight;
    }

    public List<Deliverable> getDeliverables() {
        return deliverables;
    }

    @Override
    public String toString() {
        return "Package{" +
                "maxPackageWeight=" + maxPackageWeight +
                ", deliverables=" + deliverables +
                '}';
    }
}
