package com.sabc.digitalchampions.exceptions;

public class NullChapterReferenceException extends AbstractException {
    public NullChapterReferenceException() {
        super("This section must have a reference of Chapter that it belongs to", 707);
    }
}
