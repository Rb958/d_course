package com.sabc.digitalchampions.exceptions;

public class SectionExistException extends AbstractException {
    public SectionExistException() {
        super("This section already exists in chapter", 607);
    }
}
