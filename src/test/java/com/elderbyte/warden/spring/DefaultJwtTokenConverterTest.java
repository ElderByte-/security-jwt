package com.elderbyte.warden.spring;

import com.elderbyte.warden.spring.local.auth.AuthenticationDetail;
import com.elderbyte.warden.spring.local.jwt.DefaultJwtTokenConverter;
import com.elderbyte.warden.spring.local.jwt.JwtTokenConverter;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.PlainJWT;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests the default token converter
 */
public class DefaultJwtTokenConverterTest {

    private JwtTokenConverter tokenConverter;

    @Before
    public void init(){
        tokenConverter = new DefaultJwtTokenConverter();
    }


   @Test
    public void testConvert() throws ParseException {

       JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();


       List<String> roles = new ArrayList<>();

       roles.add("user");
       roles.add("ROLE_test");
       roles.add("admin");

       builder.issuer("myissuer");
       builder.subject("myRealm/myLogin");
       builder.claim("name", "Firstname Lastname");
       builder.claim("lang", "en");
       builder.claim("roles", roles);
       builder.claim("realm","myRealm");


       JWT jwt = new PlainJWT(builder.build());

       AuthenticationDetail authenticationDetail = tokenConverter.convert(jwt);


       Assert.assertEquals("myRealm", authenticationDetail.getRealm());
       Assert.assertEquals("myRealm/myLogin", authenticationDetail.getSubject());
       Assert.assertEquals("myLogin", authenticationDetail.getLoginName());
       Assert.assertEquals("myRealm/myLogin", authenticationDetail.getPrincipal());

       Assert.assertEquals("Firstname Lastname", authenticationDetail.getName());


       Assert.assertEquals(3, authenticationDetail.getAuthorities().size());

       authenticationDetail.hasRole("ROLE_user");
       authenticationDetail.hasRole("ROLE_test");
       authenticationDetail.hasRole("ROLE_admin");
   }
}
