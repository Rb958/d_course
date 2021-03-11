package com.sabc.digitalchampions.exceptions;

public class SectionNotFoundException extends AbstractException {
    public SectionNotFoundException() {
        super("The section referenced does not exist", 804);
    }
}
