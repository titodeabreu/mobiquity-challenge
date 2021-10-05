package com.mobiquity.packer.io;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.Package;
import com.mobiquity.packer.dto.PackageContainer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TestMobiquityPackageContainerDeserializer {

    @Test
    public void shouldDeserializePackageContainerWithSuccess() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "example_input");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        int expectedPackagesRequests = 4;
        int expectedFirstPackageDeliverables = 6;
        int expectedSecondPackageDeliverables = 1;
        int expectedThirdPackageDeliverables = 9;
        int expectedFourthPackageDeliverables = 9;

        // WHEN
        PackageContainer pkgContainer = new MobiquityPackageContainerDeserializer().deserialize(absolutePath);

        // THEN
        assertNotNull(pkgContainer);
        assertEquals(expectedPackagesRequests, pkgContainer.getPackages().size());
        assertEquals(expectedFirstPackageDeliverables, pkgContainer.getPackages().get(0).getDeliverables().size());
        assertEquals(expectedSecondPackageDeliverables, pkgContainer.getPackages().get(1).getDeliverables().size());
        assertEquals(expectedThirdPackageDeliverables, pkgContainer.getPackages().get(2).getDeliverables().size());
        assertEquals(expectedFourthPackageDeliverables, pkgContainer.getPackages().get(3).getDeliverables().size());
    }

    @Test
    public void shouldDeserializePackageContainerWithSuccessScenario2() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "valid_inputs", "example_input_max_package_weight_valid");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();

        int expectedPackagesRequests = 1;
        int expectedFirstPackageDeliverables = 1;
        Integer expectedMaxPackageWeight = 8;

        // WHEN
        PackageContainer pkgContainer = new MobiquityPackageContainerDeserializer().deserialize(absolutePath);
        Package pkg = pkgContainer.getPackages().get(0);
        // THEN
        assertNotNull(pkgContainer);
        assertEquals(expectedPackagesRequests, pkgContainer.getPackages().size());
        assertEquals(expectedFirstPackageDeliverables, pkgContainer.getPackages().get(0).getDeliverables().size());
        assertEquals(expectedMaxPackageWeight, pkg.getMaxPackageWeight());
    }


    @Test
    public void shouldThrowAPIExceptionWhenFilePathIsNull() throws APIException {
        // GIVEN
        String filePath = null;
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(filePath));
    }

    @Test
    public void shouldThrowAPIExceptionWhenFilePathIsEmpty() throws APIException {
        // GIVEN
        String filePath = "";
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(filePath));
    }

    @Test
    public void shouldThrowAPIExceptionWhenFilePathIsFileNotExists() throws APIException {
        // GIVEN
        String filePath = "someInvalidPath" + File.separator + "invalidFile";
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(filePath));
    }


    @Test
    public void shouldThrowAPIExceptionWhenHasEmptyLine() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "invalid_inputs", "invalid_input_empty_line");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(absolutePath));
    }

    @Test
    public void shouldThrowAPIExceptionWhenIsMissingColon() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "invalid_inputs", "invalid_input_without_colon");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(absolutePath));
    }

    @Test
    public void shouldThrowAPIExceptionWhenMaxPackageWeightIsInvalid() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "invalid_inputs", "invalid_input_max_package_weight");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(absolutePath));
    }


    @Test
    public void shouldThrowAPIExceptionWhenADeliverableIsInvalidInsideThePackage() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "invalid_inputs", "invalid_input_package_deliverable");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        // WHEN & THEN
        assertThrows(APIException.class, () -> new MobiquityPackageContainerDeserializer().deserialize(absolutePath));
    }


}
