package com.fizzpod.ibroadcast;

public class Main {

    public static void main(String[] args) {
        def options = CLI.parse(args)
        if(options.c) {
            CopyOperation.run(options)
        } else if(options.s) {
            SyncOperation.run(options)
        } else {
            CLI.usage()
        }
    }

}
