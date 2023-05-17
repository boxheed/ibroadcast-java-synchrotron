package com.fizzpod.ibroadcast;

import groovy.cli.picocli.CliBuilder;

public class CLI {
    private static final def parser = { ->
        def cli = new CliBuilder(usage:'ibsync');
        cli.h(longOpt:'help', 'Print this message')
        cli.i(type:File, longOpt:'input', args:1, argName:'inputFolder', 'The folder containing the music library to synchronise', required: true)
        cli.s(longOpt:'send', 'Sends any missing or updated music from the local filesystem to iBroadcast')
        cli.c(longOpt:'clean', 'Moves tracks on iBroadcast to the trash folder that are not stored locally')
        cli.u(longOpt:'user', args:1, argName:'user', 'Your ibroadcast username')
        cli.p(longOpt:'password', args:1, argName:'password', 'Your ibroadcast password')
        cli.d(longOpt:'dry', 'Performs a dry-run, does not make any changes')
        return cli

    }.call()

    public static def parse = { String[] args ->
        return CLI.parser.parse(args)
    }

    public static def usage = {
        CLI.parser.usage()
    }

}