package com.fizzpod.ibroadcast.functions

import org.mapdb.DB
import java.util.concurrent.ConcurrentMap
import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.fizzpod.ibroadcast.MusicFile

public class MusicCache {

    private DB db;
    private ConcurrentMap map;
    private JsonSlurper jsonSlurper = new JsonSlurper()

    public def open() {
        this.db = DBMaker.fileDB("file.db").make();
        map = db.hashMap("map").createOrOpen();
    }

    public MusicFile get(String path) {
        def data = map.get(path)
        MusicFile musicFile = null;
        if(data != null) {
            def map = jsonSlurper.parseText(data)
            musicFile = new MusicFile(map)
        }
        return musicFile;
    }

    public def put(MusicFile musicFile) {
        def json = JsonOutput.toJson(musicFile)
        map.put(musicFile.path, json)
    }

    public void close() {
        db.close();
    }

}