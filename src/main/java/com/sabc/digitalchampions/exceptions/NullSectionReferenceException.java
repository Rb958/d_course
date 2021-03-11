package com.sabc.digitalchampions.exceptions;

public class NullSectionReferenceException extends AbstractException {
    public NullSectionReferenceException() {
        super("This content must have a reference of the section that it belongs to", 707);
    }
}
