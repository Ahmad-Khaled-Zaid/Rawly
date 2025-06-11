package com.rawly.webapp.exception;

import java.util.List;

public class DuplicateFieldException extends RuntimeException {
    private final List<String> fields;

    public DuplicateFieldException(List<String> conflictsList) {
        super("Duplicate fields found: " + String.join(", ", conflictsList));
        this.fields = conflictsList;
    }

    public List<String> getFields() {
        return fields;
    }
}
