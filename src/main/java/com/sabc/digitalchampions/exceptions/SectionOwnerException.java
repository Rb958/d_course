package com.sabc.digitalchampions.exceptions;

public class SectionOwnerException extends AbstractException {
    public SectionOwnerException() {
        super("This section does not belongs to the specified chapter", 407);
    }
}
