package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import com.google.common.hash.*
import com.google.common.io.*

public class TrackData {

    private static def cache

    private static def trackKey

    private static def inputFolderPath

    public static final def init(File inputFolder, File dataFolder, String format) {
        info("Initialising cache in folder {}", dataFolder)
        if(format == "binary") {
            cache = new TrackDataCache()
        } else {
            cache = new JsonTrackDataCache()
        }
        cache.open(dataFolder)
        trackKey = new TrackKey(inputFolder)
        inputFolderPath = inputFolder.getAbsolutePath()
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
            track.folder = trackFile.getParentFile().getAbsolutePath()
            track.relativePath = PathUtils.cut(inputFolderPath, track.folder)
            track.path = trackFile.getAbsolutePath()
            track.modified = trackFile.lastModified()
            String fileChecksum = Files.hash(trackFile, Hashing.md5()).toString();
            track.checksum = fileChecksum

            track.key = trackKey.generateKey(track)
            debug("track {}", track)
            cache.put(track)
        }
        track.relativePath = PathUtils.cut(inputFolderPath, track.folder)
        track.key = trackKey.generateKey(track)
        return track;
    }

}