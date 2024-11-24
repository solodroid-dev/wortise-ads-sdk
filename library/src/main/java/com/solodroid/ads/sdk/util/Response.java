package com.solodroid.ads.sdk.util;

import java.io.Serializable;

public class Response implements Serializable {

    public boolean status;
    public String title = "";
    public String message = "";

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
