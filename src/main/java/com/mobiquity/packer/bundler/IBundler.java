package com.mobiquity.packer.bundler;

import com.mobiquity.packer.dto.Deliverable;
import java.util.List;

public interface IBundler {

    // Challenge Comment:  Since it's an CONSTRAINT over the requirements I left it as a CONSTANT,
    // but it could be also possible to get it as a configuration parameter at application start up.
    // like:
    //      -> creating our own way to load config property by file in our jvm classpath
    //      -> using some framework for it (like spring)
    //      -> using system variables System.getenv("SOME_SYSTEM_VARIABLE")
    Integer MAX_PACKAGE_WEIGHT = 100;
    Integer MAX_PACKAGE_DELIVERABLES = 15;
    Integer MAX_ITEM_COST = 100;
    Integer MAX_ITEM_WEIGHT = 100;

    String bundle(Integer maxPackageWeight, List<Deliverable> deliverables);
}