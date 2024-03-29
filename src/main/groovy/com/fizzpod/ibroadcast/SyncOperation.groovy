package com.fizzpod.ibroadcast;

import org.apache.commons.io.FilenameUtils
import static org.tinylog.Logger.*;

import org.tinylog.*
import com.fizzpod.ibroadcast.functions.*;

import groovy.json.JsonOutput


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
        context.libraries.remote = IBroadcast.library(context.credentials, options.i)
        context.libraries.checksums = IBroadcast.checksums(context.credentials)

        def remoteLibrary = JsonOutput.toJson(context.libraries.remote)
        
        ThreadContext.put("mode", "scan")

        TrackData.init(options.i, new File(options.b), options.f)

        File inputFolder = new File(options.i, options.g)
        LocalMusic.scan(inputFolder, { f ->
            info("{} ", f)
            def track = TrackData.read(f)
            context.libraries.local.put(track.key, track)
//            context.libraries.remote.remove(data.key)
        })
        if(options.z) {
            info("Dumping data to files")
            def remoteLibraryFile = new File(options.b, 'remote_library.json')
            info("Writing {}", remoteLibraryFile)
            remoteLibraryFile.write(remoteLibrary)

            def localLibrary = JsonOutput.toJson(context.libraries.local)
            def localLibraryFile = new File(options.b, 'local_library.json')
            info("Writing {}", localLibraryFile)
            localLibraryFile.write(localLibrary)

            def prunedRemoteLibrary = JsonOutput.toJson(context.libraries.remote)
            def prunedRemoteLibraryFile = new File(options.b, 'pruned_remote_library.json')
            info("Writing {}", prunedRemoteLibraryFile)
            prunedRemoteLibraryFile.write(prunedRemoteLibrary)

            def checksums = JsonOutput.toJson(context.libraries.checksums)
            def checksumsFile = new File(options.b, 'checksums.json')
            info("Writing {}", checksumsFile)
            checksumsFile.write(checksums)
        }

        TrackData.close()
        if(options.s) {
            ThreadContext.put("mode", "send")
            context.libraries.local.each { key, track ->
                boolean upload = false
                if(!context.libraries.remote.containsKey(key)) {
                    upload = true
                } else if(!context.libraries.checksums.contains(track.checksum)) {
                    upload = true
                }

                if(!upload) {
                    info("skipping {}", track.file)
                    context.stats.skipped++
                } else {
                    info("uploading {}", track.file)
                    if(!options.d) {
                        IBroadcast.upload(context.credentials, track)
                    }
                    context.stats.uploaded++
                }
            }
        }

        if(options.c) {
            ThreadContext.put("mode", "clean")
            def trashTracks = []
            context.libraries.remote.each { key, value ->
                if(!context.libraries.local.containsKey(key)) {
                    info("trashing {}", key)
                    trashTracks << (value.id as Integer)
                    context.stats.cleaned++
                }
            }
            if(!options.d && trashTracks.size() > 0) {
                IBroadcast.trash(context.credentials, trashTracks)
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