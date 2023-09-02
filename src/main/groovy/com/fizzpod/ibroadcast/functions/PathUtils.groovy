package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils

public class PathUtils {

    static def cut(String base, String full) {
        String basePath = normalize(base)
        String fullPath = normalize(full)
        String cutPath = fullPath
        if(fullPath.startsWith(basePath)) {
            cutPath = StringUtils.removeStart(fullPath, basePath)
        } else {
            int overlap = calculateOverlap(basePath, fullPath)
            if(overlap > 0) {
                cutPath = StringUtils.substring(fullPath, overlap)
            }
        }
        cutPath = StringUtils.removeStart(cutPath, "/")
        return cutPath
    }

    static def cut(File root, String path) {
        cut(root.getAbsolutePath(), path)
    }

    static def cut(String root, File path) {
        cut(root, path.getAbsolutePath())
    }

    static def cut(File root, File path) {
        cut(root.getAbsolutePath(), path.getAbsolutePath())
    }

    static String normalize(String path) {
        String normalizedPath = FilenameUtils.normalizeNoEndSeparator(path, true)
        if(normalizedPath == null) {
            normalizedPath = FilenameUtils.separatorsToUnix(path)
            normalizedPath = StringUtils.removeEnd(normalizedPath, "/")
        }
        normalizedPath = StringUtils.removeStart(normalizedPath, FilenameUtils.getPrefix(normalizedPath))
        return normalizedPath
    }

    static int calculateOverlap(String path1, String path2) {
        int overlap = 0;
        String[] path1Parts = StringUtils.split(path1, '/')
        String path = ""
        for(int i = path1Parts.length; i >= 0; i--) {
            path = path1Parts[i - 1] + path
            if(path2.startsWith(path)) {
                overlap = path.length()
            }
            path = "/" + path
        }
        return overlap
    }

}