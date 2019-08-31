package com.gcstorage.reportservice.http;

import java.io.Serializable;

/**
 * Created by EA on 2018/4/18
 */

public class BaseBean<T> implements Serializable {
    //public int code;
    public String resultcode;//

    public T response;
    public String resultmessage;

    public String getResultcode() {
        return resultcode == null ? "" : resultcode;
    }

    public void setResultcode(String resultcode) {
        this.resultcode = resultcode;
    }

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getResultmessage() {
        return resultmessage == null ? "" : resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }
}
