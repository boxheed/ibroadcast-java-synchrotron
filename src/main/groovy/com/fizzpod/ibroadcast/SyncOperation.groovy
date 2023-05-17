package com.fizzpod.ibroadcast;

import static org.tinylog.Logger.*;

import org.tinylog.*
import com.fizzpod.ibroadcast.functions.*;


public class SyncOperation {

    public static def run(def options) {
        if(options.d) {
            ThreadContext.put("dry", "(dry)")
        }
        ThreadContext.put("mode", "init")
        info("starting")
        def context = [
            credentials: IBroadcast.auth(options.u, options.p),
            options: options,
            libraries: [
                local: [:],
                remote: [:],
                checksums: []
            ],
            stats:  [uploaded: 0, skipped: 0, cleaned: 0]
        ]
        context.libraries.remote = IBroadcast.library(context.credentials)
        context.libraries.checksums = IBroadcast.checksums(context.credentials)
        ThreadContext.put("mode", "scan")

        LocalMusic.scan(options.i, { f ->
            info("{} ", f);
            def data = TrackData.read(f);
            context.libraries.local.put(data.key, data)
            context.libraries.remote.remove(data.key)
        })

        if(options.s) {
            ThreadContext.put("mode", "send")
            context.libraries.local.each { key, track ->
                if(context.libraries.checksums.contains(track.checksum)) {
                    info("skipping {}", track.file)
                    context.stats.skipped++
                } else {
                    info("uploading {}", track.file)
                    if(!options.d) {
                        IBroadcast.upload(context.credentials, track.file)
                    }
                    context.stats.uploaded++
                }
            }
        }

        if(options.c) {
            ThreadContext.put("mode", "clean");
            context.libraries.remote.each { key, value ->
                info("cleaning {}", key)
                if(!options.d) {
                    IBroadcast.trash(credentials, value.id)
                }
                context.stats.cleaned++
            }
        }
        info("finished {}", context.stats)
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

    public static def setLogMode(def options) {

    }
}