package com.fizzpod.ibroadcast.functions

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils
import java.nio.file.Path


public class TrackKey {

    private Path inputPath = null

    public TrackKey() {
    }

    public TrackKey(Path inputPath) {
        this.inputPath = inputPath;
    }

    public TrackKey(File inputFolder) {
        this.inputPath = inputFolder.toPath();
    }

    public def generateKey(def track) {
        def folder = getFolderKey(track)
        def number = getTrackNumber(track)
        def title = getTrackTitle(track)
        return "[" + folder + "] [" + number + "] [" + title + "]"
    }

    public def getFolderKey(def track) {
        def folder = track.folder
        if(inputPath != null) {
            folder = inputPath.relativize(track.file.toPath()).toString()
        }
        folder = FilenameUtils.separatorsToUnix(folder)
        return folder.replace("/", "")
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