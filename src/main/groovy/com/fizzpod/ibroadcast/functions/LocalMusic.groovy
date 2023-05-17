package com.fizzpod.ibroadcast.functions

import static org.tinylog.Logger.*
import static groovy.io.FileType.*
import static groovy.io.FileVisitResult.*

public class LocalMusic {

    private static def musicFilesFilter = ~/.*\.(mp3|m4a)$/
    
    public static def scan(File source, Closure processor) {
        source.traverse([type: FILES, visit: processor, nameFilter: musicFilesFilter])
    }

}