package com.sabc.digitalchampions.exceptions;

public class CourseExistException extends AbstractException{
    public CourseExistException() {
        super("This course already exist", 603);
    }
}
