package com.fizzpod.ibroadcast.functions

import org.mapdb.DB
import java.util.concurrent.ConcurrentMap
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

public class MusicCache {

    private DB db;
    private ConcurrentMap map;
    private JsonSlurper jsonSlurper = new JsonSlurper()

    public def open() {
        this.db = DBMaker.fileDB("file.db").make();
        map = db.hashMap("map").createOrOpen();
    }

    public def get(String path) {
        def data = map.get(path)
        def musicFile = null;
        if(data != null) {
            data = jsonSlurper.parseText(data)
        }
        return data;
    }

    public def put(def musicFile) {
        def json = JsonOutput.toJson(musicFile)
        map.put(musicFile.path, json)
    }

    public void close() {
        db.close();
    }

}