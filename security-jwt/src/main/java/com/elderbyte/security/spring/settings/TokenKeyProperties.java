package com.elderbyte.security.spring.settings;

public class TokenKeyProperties {

    /**
     * The public key to use for validating RSA signed tokens.
     * (Precedence over the url property)
     */
    private String value;

    /**
     * The url to obtain the public key for validating RSA signed tokens.
     * (Will be ignored when value is explicitly set)
     *
     * The url supports template variables:
     *
     * {realm} --> replaced by the current realm name
     */
    private String url;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
