
### security-jwt-all [ ![Download](https://api.bintray.com/packages/elderbyte/maven/security-jwt-all/images/download.svg) ](https://bintray.com/elderbyte/maven/security-jwt-all/_latestVersion)

###  security-jwt-servlet [ ![Download](https://api.bintray.com/packages/elderbyte/maven/security-jwt-servlet/images/download.svg) ](https://bintray.com/elderbyte/maven/security-jwt-servlet/_latestVersion)

### security-jwt-webflux [ ![Download](https://api.bintray.com/packages/elderbyte/maven/security-jwt-webflux/images/download.svg) ](https://bintray.com/elderbyte/maven/security-jwt-webflux/_latestVersion)


# Spring Security JWT integration

This project provides an out of the box working JWT integration into Spring security, using springs auto-configuration features.

## How to use

1. Add this library as a dependency to your build. Since this library is auto-configuration enabled, it will enable itself by default.


### Configuration Properties

**elder.security.jwt.realm** *[string]* (optional)

Define the identity name of the realm / audience. Incomming JWT tokens must have a matching realm / audience claim to be accepted. 


**elder.security.jwt.publicKeyValue** *[string]* (required)

Configure the Public Key used to verify incomming JWT Tokens. This property is required by the default `RSAPublicKeyProvider`.
If desired, you may overwrite the `RSAPublicKeyProvider` with Spring configuration and use your own logic to retirve it.


### Securing your methods and endpoints

You basically use the standard Spring security features.
* For simple cases you should use the standard Spring `@Secured("ROLE_USER"")` annotations.
* For more complex cases, you can use the `Acw` utility to build logical expressions:

```java
// Statically import the Acw utility
import static com.elderbyte.security.spring.local.auth.Acw.*;


public void mySecuredMethod(){

    requireAll(hasRole(KnownRole.ROLE_OWNER), hasAnyRealm(Realm.Master, realmId)).enforce();
    
    // Execute your critical code
}
```


### Mocking Security for testing / development

While developing your secured Spring Application, the security can be quite an obstacle. 
If you enable mocking, the spring security context will get mocked for you:

```
elder.security.jwt:
  enableMock: true
  mockUsers:
      - realm: mycompany
        login: foo.bar
        fullName: Foo Bar
        roles: COOL_ADMIN, USER
```


## Supported JWT Claims


| JWT Claim             |      Description                          |  Example           |
|-----------------------|:-----------------------------------------:|-------------------:|
| iss                   |  The issuer of this token                 | http://my.auth.com |
| aud                   |  The intended audience                    | [master]           |
| exp                   |  The expiration date                      | (date)             |
| realm                 |  The security realm / tenant              | master             |
| username              |  The user login name                      | foo.bar            |
| sub                   |  The subject, a unique user id            | 512342314          |
| name                  |  A display friendly name                  | Foo Bar            |
| roles                 |  All granted security roles               | [USER, FRONT_DESK] |
| lang                  |  Preferred user language                  | en_US              |

