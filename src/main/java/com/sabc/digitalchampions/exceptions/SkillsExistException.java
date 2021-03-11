package com.sabc.digitalchampions.exceptions;

public class SkillsExistException extends AbstractException {
    public SkillsExistException() {
        super("This Skill already exist", 605);
    }
}
