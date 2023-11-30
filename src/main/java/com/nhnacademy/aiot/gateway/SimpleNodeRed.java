package com.nhnacademy.aiot.gateway;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class SimpleNodeRed {

    public static void main(String[] args) {
        SettingNode settingNode = new SettingNode();
        isCommandLine(settingNode, args);
        settingNode.makeFlow();
        settingNode.connectWire();
        settingNode.nodeStart();
    }

    public static void isCommandLine(SettingNode settingNode, String[] args) {

        Options commandOptions = new Options();
        commandOptions.addOption("c", null, false, "Test");
        commandOptions.addOption(null, "an", true, "application name이 주어질 경우 해당 메시지만 수신하도록 한다.");
        commandOptions.addOption("s", null, true, "Test");
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(commandOptions, args);
            if (commandLine.hasOption("c")) {
                settingNode.commandLineOn();
            }
            if (commandLine.hasOption("an")) {
                settingNode.setAplicationName(commandLine.getOptionValue("an"));
            }
            if (commandLine.hasOption("s")) {
                settingNode.setSensors(
                        new ArrayList<>(List.of(commandLine.getOptionValue("s").split(","))));
            }
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("test", commandOptions);
        }
    }
}
