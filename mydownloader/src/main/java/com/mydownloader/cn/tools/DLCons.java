package com.mydownloader.cn.tools;

import android.provider.BaseColumns;

final class DLCons {
    private DLCons() {
    }

    static boolean DEBUG = true;

    static final class Base {
        static final int DEFAULT_TIMEOUT = 20000;
        static final int MAX_REDIRECTS = 5;
        static final int LENGTH_PER_THREAD = 10485760;
    }

    static final class Code {
        static final int HTTP_CONTINUE = 100;
        static final int HTTP_SWITCHING_PROTOCOLS = 101;
        static final int HTTP_PROCESSING = 102;

        static final int DOWNLOAD_err_url = 103;
        static final int DOWNLOAD_err_no_response = 104;
    }

    static final class DBCons {
        static final String TB_TASK = "task_info";
        static final String TB_TASK_URL_BASE = "base_url";
        static final String TB_TASK_URL_REAL = "real_url";
        static final String TB_TASK_DIR_PATH = "file_path";
        static final String TB_TASK_CURRENT_BYTES = "currentBytes";
        static final String TB_TASK_TOTAL_BYTES = "totalBytes";
        static final String TB_TASK_FILE_NAME = "file_name";
        static final String TB_TASK_MIME_TYPE = "mime_type";
        static final String TB_TASK_ETAG = "e_tag";
        static final String TB_TASK_DISPOSITION = "disposition";
        static final String TB_TASK_LOCATION = "location";

        static final String TB_THREAD = "thread_info";
        static final String TB_THREAD_URL_BASE = "base_url";
        static final String TB_THREAD_START = "start";
        static final String TB_THREAD_END = "end";
        static final String TB_THREAD_ID = "id";

        static final String TB_TASK_SQL_CREATE = "CREATE TABLE " +
                DLCons.DBCons.TB_TASK + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DLCons.DBCons.TB_TASK_URL_BASE + " CHAR, " +
                DLCons.DBCons.TB_TASK_URL_REAL + " CHAR, " +
                DLCons.DBCons.TB_TASK_DIR_PATH + " CHAR, " +
                DLCons.DBCons.TB_TASK_FILE_NAME + " CHAR, " +
                DLCons.DBCons.TB_TASK_MIME_TYPE + " CHAR, " +
                DLCons.DBCons.TB_TASK_ETAG + " CHAR, " +
                DLCons.DBCons.TB_TASK_DISPOSITION + " CHAR, " +
                DLCons.DBCons.TB_TASK_LOCATION + " CHAR, " +
                DLCons.DBCons.TB_TASK_CURRENT_BYTES + " INTEGER, " +
                DLCons.DBCons.TB_TASK_TOTAL_BYTES + " INTEGER)";
        static final String TB_THREAD_SQL_CREATE = "CREATE TABLE " +
                DLCons.DBCons.TB_THREAD + "(" +
                BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DLCons.DBCons.TB_THREAD_URL_BASE + " CHAR, " +
                DLCons.DBCons.TB_THREAD_START + " INTEGER, " +
                DLCons.DBCons.TB_THREAD_END + " INTEGER, " +
                DLCons.DBCons.TB_THREAD_ID + " CHAR)";

        static final String TB_TASK_SQL_UPGRADE = "DROP TABLE IF EXISTS " +
                DLCons.DBCons.TB_TASK;
        static final String TB_THREAD_SQL_UPGRADE = "DROP TABLE IF EXISTS " +
                DLCons.DBCons.TB_THREAD;
    }
}