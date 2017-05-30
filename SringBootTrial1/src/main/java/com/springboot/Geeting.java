package com.springboot;

import java.math.BigInteger;

/**
 * Created by san6685 on 11/15/2016.
 */
public class Geeting {

    private BigInteger id;
    private String message;

    public Geeting() {

    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
