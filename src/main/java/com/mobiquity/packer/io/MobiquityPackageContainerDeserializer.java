package com.mobiquity.packer.io;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.Deliverable;
import com.mobiquity.packer.dto.Package;
import com.mobiquity.packer.dto.PackageContainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MobiquityPackageContainerDeserializer implements IPackageDeserializer {

    private static final String COLON_SEPARATOR = ":";
    private static final String COMMA_SEPARATOR = ",";
    private static final String SPLIT_DELIVERABLE_REGEX = "[\\(\\)]";
    private static final String CLEAN_UP_EMPTY_SPACES_REGEX = "\\s";
    private static final String ONLY_DIGITS_REGEX = "[^\\.A-Za-z0-9_]";
    private static final String ONLY_CHAR_REGEX = "\\w|\\.";

    public PackageContainer deserialize(String filePath) throws APIException {
        if (filePath == null || filePath.isEmpty())
            throw new APIException(String.format("Invalid or Empty filePath=%s", filePath));

        PackageContainer pkgContainer = new PackageContainer();
        try {
            List<Package> packages = parsePackages(filePath);
            pkgContainer.setPackages(packages);
        } catch (Exception ex) {
            throw new APIException(String.format("Invalid filePath=%s", filePath), ex);
        }

        return pkgContainer;
    }

    private List<Package> parsePackages(String filePath) throws IOException, APIException {
        List<String> lines = readLinesFromFile(filePath);

        List<Package> packages = new ArrayList<>(0);
        String currentLine;
        Package pkg;

        // iterates over each line in the file to create a package
        for (int index = 0; index < lines.size(); index++) {
            try {
                currentLine = lines.get(index);
                pkg = createPackage(currentLine);

                // add new package from currentLine
                packages.add(pkg);
            } catch (APIException ex) {
                throw new APIException(String.format("Invalid package at line=%s", index+1 /* just to get the right line number*/), ex);
            }
        }
        return packages;
    }

    private List<String> readLinesFromFile(String filePath) throws IOException {
        List<String> lines;
        // read with closable try and read lines from file
        // it will be read as UTF-8
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath))) {
            lines = br.lines().collect(Collectors.toList());
        }
        return lines;
    }


    private Package createPackage(String currentLine) throws APIException {
        if (currentLine.isEmpty())
            throw new APIException("Line is Empty");

        int colonIndex = currentLine.indexOf(COLON_SEPARATOR);

        if(colonIndex == -1)
            throw new APIException("Invalid Line no colon(:) were found.");

        Integer maxPackageWeight = parsePackageWeight(colonIndex, currentLine);
        List<Deliverable> deliverables = parseDeliverables(colonIndex, currentLine);
        return new Package(maxPackageWeight, deliverables);
    }


    private Integer parsePackageWeight(int colonIndex, String newPackage) throws APIException {
        try {
            String packageWeight = newPackage.substring(0, colonIndex).trim();
            return Integer.valueOf(packageWeight);
        } catch (Exception ex) {
            throw new APIException("Error while tries to parse invalid Package Weight");
        }
    }

    private List<Deliverable> parseDeliverables(int colonIndex, String newPackage) throws APIException {
        try {
            List<String> unparsedDeliverables = getUnparsedDeliverables(colonIndex, newPackage);
            return unparsedDeliverables.stream().map(this::createDeliverable).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new APIException("Error while tries to parse invalid Package Deliverables");
        }
    }

    private List<String> getUnparsedDeliverables(int colonIndex, String newPackage) {
        String deliverablesAsString = newPackage
                .substring(colonIndex + 1)
                .replaceAll(CLEAN_UP_EMPTY_SPACES_REGEX, "");
        return Arrays
                .stream(deliverablesAsString.split(SPLIT_DELIVERABLE_REGEX))
                .filter(it -> !it.isEmpty() && !it.isBlank())
                .collect(Collectors.toList());
    }

    private Deliverable createDeliverable(String unparsedDeliverable) {
        String[] deliverableData = unparsedDeliverable.split(COMMA_SEPARATOR);
        Integer index = Integer.valueOf(deliverableData[0]);
        Float weight = Float.valueOf(deliverableData[1]);
        Float cost = Float.valueOf(deliverableData[2].replaceAll(ONLY_DIGITS_REGEX, ""));
        String currency = deliverableData[2].replaceAll(ONLY_CHAR_REGEX, "");
        return new Deliverable(index, weight, cost, currency);
    }

}
