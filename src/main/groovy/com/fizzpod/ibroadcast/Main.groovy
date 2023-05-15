package com.fizzpod.ibroadcast

import org.apache.groovy.contracts.*
import org.tinylog.*

public class Main {

    public static void main(String[] args) {
        def options = CLI.parse(args)
        String mode = ""
        if(options.s) {
            mode = "SYNC"
        }
        if(options.c) {
            mode = "COPY"
        }
        if(options.d) {
            mode = mode + "(dry)"
        } 

        ThreadContext.put("mode", mode);
        if(options != null) {
            try {
                if(options.s || options.c) {
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
