package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import com.google.common.hash.*
import com.google.common.io.*

public class TrackData {

    public static final def read(File track) {
        info("Reading file {}", track.getName())
        def data = [:]
        data.put("path", track.getAbsolutePath())
        data.put("modified", track.lastModified())
        data.tags = TrackTagReader.parse(track)

        //data.key = data.tags[].number + track.title + track.album + track.artist
        String fileChecksum = Files.hash(track, Hashing.md5()).toString();
        data.put("checksum", fileChecksum)
        info("Track Data: {}", data)

        return data;
    }

}