package com.fizzpod.ibroadcast;

import static org.tinylog.Logger.*;
import com.fizzpod.ibroadcast.functions.*;


public class SyncOperation {

    public static def run(def options) {
        def stats = [upload:0, skip:0, trash: 0]
        info("Starting operation")
        def credentials = IBroadcast.auth(options.u, options.p)
        def ibroadcastLibrary = IBroadcast.library(credentials)
        def checksums = IBroadcast.getMusicChecksums(credentials)
        def localMusicData = [:]
        LocalMusic.scan(options.i, { f ->
            info("Processing {} ", f);
            def data = TrackData.read(f);
            localMusicData.put(data.key, data)
            if(options.s || options.c) {
                if(checksums.contains(data.checksum)) {
                    info("Skipping {}", f)
                    stats.skip++
                } else {
                    info("Uploading {}", f)
                    stats.upload++
                    if(!options.d) {
                        IBroadcast.upload(credentials, f)
                    }
                }
            }
            ibroadcastLibrary.remove(data.key)
        })
        if(options.s) {
            ibroadcastLibrary.each { key, value ->
                info("Trashing {}", key)
                stats.trash++
                if(!options.d) {
                    IBroadcast.trash(credentials, value.id)
                }
            }
        }
        info("Finished {}", stats)
        //COPY
        //authenticate with iBroadcast & get token
        //get checksums of existing files (single call)
        //iterate over the local files
        //for each file 
            //get file metadata
                //read lastmodified date
                //get metadata from cache
                //if dates are different reload metadata
                //save back to cache
            //find file on iBroadcast manifest using checksum
            //if file doesn't exist on ibroadcast
                //upload file
        //SYNC
        //re-retrieve manifest from iBroadcast
        //iterate over the manifest
        //for each entry
            //get checksum
            //if checksum does not exist locally
            //send to trash on ibroadcast
        //finish
        //TIDY-UP
        //clean up database

    }
}