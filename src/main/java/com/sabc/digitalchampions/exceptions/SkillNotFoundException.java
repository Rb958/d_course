package com.sabc.digitalchampions.exceptions;

public class SkillNotFoundException extends AbstractException {
    public SkillNotFoundException() {
        super("This Skill does not exist", 806);
    }
}
