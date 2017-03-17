# Spring Security JWT integration

This project provides an out of the box working JWT integration into Spring security, using springs auto-configuration features.

## How to use

1. Add this library as a dependency to your build. Since this library is auto-configuration enabled, it will enable itself by default.


### Securing your methods and endpoints

You basically use the standard Spring security features.
* For simple cases you should use the standard Spring `@Secured("ROLE_USER"")` annotations.
* For more complex cases, you can use the `Acw` utility to build logical expressions:

```
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




