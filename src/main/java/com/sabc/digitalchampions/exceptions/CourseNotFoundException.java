package com.sabc.digitalchampions.exceptions;

public class CourseNotFoundException extends AbstractException {
    public CourseNotFoundException() {
        super("This course was not found", 802);
    }
}
