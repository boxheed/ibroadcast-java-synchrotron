package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import com.google.common.hash.*
import com.google.common.io.*

public class TrackData {

    public static final def read(File trackFile) {
        info("reading {}", trackFile)
        def track = [:]
        track = track + TrackTagReader.parse(trackFile)
        track.file = trackFile
        track.modified = trackFile.lastModified()
        String fileChecksum = Files.hash(trackFile, Hashing.md5()).toString();
        track.checksum = fileChecksum

        //TODO should create this with a KeyMaker
        track.key = track.album + " " + track.artist + " " + track.number + " " + track.title
        info("track {}", track)
        return track;
    }

}