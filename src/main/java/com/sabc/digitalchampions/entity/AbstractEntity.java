package com.sabc.digitalchampions.entity;

import com.sabc.digitalchampions.exceptions.AbstractException;

public abstract class AbstractEntity {
    public abstract void checkEntity() throws AbstractException;
}
