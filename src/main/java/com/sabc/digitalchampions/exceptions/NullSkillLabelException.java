package com.sabc.digitalchampions.exceptions;

public class NullSkillLabelException extends AbstractException {
    public NullSkillLabelException() {
        super("The label of the skill must not null", 709);
    }
}
