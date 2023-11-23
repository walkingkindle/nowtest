package com.seleniumtest;


public class test {
    public static void main(String[] args) {
        print("Marija",1,6.5);
    }
    public static <T> void print(T... args) {
        for(T arg : args) {
            System.out.println(arg);
        }
    }
}
