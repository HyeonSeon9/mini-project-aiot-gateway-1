package com.nhnacademy.aiot.gateway;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandOption {

    public void hasAppName() {
        
    }

    public void sensorSelect() {

    }

    public static void main(String[] args) {
        CommandOption commandOption = new CommandOption();
        Options options = new Options();

        options.addOption("an", null, true, "어플리케이션 이름을 지정합니다.");
        options.addOption("s", null, true, "허용 가능한 센서 종류를 지정합니다.");
        // longOpt
        
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine commandLine = parser.parse(options, args);
            
            if (commandLine.hasOption("an")) { 
                // 프로그램 옵션으로 Application Name을 줄 수 있으며
                //, application name이 주어질 경우 해당 메시지만 수신하도록 한다.
                System.out.println(commandLine.getOptionValue("an") + "이 추가되었습니다.");
            }
            if (commandLine.hasOption("s")) {
                // 허용 가능한 옵션 센서 종류 지정
                System.out.println(commandLine.getOptionValue("s") + "이 삭제되었습니다.");
            }
        } catch (ParseException ignore) {
        
        }
    }
}
