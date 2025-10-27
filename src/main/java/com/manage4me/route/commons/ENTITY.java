package com.manage4me.route.commons;

import java.io.Serializable;

public interface ENTITY<T extends Serializable> {

    T getId();

    void setId(T t);

}
