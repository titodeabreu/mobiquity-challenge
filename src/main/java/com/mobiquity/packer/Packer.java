package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.bundler.BundlePackage;
import com.mobiquity.packer.handler.PackageHandler;
import com.mobiquity.packer.io.MobiquityPackageContainerDeserializer;

public class Packer {

    private Packer() {
    }

    // Challenge Comment: here we have the magic...
    // I choose the path to have many interfaces around a handler that can be injected as needed
    // in order to have the package built
    // Like we have our
    // IPackageHandler:
    //      interface signature ->  String processPkg(String filePath) throws APIException;
    // that is quit able to processPkg by a filepath.
    // but this filePath needs to be loaded and deserialized and in order to do it
    // we have an
    // IPackageDeserializer:
    //      interface signature ->  Package deserialize(String filePath) throws APIException;
    // that could handle many file structures for each impl over it,
    // in our case we are using MobiquityPackageDeserializer based on the challenger rules.
    // And finally we have our
    // IBundler:
    //      interface signature ->  String bundle(Integer maxPackageWeight, List<Deliverable> deliverables);
    // that is responsible to get our package items and constraints and bundle it together based on the requirements.
    // This left to us an interface based lib
    // where we can change behavior as needed without have a big impact over the application,
    // also I tried to put it all following the single responsibility principle to be easy to test it.
    // Another thing this approach that we are passing instances through the constructors is an easy
    // to be unit tested and have could have a diff behavior only by passing a new implementation instance.

    // SIDE note: another approach for this problem could be the design pattern template method
    // creating an abstract class with a processPkg method
    // and inside it have the deserializer and bundle methods as abstract
    // and each impl of this abstract class could be called here.
    public static String pack(String filePath) throws APIException {
        return new PackageHandler(new MobiquityPackageContainerDeserializer(), new BundlePackage())
                .processPkg(filePath);
    }
}