package com.mobiquity.packer.handler;

import com.mobiquity.exception.APIException;

public interface IPackageHandler {
    String processPkg(String filePath) throws APIException;
}
