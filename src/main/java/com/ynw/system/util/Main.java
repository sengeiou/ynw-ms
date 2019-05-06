package com.ynw.system.util;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println(UUIDUtil.getEUUID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
