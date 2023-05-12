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
        def tags = [:];
        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            AudioHeader audioHeader = f.getAudioHeader();
            String artist = tag.getFirst(FieldKey.ARTIST);
            tags.put(FieldKey.ARTIST.toString(), artist);
            String album = tag.getFirst(FieldKey.ALBUM);
            tags.put(FieldKey.ALBUM.toString(), album);
            String title = tag.getFirst(FieldKey.TITLE);
            tags.put(FieldKey.TITLE.toString(), title);
            String composer = tag.getFirst(FieldKey.COMPOSER);
            tags.put(FieldKey.COMPOSER.toString(), composer);
            String genre = tag.getFirst(FieldKey.GENRE);
            tags.put(FieldKey.GENRE.toString(), genre);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException e) {
            error("Could not read file {}, received error {}", file, e.getMessage())
        }
        return tags;
    }

}
