package edu.syr.eecs.cis.cscs.util;

import org.apache.commons.cli.*;

public class CscsUtility {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Parse command line String array into a Commandline
        // object containing options
        CommandLine commandLineArguments = parseCommandLine(args);

        String action = "";
        if (commandLineArguments.hasOption("action")) {
            action = commandLineArguments.getOptionValue("action");
        }
        else {
            System.out.println("Please specify action of add or delete");
            System.exit(1);
        }

        switch (action) {
            case "add": addKey(commandLineArguments);
                break;
            case "delete": deleteKey(commandLineArguments);
                break;
        }

        System.exit(0);
    }

    private static void addKey(CommandLine commandLineArguments) {

    }

    private static void deleteKey(CommandLine commandLineArguments) {

    }

    private static CommandLine parseCommandLine(String[] args) {

        //first define the possible options
        Options options = new Options();
        Boolean requiresArg = true;
        Boolean doesNotRequireArg = false;
        options.addOption( "help", doesNotRequireArg, "print this message" );
        options.addOption("encrypt", doesNotRequireArg, "perform encryption of value");
        options.addOption("host", requiresArg, "cluster host to submit to");
        options.addOption("key", requiresArg, "key name");
        options.addOption("value", requiresArg, "string to place in value");
        options.addOption("action", requiresArg, "add or delete");

        CommandLineParser commandParser = new DefaultParser();
        CommandLine commandLineArguments = null;
        try {
            commandLineArguments = commandParser.parse(options, args, true);
        }
        catch (ParseException exp) {
            System.err.println("Error parsing command line: " + exp.getMessage());
        }

        // print usage information and exit if the -help argument was passed
        if (commandLineArguments.hasOption("help")) {
            // automatically generate the help statements
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "CscsUtility <action> [encrypt] <host> <key> [value]", options );
            System.exit(1);
        }

        return commandLineArguments;
    }
}
