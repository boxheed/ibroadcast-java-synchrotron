package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import com.google.common.hash.*
import com.google.common.io.*

public class TrackData {

    private static final TrackDataCache cache = new TrackDataCache()

    public static final def init(File dataFolder) {
        info("Initialising cache in folder {}", dataFolder)
        cache.open(dataFolder)
    }

    public static final def close(File dataFolder) {
        cache.close()
    }

    public static final def read(File trackFile) {
        info("reading {}", trackFile)
        def track = cache.get(trackFile.getAbsolutePath())
        if(track != null && trackFile.lastModified() == track.modified) {
            debug("Loaded track from cache {}", track)
        } else {
            debug("Parsing track")
            track = [:]
            track = track + TrackTagReader.parse(trackFile)
            track.file = trackFile
            track.path = trackFile.getAbsolutePath()
            track.modified = trackFile.lastModified()
            String fileChecksum = Files.hash(trackFile, Hashing.md5()).toString();
            track.checksum = fileChecksum

            //TODO should create this with a KeyMaker
            track.key = track.album + " " + track.artist + " " + track.number + " " + track.title
            debug("track {}", track)
            cache.put(track)
        }
        return track;
    }

}