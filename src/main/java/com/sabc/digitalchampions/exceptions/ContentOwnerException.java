package com.sabc.digitalchampions.exceptions;

public class ContentOwnerException extends AbstractException {
    public ContentOwnerException() {
        super("This content does not belong to the specified section", 407);
    }
}
