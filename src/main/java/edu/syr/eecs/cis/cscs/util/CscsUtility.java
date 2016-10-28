package edu.syr.eecs.cis.cscs.util;

import edu.syr.eecs.cis.cscs.entities.statemachine.*;
import io.atomix.catalyst.transport.Address;
import io.atomix.catalyst.transport.netty.NettyTransport;
import io.atomix.copycat.client.ConnectionStrategies;
import io.atomix.copycat.client.CopycatClient;
import io.atomix.copycat.client.RecoveryStrategies;
import io.atomix.copycat.client.ServerSelectionStrategies;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CscsUtility {

    /**
     * @param args
     */
    public static void main(String[] args) {

        // Parse command line String array into a Commandline
        // object containing options
        CommandLine commandLineArguments = parseCommandLine(args);

        if (!commandLineArguments.hasOption("host")) {
            System.out.println("Please specify a host in format IP:PORT");
            System.exit(1);
        }

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
            case "query": queryKey(commandLineArguments);
                break;
            case "delete": deleteKey(commandLineArguments);
                break;
        }

        System.exit(0);
    }

    private static void addKey(CommandLine commandLineArguments) {

        CopycatClient client = createClient(commandLineArguments.getOptionValue("host"));

        String key = commandLineArguments.getOptionValue("key");
        String value = commandLineArguments.getOptionValue("value");
        if (StringUtils.isNotEmpty(key)) {
            String writeResult = writeKey(client, key, value);
            System.out.println("The key " + key + " was set to " + value);
        }
        else {
            System.out.println("Key must not be empty");
            System.exit(1);
        }

        return;
    }

    private static void queryKey(CommandLine commandLineArguments) {

        CopycatClient client = createClient(commandLineArguments.getOptionValue("host"));

        String key = commandLineArguments.getOptionValue("key");
        if (StringUtils.isNotEmpty(key)) {
            String readResult = readKey(client, key);
            System.out.println("Key " + key + " has a value of " + readResult);
        }
        else {
            System.out.println("Key must not be empty");
            System.exit(1);
        }

        return;

    }

    private static void deleteKey(CommandLine commandLineArguments) {

        return;

    }

    private static CopycatClient createClient(String host) {
        // Specify member servers the client will connect to.
        List<Address> members = new ArrayList<>();
        List<String> clientMemberList = new ArrayList<>();
        if (StringUtils.isNotEmpty(host)) {
            clientMemberList = Arrays.asList(host.split("\\s*,\\s*"));
            for (String hostPortTuple : clientMemberList) {
                String[] parts = hostPortTuple.split(":");
                if (parts[0] != null && parts[1] != null ) {
                    members.add(new Address(parts[0], Integer.valueOf(parts[1])));
                }
                else {
                    System.out.println("Please specify a host in format IP:PORT");
                    System.exit(1);
                }
            }
        }
        else {
            System.out.println("Please specify a host in format IP:PORT");
            System.exit(1);
        }

        CopycatClient client = CopycatClient.builder()
                .withTransport(new NettyTransport())
                .withConnectionStrategy(ConnectionStrategies.FIBONACCI_BACKOFF)
                .withRecoveryStrategy(RecoveryStrategies.RECOVER)
                .withServerSelectionStrategy(ServerSelectionStrategies.LEADER)
                .build();

        client.serializer().register(MapPutCommand.class);
        client.serializer().register(MapGetQuery.class);

        client.connect(members).join();

        return client;
    }

    // returns the value as a string of the specified key
    private static String readKey(CopycatClient stateMachineClient, String key) {
        Object getResult = stateMachineClient.submit(new MapGetQuery(key)).join();
        String value = (getResult != null) ? getResult.toString() : "";
        return value;
    }

    // returns as string the value that was set
    private static String writeKey(CopycatClient stateMachineClient, String key, String value) {
        Object putResult = stateMachineClient.submit(new MapPutCommand(key, value)).join();
        String valueResult = (putResult != null) ? putResult.toString() : "";
        return valueResult;

    }

    private static CommandLine parseCommandLine(String[] args) {

        //first define the possible options
        Options options = new Options();
        Boolean requiresArg = true;
        Boolean doesNotRequireArg = false;
        options.addOption( "help", doesNotRequireArg, "print this message" );
        options.addOption("encrypt", doesNotRequireArg, "perform encryption of value");
        options.addOption("host", requiresArg, "IP:PORT of cluster host to submit to)");
        options.addOption("key", requiresArg, "key name");
        options.addOption("value", requiresArg, "string to place in value");
        options.addOption("action", requiresArg, "add, query, or delete");

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
