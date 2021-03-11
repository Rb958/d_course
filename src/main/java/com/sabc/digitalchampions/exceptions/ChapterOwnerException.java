package com.sabc.digitalchampions.exceptions;

public class ChapterOwnerException extends AbstractException {
    public ChapterOwnerException() {
        super("This chapter does not belongs to the specified course", 405);
    }
}
