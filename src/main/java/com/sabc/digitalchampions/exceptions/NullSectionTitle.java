package com.sabc.digitalchampions.exceptions;

public class NullSectionTitle extends AbstractException {
    public NullSectionTitle() {
        super("The Section must have a title", 711);
    }
}
