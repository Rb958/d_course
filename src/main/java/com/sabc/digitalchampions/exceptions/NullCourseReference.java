package com.sabc.digitalchampions.exceptions;

public class NullCourseReference extends AbstractException {
    public NullCourseReference() {
        super("The chapter must have a valid course reference", 705);
    }
}
