package ru.domain.java3.lesson2;

import java.io.FileWriter;
import java.io.IOException;


import java.util.Date;

public class AppLogger {

    public static final String INFO = "[INFO]";
    public static final String ERROR = "[ERROR]";

    private AppLogger(){};

    public static void writeLog(String logMessage,String logType) {
        try(FileWriter fw = new FileWriter("log.txt",true)){

            fw.write(logType+" ["+new Date()+"] "+logMessage+"\n");

        }catch (IOException e){

        }
    }
}
