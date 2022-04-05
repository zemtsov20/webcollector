package com.common.enums;

public enum State {
        QUEUED,

        DOWNLOADING,
        DOWNLOADING_ERROR,
        DOWNLOADED,

        PARSING,
        PARSED,
        PARSING_ERROR,

        CHECKED
}
