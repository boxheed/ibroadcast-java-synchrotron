package com.fizzpod.ibroadcast;

import static org.tinylog.Logger.*;
import com.fizzpod.ibroadcast.functions.*;


public class SyncOperation {

    public static def run(def options) {
        info("Starting sync operation")
        createPipeline(options)
    }

    private static def createPipeline(def options) {
        def credentials = IBroadcast.auth(options.u, options.p)
        def tracks = IBroadcast.listTracks(credentials)
        def albums = IBroadcast.listAlbums(credentials)
        def checksums = IBroadcast.getMusicChecksums(credentials)
        info(checksums)
        LocalMusic.scan(options.i, { f ->
            info("Processing {} ", f);
            def data = TrackData.read(f);
            if(checksums.contains(data.checksum)) {
                info("Skipping {} as it's already upoaded", f)
            } else {
                info("Uploading {}", f)
            }
        });
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