package com.nhnacademy.aiot.gateway;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArgument {

    public void hasAppName(String appName) { // applicationName: "NHNAcademyEMS"
        if (appName != null) {
            // 해당 애플리케이션의 메세지만 수신
            // application/#
        }

    }

    public String[] sensorSelect(String sensorName) {
        String[] selectedSenser = sensorName.split(",");
        // 잘못된 요소가 들어가있는 경우의 처리

        for(int i = 0; i < selectedSenser.length; i++) {
            // 더 많은 요소 추가?
            if (selectedSenser[i] == "temperature" ||
                selectedSenser[i] == "humidity"  ||
                selectedSenser[i] == "co2") {
            } else {
                throw new IllegalArgumentException();
            }
        }
        return selectedSenser; // 배열은 받은 메소드가 해당 값을 처리하도록 해야함
    }

    public static void main(String[] args) {
        CommandLineArgument commandLineArgument = new CommandLineArgument();
        Options options = new Options();

        options.addOption("an", true, "어플리케이션 이름을 지정합니다.");
        options.addOption("s", true, "허용 가능한 센서 종류를 지정합니다.");
        
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            
            if (commandLine.hasOption("an")) { 
                // 프로그램 옵션으로 Application Name을 줄 수 있으며
                //, application name이 주어질 경우 해당 메시지만 수신하도록 한다.
                commandLineArgument.hasAppName(commandLine.getOptionValue("an"));
                System.out.println(commandLine.getOptionValue("an") + "이 추가되었습니다.");
            }
            if (commandLine.hasOption("s")) {
                // 허용 가능한 옵션 센서 종류 지정
                commandLineArgument.hasAppName(commandLine.getOptionValue("s"));
                System.out.println(commandLine.getOptionValue("s") + "이 삭제되었습니다.");
            }
        } catch (ParseException ignore) {
        
        }
    }
}
