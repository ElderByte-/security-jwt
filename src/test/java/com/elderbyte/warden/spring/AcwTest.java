package com.elderbyte.warden.spring;

import com.elderbyte.warden.spring.local.auth.Acw;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.access.AccessDeniedException;

/**
 * Tests for the access control expressions
 */
public class AcwTest {


    @Test
    public void testBasic(){
        Assert.assertFalse(Acw.dynamicEval(() -> false).evalPermission());
        Assert.assertTrue(Acw.dynamicEval(() -> true).evalPermission());
    }

    @Test
    public void testDenyAndGrant(){
        Assert.assertTrue(Acw.grant().evalPermission());
        Assert.assertFalse(Acw.deny().evalPermission());
    }


    @Test(expected = AccessDeniedException.class)
    public void testBasicEnforceFalse(){
        Acw.deny().enforce();
    }

    @Test
    public void testBasicEnforceTrue(){
        Acw.grant().enforce();
    }



    @Test
    public void testBasicLogicOr(){
        Assert.assertTrue(Acw.requireAny(Acw.deny(), Acw.grant()).evalPermission());
        Assert.assertTrue(Acw.requireAny(Acw.grant(), Acw.deny()).evalPermission());

        Assert.assertTrue(Acw.requireAny(Acw.grant(), Acw.grant()).evalPermission());
        Assert.assertFalse(Acw.requireAny(Acw.deny(), Acw.deny()).evalPermission());
    }

    @Test
    public void testBasicLogicAnd(){
        Assert.assertFalse(Acw.requireAll(Acw.deny(), Acw.grant()).evalPermission());
        Assert.assertFalse(Acw.requireAll(Acw.grant(), Acw.deny()).evalPermission());

        Assert.assertTrue(Acw.requireAll(Acw.grant(), Acw.grant()).evalPermission());
        Assert.assertFalse(Acw.requireAll(Acw.deny(), Acw.deny()).evalPermission());
    }



    @Test
    public void testComplex1(){


        Assert.assertTrue(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.deny()),
                Acw.requireAny(Acw.deny(), Acw.grant())
            ).evalPermission()
        );

        Assert.assertTrue(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.grant()),
                Acw.requireAny(Acw.deny(), Acw.grant())
            ).evalPermission()
        );

        Assert.assertFalse(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.deny()),
                Acw.requireAny(Acw.deny(), Acw.deny())
            ).evalPermission()
        );

        Assert.assertFalse(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.deny()),
                Acw.requireAny(Acw.deny(), Acw.deny())
            ).evalPermission()
        );


    }


    @Test
    public void testComplex2(){


        Assert.assertTrue(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.deny()),
                Acw.grant()
            ).evalPermission()
        );

        Assert.assertTrue(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.grant()),
                Acw.deny()
            ).evalPermission()
        );

        Assert.assertFalse(
            Acw.requireAny(
                Acw.requireAll(Acw.grant(), Acw.deny()),
                Acw.deny()
            ).evalPermission()
        );

        Assert.assertFalse(
            Acw.requireAny(
                Acw.requireAll(Acw.deny(), Acw.deny()),
                Acw.deny()
            ).evalPermission()
        );


    }



}
