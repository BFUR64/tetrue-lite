package com.teic.trueris;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Logging {
    private static final String FOLDER_NAME = "logs";

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final int LOG_LEVEL = LogType.ERROR.SEVERITY;

    public static void writeStackTrace(LogType type, Exception error) {
        if (type.SEVERITY < LOG_LEVEL) {
            return;
        }

        ensureDirExists();

        try (
            FileWriter file = new FileWriter(getFilePath(), true);
            PrintWriter pWriter = new PrintWriter(file);
        ) {
            pWriter.println(formatLog(
                type,
                error.getClass().getSimpleName() +
                ": " + error.getMessage())
            );

            error.printStackTrace(pWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatLog(LogType type, String log) {
        return LocalDate.now().format(DATE_FORMAT) +
                " " + LocalTime.now().format(TIME_FORMAT) +
                " " + type.LABEL +
                " " + log;
    }

    private static void ensureDirExists() {
        File dir = new File(FOLDER_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String getFilePath() {
        return FOLDER_NAME + "/" +
                LocalDate.now().format(DATE_FORMAT) +
                ".log";
    }
}
