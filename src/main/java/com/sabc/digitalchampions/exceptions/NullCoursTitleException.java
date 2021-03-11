package com.sabc.digitalchampions.exceptions;

public class NullCoursTitleException extends AbstractException {
    public NullCoursTitleException() {
        super("This course must have a title", 709);
    }
}
