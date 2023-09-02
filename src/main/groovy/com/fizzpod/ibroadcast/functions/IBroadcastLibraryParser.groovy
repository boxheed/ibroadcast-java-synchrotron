package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import groovy.contracts.*

/*
    "map": {
                "rating": 14,
                "uid": 13,
                "artists_additional": 20,
                "enid": 8,
                "artists_additional_map": {
                    "phrase": 1,
                    "type": 2,
                    "artist_id": 0
                },
                "genre": 3,
                "artwork_id": 6,
                "length": 4,
                "album_id": 5,
                "uploaded_on": 9,
                "size": 11,
                "artist_id": 7,
                "icatid": 22,
                "replay_gain": 18,
                "year": 1,
                "trashed": 10,
                "genres_additional": 21,
                "uploaded_time": 19,
                "title": 2,
                "plays": 15,
                "track": 0,
                "type": 17,
                "path": 12,
                "file": 16
            }

            "title"
            "album_id"
            "artist_id"
            "track"

            "325853478": [
                10,
                2001,
                "Chorus And The Ring",
                "Rock",
                272,
                148172318,
                11631829,
                10735370,
                0,
                "2023-04-29",
                false,
                9006047,
                "mnt/ex2/music/iTunes/iTunes Music/Music/R.E.M_/Reveal",
                "",
                0,
                0,
                "/128/01f/33f/251648936",
                "audio/mp4",
                "-6.2",
                "22:47:49",
                [],
                [],
                "8prZ76v5"
            ],

    "148172318": [
        "Reveal",
        [
            325853163,
            325853187,
            325853212,
            325853241,
            325853261,
            325853285,
            325853314,
            325853344,
            325853370,
            325853403,
            325853440,
            325853478
        ],
        10735370,
        false,
        0,
        1,
        2001,
        [],
        "7JauNlNY"
    ]
    */

public class IBroadcastLibraryParser {

    private TrackKey trackKey
    private File inputFolder

    public IBroadcastLibraryParser(File inputFolder) {
        this.inputFolder = inputFolder
        trackKey = new TrackKey(inputFolder)
    }

    public def parseTracks(def library) {

        def albums = parseAlbums(library.albums)
        debug("Albums: {}", albums)
        def artists = parseArtists(library.artists)
        debug("Artists: {}", artists)
        def tracks = parseTracks(library.tracks, albums, artists)
        return tracks
    }

    public def parseTracks(def data, albums, artists) {

        def trackMap = data.map
        def trackTitleIndex = trackMap.title
        def trackAlbumIdIndex = trackMap.album_id
        def trackArtistIdIndex = trackMap.artist_id
        def trackNumberIndex = trackMap.track
        def pathIndex = trackMap.path
        def tracks = [:]
        data.each { key, val ->
            if(key != "map") {
                def track = [:]
                track.id = key
                track.title = val[trackTitleIndex]
                track.albumId = val[trackAlbumIdIndex] as Integer
                track.album = albums[track.albumId]
                track.artistId = val[trackArtistIdIndex] as Integer
                track.artist = artists[track.artistId]
                track.number = val[trackNumberIndex] as Integer
                track.folder = val[pathIndex]
                track.relativePath = PathUtils.cut(inputFolder, track.folder)
                track.key = trackKey.generateKey(track)
                debug("Track: {}", track)
                tracks.put(track.key, track)
            }
        }
        return tracks

    }

    public def parseAlbums(def data) {
        debug("Parsing Albums")
        //parse the albums
        def albumMap = data.map
        def albums = [:]
        def albumNameIndex = albumMap.name

        data.each { key, val ->
            if(key != "map") {
                albums.put(key as Integer, val[albumNameIndex])
            }
        }
        return albums
    }

    public def parseArtists(def data) {
        
        def artistMap = data.map
        def artists = [:]
        def artistNameIndex = artistMap.name

        data.each { key, val ->
            if(key != "map") {
                artists.put(key as Integer, val[artistNameIndex])
            }
        }
        return artists

    }

}