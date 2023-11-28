package com.nhnacademy.aiot.gateway;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineArgument {

    public void hasAppName(String appName) { // payload.deviceInfo.applicationName: "NHNAcademyEMS"
        if (appName != null) {
            // 프로그램 옵션으로 Application Name을 줄 수 있으며 
            // application name이 주어질 경우 해당 메시지만 수신하도록 한다.
        }

    }

    public String[] sensorSelect(String sensorName) { // getJsonObject("object")
        // 센서 종류를 저장하는 배열
        String[] selectedSenser = sensorName.split(",");
        
        for(int i = 0; i < selectedSenser.length; i++) {
            if (selectedSenser[i] == null) {
                throw new IllegalArgumentException();
            }    
        }

        for(int i = 0; i < selectedSenser.length; i++) {
            selectedSenser[i] = "object." + selectedSenser[i].trim(); // ex) object.temperature
        }

        // 메인을 실행하기 전에 센서를 처리하거나 가지고 온 후 센서 처리

        return selectedSenser;
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
