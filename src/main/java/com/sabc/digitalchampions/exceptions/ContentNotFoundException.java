package com.sabc.digitalchampions.exceptions;

public class ContentNotFoundException extends AbstractException {
    public ContentNotFoundException() {
        super("This content does not exists", 805);
    }
}
