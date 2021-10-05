package com.mobiquity.packer.io;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.dto.PackageContainer;

public interface IPackageDeserializer {
    PackageContainer deserialize(String filePath) throws APIException;
}
