package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils


public class TrackKey {

    public static def generateKey(def track) {
        def folder = getFolderKey(track)
        def number = getTrackNumber(track)
        def title = getTrackTitle(track)
        return "[" + folder + "] [" + number + "] [" + title + "]"
    }

    public static def getFolderKey(def track) {
        def folder = FilenameUtils.separatorsToUnix(track.folder) 
        return folder.replace("/", "_")
    }

    public static def getTrackNumber(def track) {
        def number = track.number
        if(track.containsKey("disc") && track.containsKey("discTotal") && track.discTotal != "1") {
            number = track.disc + number
        }
        return number
    }

    public static def getTrackTitle(def track) {
        return track.title
    }


}