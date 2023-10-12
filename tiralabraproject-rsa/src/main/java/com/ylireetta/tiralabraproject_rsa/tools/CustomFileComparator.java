package com.ylireetta.tiralabraproject_rsa.tools;

import java.io.File;
import java.util.Comparator;

public class CustomFileComparator implements Comparator<File> {
    @Override
    public int compare(File first, File second) {
        String firstUserName = getNameStart(first.getName());
        String secondUserName = getNameStart(second.getName());

        if (firstUserName.startsWith(secondUserName)) {
            // The first element begins with the second, so the second one should be put first.
            // e.g. filename5 begins with filename, which means that filename is first.
            // Note that the comparison is done in the order first.compareTo(second), so return 1 and change their order in the list.
            return 1;
        } else if (secondUserName.startsWith(firstUserName)) {
            return -1;
        }

        // Compare lexicographically.
        return firstUserName.compareTo(secondUserName);
    }

    /**
     * Get the beginning of the file name if it contains underscores.
     * @param fileName The complete file name to check.
     * @return The beginning of the file name up until the first underscore. If no underscores are found, return the original file name.
     */
    public static String getNameStart(String fileName) {
        int underScoreIndex = fileName.indexOf("_");

        if (underScoreIndex != -1) {
            return fileName.substring(0, underScoreIndex);
        } else {
            return fileName;
        }
    }
}
