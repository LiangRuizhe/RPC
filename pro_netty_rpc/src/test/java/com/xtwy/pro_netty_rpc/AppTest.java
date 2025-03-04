package com.xtwy.pro_netty_rpc;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest   extends TestCase
{

    public void testApp()
    {
    	System.out.println("Java version: " + System.getProperty("java.version"));
		System.out.println("Java runtime: " + System.getProperty("java.runtime.name"));
		System.out.println("Java vendor: " + System.getProperty("java.vendor"));

        assertTrue( true );
    }
}
