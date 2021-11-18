package com.corp;


import java.util.concurrent.atomic.AtomicBoolean;

public class EnvironmentDetector {

    static final boolean JNKENV = detectJenkins();

    public static void main(String[] args) {
        detectJenkins();
    }

    private static boolean detectJenkins() {
        AtomicBoolean res = new AtomicBoolean(false);
        System.getenv().forEach((k, v) -> {
            if(k.toLowerCase().contains("jenkins")){
                res.set(true);
            }
        });
        return res.get();
    }
}

