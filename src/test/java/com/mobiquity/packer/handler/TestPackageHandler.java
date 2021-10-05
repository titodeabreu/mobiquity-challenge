package com.mobiquity.packer.handler;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.bundler.BundlePackage;
import com.mobiquity.packer.dto.Package;
import com.mobiquity.packer.dto.PackageContainer;
import com.mobiquity.packer.io.IPackageDeserializer;
import com.mobiquity.packer.io.MobiquityPackageContainerDeserializer;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPackageHandler {

    @Test
    public void shouldProcessPkgWithSuccess() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "example_input");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        String expectedResult = String.format("4%s-%s2,7%s8,9", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        // WHEN
        String packageFromFile = new PackageHandler(new MobiquityPackageContainerDeserializer(), new BundlePackage())
                .processPkg(absolutePath);
        // THEN
        assertEquals(expectedResult, packageFromFile);
    }

    @Test
    public void shouldProcessPkgWithSuccessWhenPackagesDeliverablesAreEmpty() throws APIException {
        // GIVEN
        String filePath = null;
        Package pkg = new Package(60, Collections.emptyList());
        PackageContainer container = new PackageContainer(Arrays.asList(pkg));

        IPackageDeserializer customImpl = new IPackageDeserializer() {
            @Override
            public PackageContainer deserialize(String filePath) {
                return container;
            }
        };
        // WHEN
        String packageFromFile = new PackageHandler(customImpl, new BundlePackage())
                .processPkg(filePath);
        // THEN
        assertEquals(BundlePackage.DELIVERABLE_EMPTY_VALUE, packageFromFile);
    }


    @Test
    public void shouldThrowAPIExceptionWhenPackageContainerIsNull() {
        // GIVEN
        String filePath = null;
        IPackageDeserializer customImpl = new IPackageDeserializer() {
            @Override
            public PackageContainer deserialize(String filePath) {
                return null;
            }
        };
        // WHEN & THEN
        assertThrows(APIException.class, () -> new PackageHandler(customImpl, new BundlePackage())
                .processPkg(filePath));

    }

}
