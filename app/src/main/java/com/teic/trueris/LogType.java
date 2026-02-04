package com.teic.trueris;

public enum LogType {
    DEBUG("[ DEBUG ]", 0),
    INFO("[ INFO ]", 1),
    WARN("[ WARN ]", 2),
    ERROR("[ ERROR ]", 3);

    public final String LABEL;
    public final int SEVERITY;

    private LogType(String label, int severity) {
        this.LABEL = label;
        this.SEVERITY = severity;
    }
}
