package com.mobiquity.packer.handler;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.bundler.IBundler;
import com.mobiquity.packer.dto.PackageContainer;
import com.mobiquity.packer.io.IPackageDeserializer;

import java.util.stream.Collectors;

public class PackageHandler implements IPackageHandler {

    private IPackageDeserializer pkgDeserializer;
    private IBundler bundler;

    public PackageHandler(IPackageDeserializer pkgDeserializer, IBundler bundler) {
        this.pkgDeserializer = pkgDeserializer;
        this.bundler = bundler;
    }

    public String processPkg(String filePath) throws APIException {
        PackageContainer pkgContainer = pkgDeserializer.deserialize(filePath);

        if (pkgContainer == null)
            throw new APIException(String.format("Package not properly parsed with filePath %s", filePath));

        return pkgContainer
                .getPackages()
                .stream()
                .map(pkg -> bundler.bundle(pkg.getMaxPackageWeight(), pkg.getDeliverables()))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
