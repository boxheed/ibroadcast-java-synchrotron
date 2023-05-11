package com.fizzpod.ibroadcast

import org.apache.groovy.contracts.*

public class Main {

    public static void main(String[] args) {
        def options = CLI.parse(args)
        if(options != null) {
            try {
                if(options.c) {
                    CopyOperation.run(options)
                } else if(options.s) {
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
