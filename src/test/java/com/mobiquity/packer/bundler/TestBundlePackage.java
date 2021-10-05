package com.mobiquity.packer.bundler;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.Deliverable;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBundlePackage {

    @Test
    public void shouldBundlePackageWithSuccessScenario1FromExampleInput() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 81;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 53.38f, 45f),
                new Deliverable(2, 88.62f, 98f),
                new Deliverable(3, 78.48f, 3f),
                new Deliverable(4, 72.30f, 76f),
                new Deliverable(5, 30.18f, 9f),
                new Deliverable(6, 46.34f, 48f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("4", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithSuccessScenario2FromExampleInput() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 8;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 15.3f, 34f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("-", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithSuccessScenario3FromExampleInput() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 75;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 85.31f, 29f),
                new Deliverable(2, 14.55f, 74f),
                new Deliverable(3, 3.98f, 16f),
                new Deliverable(4, 26.24f, 55f),
                new Deliverable(5, 63.69f, 52f),
                new Deliverable(6, 76.25f, 75f),
                new Deliverable(7, 60.02f, 74f),
                new Deliverable(8, 93.18f, 35f),
                new Deliverable(9, 89.95f, 78f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("2,7", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithSuccessScenario4FromExampleInput() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 56;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 90.72f, 13f),
                new Deliverable(2, 33.80f, 40f),
                new Deliverable(3, 43.15f, 10f),
                new Deliverable(4, 37.97f, 16f),
                new Deliverable(5, 46.81f, 36f),
                new Deliverable(6, 48.77f, 79f),
                new Deliverable(7, 81.80f, 45f),
                new Deliverable(8, 19.36f, 79f),
                new Deliverable(9, 6.76f, 64f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("8,9", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithDefaultValueWhenPackageIsOverMaxPackageWeightRule() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 80;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 101f, 13f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("-", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithDefaultValueWhenPackageIsOverCustomMaxPackageWeightRule() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 80;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 81f, 13f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("-", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithDefaultValueWhenPackageDeliverableIsOverCosttRule() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 80;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 2f, 101f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("-", pkgBuilt);
    }

    @Test
    public void shouldBundlePackageWithSuccessAndSelectWithLessWeightWhenHaveTheSamePrice() throws APIException {
        // GIVEN
        Integer maxPackageWeight = 2;
        List<Deliverable> deliverables = Arrays.asList(
                new Deliverable(1, 2f, 10f),
                new Deliverable(2, 2f, 10f),
                new Deliverable(3, 1f, 10f)
        );

        // WHEN
        String pkgBuilt = new BundlePackage().bundle(maxPackageWeight, deliverables);
        // THEN
        assertEquals("3", pkgBuilt);
    }

}
