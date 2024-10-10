package com.pdsc.util;

/**
 *
 * @author williansantos
 */
public class Logging {
    
   private static final String MAIN_TAG =  "Campus Resolve: ";
    
    public static void d(String tag, String message) {
        System.out.println(MAIN_TAG + tag +": " + message);
    }
}
