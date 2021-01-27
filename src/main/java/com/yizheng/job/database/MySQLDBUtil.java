package com.yizheng.job.database;

public class MySQLDBUtil {
    private static final String INSTANCE = "project-instance.c0xjh4vztkv7.us-east-2.rds.amazonaws.com";
    private static final String PORT_NUM = "3306";
    public static final String DB_NAME = "project";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "eqape3lvvt";
    public static final String URL = "jdbc:mysql://"
            + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
            + "?user=" + USERNAME + "&password=" + PASSWORD
            + "&autoReconnect=true&serverTimezone=UTC";
}
