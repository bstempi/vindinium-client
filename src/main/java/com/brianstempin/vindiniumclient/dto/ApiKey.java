package com.brianstempin.vindiniumclient.dto;

import com.google.api.client.util.Key;

/**
 * Created by bstempi on 9/15/14.
 */
public class ApiKey {

    @Key
    private final String key;

    public ApiKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
