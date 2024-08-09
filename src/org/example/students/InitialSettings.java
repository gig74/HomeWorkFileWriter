package org.example.students;

import java.io.File;

public final class InitialSettings {
    private static final File BASE_DIR = new File("C:\\TEMP");
    public static final int  MAX_CYCLE_FIND_DIRECTORY_COUNT = 256 ; // ограничение для цикла на всякий случай
    public static final int  MAX_CYCLE_FIND_FILE_REСURSION_COUNT = 256 ; // ограничение для рекурсий на всякий случай
    public static File getBaseDir() {
       return BASE_DIR;
    }
}
