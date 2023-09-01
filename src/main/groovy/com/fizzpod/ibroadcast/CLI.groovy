package com.fizzpod.ibroadcast;

import groovy.cli.picocli.CliBuilder;

public class CLI {
    private static final def parser = { ->
        def cli = new CliBuilder(usage:'ibsync');
        cli.h(longOpt:'help', 'Print this message')
        cli.i(type:File, longOpt:'input', args:1, argName:'inputFolder', 'The folder containing the music library to synchronise', required: true)
        cli.s(longOpt:'send', 'Sends any missing or updated music from the local filesystem to iBroadcast')
        cli.c(longOpt:'clean', 'Moves tracks on iBroadcast to the trash folder that are not stored locally')
        cli.p(longOpt:'password', args:1, argName:'password', 'Your ibroadcast password')
        cli.u(longOpt:'user', args:1, argName:'user', 'Your ibroadcast username')
        cli.d(longOpt:'dry', 'Performs a dry-run, does not make any changes')
        cli.b(longOpt: 'database', 
            args:1, 
            argName:'database', 
            'Directory for storing the database file', 
            defaultValue: System.getProperty("java.io.tmpdir"))
        cli.f(longOpt: 'format',
            args:1,
            argName:'format',
            'Format for the database, values are either binary or json',
            defaultValue: "json")
        cli.g(type:String,
            longOpt: 'filter',
            args:1,
            argName:'filter',
            //TODO improve this filter
            'Path filter to apply to the end of the input path',
            defaultValue: "")
        cli.z(longOpt:'dump', 'Dump the data to files')
        return cli

    }.call()

    public static def parse = { String[] args ->
        return CLI.parser.parse(args)
    }

    public static def usage = {
        CLI.parser.usage()
    }

}