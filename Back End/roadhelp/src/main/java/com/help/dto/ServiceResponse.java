package com.help.dto;

import java.util.List;

public class ServiceResponse <Type>{
    private String msg;
    private Type object;
    private List<Type> objects;

    public ServiceResponse(String msg, Type object) {
        this.msg = msg;
        this.object = object;
    }

    public ServiceResponse(String msg, List<Type> objects) {
        this.msg = msg;
        this.objects = objects;
    }

    public List<Type> getObjects() {
        return objects;
    }

    public void setObjects(List<Type> objects) {
        this.objects = objects;
    }

    public Type getObject() {
        return object;
    }

    public void setObject(Type object) {
        this.object = object;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
