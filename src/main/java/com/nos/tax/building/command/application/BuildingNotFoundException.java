package com.nos.tax.building.command.application;

import com.nos.tax.common.exception.ApplicationException;
import com.nos.tax.common.http.response.ErrorCode;

public class BuildingNotFoundException extends ApplicationException {
    public BuildingNotFoundException(String message) {
        super(message, ErrorCode.BUILDING_NOT_FOUND);
    }
}
