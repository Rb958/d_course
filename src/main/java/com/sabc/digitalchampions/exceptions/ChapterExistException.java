package com.sabc.digitalchampions.exceptions;

public class ChapterExistException extends AbstractException {
    public ChapterExistException() {
        super("This chapter already exist in this course", 606);
    }
}
