package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*

import java.io.File
import java.io.IOException

import org.jaudiotagger.audio.AudioFile
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.AudioHeader
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.TagException

public class TrackTagReader {

    public static final def parse(def file) {
        def tags = [:]
        try {
            AudioFile f = AudioFileIO.read(file)
            Tag tag = f.getTag()
            AudioHeader audioHeader = f.getAudioHeader()
            String artist = tag.getFirst(FieldKey.ARTIST)
            tags.artist = artist
            String album = tag.getFirst(FieldKey.ALBUM)
            tags.album = album
            String title = tag.getFirst(FieldKey.TITLE)
            tags.title=  title
            String composer = tag.getFirst(FieldKey.COMPOSER)
            tags.composer = composer
            String genre = tag.getFirst(FieldKey.GENRE)
            tags.genre = genre
            String track = tag.getFirst(FieldKey.TRACK)
            tags.number = track
            String trackTotal = tag.getFirst(FieldKey.TRACK_TOTAL)
            tags.trackTotal = trackTotal
            String disc = tag.getFirst(FieldKey.DISC_NO)
            tags.disc = disc
            String discTotal = tag.getFirst(FieldKey.DISC_TOTAL)
            tags.discTotal = discTotal;
            
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException e) {
            error("Could not read file {}, received error {}", file, e.getMessage())
        }
        return tags;
    }

}
