package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPacker {

    @Test
    public void shouldPackWithSuccess() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "example_input");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        String expectedResult = String.format("4%s-%s2,7%s8,9", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        // WHEN
        String pkgBuilt = Packer.pack(absolutePath);
        // THEN
        assertEquals(expectedResult, pkgBuilt);
    }

    @Test
    public void shouldPackWithSuccessWithInput2() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "valid_inputs", "example_input2");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        String expectedResult = String.format("-%s-%s1%s-", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        // WHEN
        String pkgBuilt = Packer.pack(absolutePath);
        // THEN
        assertEquals(expectedResult, pkgBuilt);
    }

    @Test
    public void shouldPackWithSuccessWithInput3WithMoreThan15Items() throws APIException {
        // GIVEN
        Path resourceDirectory = Paths.get("src", "test", "resources", "valid_inputs", "example_input3");
        String absolutePath = resourceDirectory.toFile().getAbsolutePath();
        String expectedResult = String.format("1,2,4,5,6,7,8,9,11,12,13,14,15,16,17%s9,10,11,12,13,14,15,16", System.lineSeparator(), System.lineSeparator(), System.lineSeparator());
        // WHEN
        String pkgBuilt = Packer.pack(absolutePath);
        // THEN
        assertEquals(expectedResult, pkgBuilt);
    }

}