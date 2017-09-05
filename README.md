
[ ![Download](https://api.bintray.com/packages/elderbyte/maven/warden-spring-security-jwt/images/download.svg) ](https://bintray.com/elderbyte/maven/warden-spring-security-jwt/_latestVersion) 


# Spring Security JWT integration

This project provides an out of the box working JWT integration into Spring security, using springs auto-configuration features.

## How to use

1. Add this library as a dependency to your build. Since this library is auto-configuration enabled, it will enable itself by default.


### Configuration Properties

**warden.client.realm** *[string]* (optional)

Define the identity name of the realm / audience. Incomming JWT tokens must have a matching realm / audience claim to be accepted. 


**warden.client.publicKeyValue** *[string]* (required)

Configure the Public Key used to verify incomming JWT Tokens. This property is required by the default `RSAPublicKeyProvider`.
If desired, you may overwrite the `RSAPublicKeyProvider` with Spring configuration and use your own logic to retirve it.


### Securing your methods and endpoints

You basically use the standard Spring security features.
* For simple cases you should use the standard Spring `@Secured("ROLE_USER"")` annotations.
* For more complex cases, you can use the `Acw` utility to build logical expressions:

```java
// Statically import the Acw utility
import static com.elderbyte.warden.spring.local.auth.Acw.*;


public void mySecuredMethod(){

    requireAll(hasRole(KnownRole.ROLE_OWNER), hasAnyRealm(Realm.Master, realmId)).enforce();
    
    // Execute your critical code
}
```


### Mocking Security for testing / development

While developing your secured Spring Application, the security can be quite an obstacle. 
You could disable it completely, however, a better alternative with more possiblities is to just mock a security context.

`warden.client.enableMock: true`


