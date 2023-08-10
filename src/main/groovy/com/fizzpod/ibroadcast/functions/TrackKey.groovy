package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils


public class TrackKey {

    public static def generateKey(def track) {
        def folder = track.folder
        folder = FilenameUtils.separatorsToUnix(folder) 
        folder = folder.replace("/", "")
        track.key = "[" + folder + "] [" + track.number + "] [" + track.title + "]"
    }

}