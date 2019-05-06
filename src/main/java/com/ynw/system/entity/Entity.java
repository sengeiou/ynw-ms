package com.ynw.system.entity;

import java.io.Serializable;

public interface Entity extends Serializable,Cloneable {
    void setId(String id);
}
