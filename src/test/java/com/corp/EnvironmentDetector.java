package com.corp;

public class EnvironmentDetector {

    static final boolean JNKENV = detectJenkins();

    private static boolean detectJenkins() {
        return System.getenv().toString().toLowerCase().contains("jenkins");
    }

}

