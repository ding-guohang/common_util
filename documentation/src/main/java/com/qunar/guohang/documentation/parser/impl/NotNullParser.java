package com.qunar.guohang.documentation.parser.impl;

/**
 * @author guohang.ding on 16-10-11
 */
@SuppressWarnings("all")
public class NotNullParser {

    public static void main(String[] args) {
        v1(); //true
        v2(); //false
        v3(); //false
        v4(); //false
        v5(); //true
    }

    private static void v1() { //true
        String s1 = new StringBuilder("a").append("a").toString();
        s1.intern();
        String s2 = "aa";
        System.out.println(s1 == s2);
    }

    private static void v2() { //false
        String s3 = new String("b") + new String("b"); //such as new StringBuilder("b").append("b").toString
        String s4 = "bb";
        s3.intern();
        System.out.println(s3 == s4);
    }

    private static void v3() {// false
        String s5 = new String("cc");
        s5.intern();
        String s6 = "cc";
        System.out.println(s5 == s6);
    }

    private static void v4() {// false
        String s7 = new String("dd");
        String s8 = "dd";
        s7.intern();
        System.out.println(s7 == s8);
    }

    private static void v5() {// true in jdk7, false in jdk6
        String s = new StringBuilder("f").append("f").toString();
        System.out.println(s == s.intern());
    }
}
