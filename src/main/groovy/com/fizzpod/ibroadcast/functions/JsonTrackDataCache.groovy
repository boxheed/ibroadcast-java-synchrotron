package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import org.mapdb.DB
import org.mapdb.DBMaker
import java.util.concurrent.ConcurrentMap
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

public class JsonTrackDataCache {

    private static final String DB_NAME = "ibsync_tracks.json"

    private def tracks = [:]
    private def tracksFile = null
    private JsonSlurper jsonSlurper = new JsonSlurper()

    public def open(File dbFolder) {
        debug("Initialising data cache in {} with name {}", dbFolder, DB_NAME)
        tracksFile = new File(dbFolder, DB_NAME)
        if(tracksFile.exists()) {
            tracks = jsonSlurper.parseText(tracksFile.getText('UTF-8'))
        }
    }



    public def get(String path) {
        def data = this.tracks.get(path)
        if(data != null) {
            debug("Cache Hit for {}", path)
            debug("Cache data {}", data)
            data.file = new File(path)
        } else {
            debug("Cache Miss for {}", path)
        }
        return data;
    }

    public def put(def data) {
        debug("Storing {}:{}", data.path, data)
        this.tracks.put(data.path, data)
    }

    public void close() {
        def tracksJson = new groovy.json.JsonBuilder(tracks).toPrettyString()
        tracksFile.write(tracksJson)
    }

}