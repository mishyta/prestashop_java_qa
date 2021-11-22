package com.corp;

public class EnvironmentDetector {

    static final boolean JNKENV = detectJenkins();

    /**
     * @return return true if environment is contains "jenkins"
     */
    private static boolean detectJenkins() {
        return System.getenv()
                .toString()
                .toLowerCase()
                .contains("jenkins");
    }

}

