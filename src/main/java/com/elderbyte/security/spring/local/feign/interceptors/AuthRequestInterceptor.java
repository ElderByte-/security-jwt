package com.elderbyte.security.spring.local.feign.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public interface AuthRequestInterceptor extends RequestInterceptor {
    /**
     * Called for every request. Add data using methods on the supplied {@link RequestTemplate}.
     */
    void apply(RequestTemplate template);
}
