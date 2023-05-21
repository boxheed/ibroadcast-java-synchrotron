package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import org.mapdb.DB
import org.mapdb.DBMaker
import java.util.concurrent.ConcurrentMap
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

public class TrackDataCache {

    private static final String DB_NAME = "ibsync_tracks.db"

    private static final String DB_TRACKS_NAME = "tracks"

    private DB db;
    private ConcurrentMap map;
    private JsonSlurper jsonSlurper = new JsonSlurper()

    public def open(File dbFolder) {
        info("Initialising the data cache in {} with name {}", dbFolder, DB_NAME)
        this.db = DBMaker.fileDB(new File(dbFolder, DB_NAME))
            .transactionEnable()
            .closeOnJvmShutdown()
            .make()
        info("Opening cache with name {}", DB_TRACKS_NAME)
        this.map = this.db.hashMap(DB_TRACKS_NAME).createOrOpen();
    }

    public def get(String path) {
        def data = this.map.get(path)
        if(data != null) {
            info("Cache Hit for {}", path)
            info("Cache data {}", data)
            data = jsonSlurper.parseText(data)
            data.file = new File(path)
        } else {
            info("Cache Miss for {}", path)
        }
        return data;
    }

    public def put(def data) {
        info("Storing {}:{}", data.path, data)
        def json = JsonOutput.toJson(data)
        this.map.put(data.path, json)
        db.commit()
    }

    public void close() {
        db.close();
    }

}