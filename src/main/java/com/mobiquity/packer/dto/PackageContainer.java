package com.mobiquity.packer.dto;

import java.util.List;

public class PackageContainer {
    private List<Package> packages;

    public PackageContainer(List<Package> packages) {
        this.packages = packages;
    }
    public PackageContainer() {
    }

    public List<Package> getPackages() {
        return packages;
    }
    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
