package com.gilbarco.pojo;

public class SPOTResponse {

    private String action;
    private String result;

    public SPOTResponse(String action, String result) {
        this.action = action;
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public String getResult() {
        return result;
    }
}
