package com.fizzpod.ibroadcast

import org.apache.groovy.contracts.*
import org.tinylog.*

public class Main {

    public static void main(String[] args) {
        def options = CLI.parse(args)

        if(options != null) {
            try {
                if(options.s || options.c || options.z) {
                    SyncOperation.run(options)
                } else {
                    CLI.usage()
                }
            } catch (AssertionViolation e) {
                println(e.message)
            }
        }
    }

}
