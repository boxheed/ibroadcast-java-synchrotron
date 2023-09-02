package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import java.nio.file.Path


public class TrackKey {

    private File inputPath = null

    public TrackKey(File inputFolder) {
        this.inputPath = inputFolder;
    }

    public def generateKey(def track) {
        def folder = getFolderKey(track)
        def number = getTrackNumber(track)
        def title = getTrackTitle(track)
        return "[" + folder + "] [" + number + "] [" + title + "]"
    }

    public def getFolderKey(def track) {
        return track.relativePath
    }

    public def getTrackNumber(def track) {
        def number = track.number
        if(track.containsKey("disc") && track.containsKey("discTotal") && track.discTotal != "1") {
            number = track.disc + StringUtils.leftPad(number, 2, '0')
        }
        return number
    }

    public def getTrackTitle(def track) {
        return track.title
    }


}