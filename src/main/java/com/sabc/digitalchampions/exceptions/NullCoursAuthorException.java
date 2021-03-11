package com.sabc.digitalchampions.exceptions;

public class NullCoursAuthorException extends AbstractException {
    public NullCoursAuthorException() {
        super("This cours must have an author", 710);
    }
}
