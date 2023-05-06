package com.fizzpod.ibroadcast;

import groovy.cli.picocli.CliBuilder;

public class CLI {
    private static final def parser = { ->
        def cli = new CliBuilder(usage:'ibsync');
        cli.h(longOpt:'help', 'Print this message')
        cli.i(type:File, longOpt:'input', args:1, argName:'inputFolder', 'The folder containing the music library to synchronise')
        cli.d(longOpt:'dry', args:1, argName:'dryRun', 'Performs a dry-run, does not make any changes')
        cli.s(longOpt:'sync', 'Copies missing music from local to iBroadcast and removes music on iBroadcast that is not in the local folder')
        cli.c(longOpt:'copy', 'Only copies missing music from local to iBroadcast, does not delete music already present')
        cli.u(longOpt:'user', args:1, argName:'user', 'Your ibroadcast username')
        cli.p(longOpt:'password', args:1, argName:'password', 'Your ibroadcast password')
        return cli

    }.call()

    public static def parse = { String[] args ->
        return CLI.parser.parse(args)
    }

    public static def usage = {
        CLI.parser.usage()
    }

}