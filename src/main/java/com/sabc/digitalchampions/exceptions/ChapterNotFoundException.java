package com.sabc.digitalchampions.exceptions;

public class ChapterNotFoundException extends AbstractException {
    public ChapterNotFoundException() {
        super("This chapter was not found", 803);
    }
}
