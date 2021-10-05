package com.mobiquity.packer.bundler;

import com.mobiquity.packer.dto.Deliverable;
import java.util.*;
import java.util.stream.Collectors;

public class BundlePackage implements IBundler {

    public static final String DELIVERABLE_DELIMITER = ",";
    public static final String DELIVERABLE_EMPTY_VALUE = "-";

    @Override
    public String bundle(Integer maxPackageWeight, List<Deliverable> deliverables) {
        List<Deliverable> sortedDeliverablesByCost = getDeliverablesSortedByCostDesc(deliverables);
        Map<Float, List<Deliverable>> deliverablePossibilities = createDeliverablesPossibilities(maxPackageWeight, sortedDeliverablesByCost);
        List<Deliverable> selectedDeliverables = selectDeliverablesPossibility(deliverablePossibilities);
        return selectedDeliverables.isEmpty() ? DELIVERABLE_EMPTY_VALUE : joinSelectedDeliverablesAsString(selectedDeliverables);
    }

    private String joinSelectedDeliverablesAsString(List<Deliverable> selectedDeliverables) {
        return selectedDeliverables
                .stream()
                .map(Deliverable::getIndex)
                .sorted()
                .map(Object::toString)
                .collect(Collectors.joining(DELIVERABLE_DELIMITER));
    }

    private List<Deliverable> selectDeliverablesPossibility(Map<Float, List<Deliverable>> deliverablePossibilities) {
        Float higherKey = null;
        double higherDeliveriesCost = 0d;
        // since it's ordered by weight as desc, we iterate it over with the lowest weight and set the one with the higher cost
        for (Map.Entry<Float, List<Deliverable>> deliverableMap : deliverablePossibilities.entrySet()) {
            double totalDeliveryCost = deliverableMap.getValue().stream().mapToDouble(Deliverable::getCost).sum();
            if (totalDeliveryCost > higherDeliveriesCost) {
                higherDeliveriesCost = totalDeliveryCost;
                higherKey = deliverableMap.getKey();
            }
        }
        if (higherKey == null)
            return new ArrayList<>(0);

        return deliverablePossibilities.get(higherKey);
    }

    private Map<Float, List<Deliverable>> createDeliverablesPossibilities(Integer maxPackageWeight, List<Deliverable> sortedDeliverablesByCost) {
        Map<Float, List<Deliverable>> deliverablePossibilities = new HashMap<>(0);
        List<Deliverable> deliverablesHelperList;
        int iterationsExecuted = 0;
        int lastAttemptIndex = 0;
        // here we are trying to create deliverable possibilities for each deliverable that we have inside the package request
        while (iterationsExecuted < sortedDeliverablesByCost.size()) {
            int index = 0;
            Float totalPackageWeight = 0f; // total package weight for each try
            Float possiblePackageWeight = 0f; // it's a sum with the next package weight to check if it will fit on the package
            deliverablesHelperList = new ArrayList<>(0);
            for (Deliverable deliverable : sortedDeliverablesByCost) {
                possiblePackageWeight += deliverable.getWeight();
                // constraint validations
                if (isValidDeliverableWeight(maxPackageWeight, possiblePackageWeight)
                        && isValidDeliverablesSize(deliverablesHelperList)
                        && isValidDeliverable(deliverable.getWeight(), deliverable.getCost())
                        && (lastAttemptIndex <= index || index == 0)
                ) {
                    totalPackageWeight += deliverable.getWeight();
                    deliverablesHelperList.add(deliverable);
                    if (index != 0)
                        lastAttemptIndex = index + 1;
                }

                possiblePackageWeight = totalPackageWeight; // at here over the next try the possiblePackageWeight will be the same as the totalPackageWeight on this try
                index++; // index is required since we have our deliverables ordered by cost average, and we don't want to lose this order
            }
            deliverablePossibilities.put(totalPackageWeight, deliverablesHelperList); // after each try we add the deliverable map possibility here
            iterationsExecuted++;
        }

        return getDeliverablesPossibilitiesSortedByWeightAsc(deliverablePossibilities);
    }

    private LinkedHashMap<Float, List<Deliverable>> getDeliverablesPossibilitiesSortedByWeightAsc(Map<Float, List<Deliverable>> deliverablePossibilities) {
        // here we are going to have a map where the key is the total weight inside that package
        // and the value will be the list of deliverables on it ordered in asc order
        return deliverablePossibilities
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // sort map with deliverable possibilities ordered by weight
                .collect(
                        Collectors
                                .toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldValue, newValue) -> oldValue, LinkedHashMap::new
                                )
                );
    }

    private List<Deliverable> getDeliverablesSortedByCostDesc(List<Deliverable> deliverables) {
        // getting the average value between cost / weight for each delivery its possible to order
        // the deliverables in reversed order to have the deliverables ordered with the higher cost and less weight
        return deliverables.stream()
                .sorted(Comparator.comparingDouble(Deliverable::getAverage).reversed())
                .collect(Collectors.toList());
    }

    // check constraint against deliverable weight and cost
    private boolean isValidDeliverable(Float weight, Float cost) {
        return weight <= MAX_ITEM_WEIGHT && cost <= MAX_ITEM_COST;
    }

    // check constraint against deliverables max size in a package
    private boolean isValidDeliverablesSize(List<Deliverable> deliverables) {
        return deliverables.size() < MAX_PACKAGE_DELIVERABLES;
    }

    // check constraint against deliverables sum weight don't overflow the MAX_PACKAGE_WEIGHT or the custom maxPackageWeight
    private boolean isValidDeliverableWeight(Integer maxPackageWeight, Float possiblePackageWeight) {
        return possiblePackageWeight <= maxPackageWeight && possiblePackageWeight <= MAX_PACKAGE_WEIGHT;
    }

}
