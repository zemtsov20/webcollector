package com.mailparser.mailextract;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

@Slf4j
public class MyDataSource extends BasicDataSource {

    private static MyDataSource instance;

    public static MyDataSource getInstance() {
        if (instance == null) {
            synchronized (MyDataSource.class) {
                if (instance == null) {
                    instance = new MyDataSource();
                }
            }
        }
        return instance;
    }

    private static final String USER = "admin";
    private static final String PASS = "mD8qH";
    private static final String VALIDATION_QUERY = "select 1";
    private static final String DRIVER_NAME = org.postgresql.Driver.class.getName();
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5431";
    private static final String DB_NAME = "maildb";

    public MyDataSource() {
        setDriverClassName(DRIVER_NAME);

        String user = System.getProperty("db.user", USER);
        String pass = System.getProperty("db.pass", PASS);
        String name = System.getProperty("db.name", DB_NAME);

        log.info("Database will work with user(taken from -Ddb.user?): " + user);
        log.info("Database will work with pass(taken from -Ddb.pass?), which len = " + pass.length());

        setUsername(user);
        setPassword(pass);

        String url = DB_URL + (name.isEmpty() ? name : "/" + name);
        log.info("Database will work with user(taken from -Ddb.name?): " + url);
        setUrl(url);
        setInitialSize(1);
        setValidationQuery(VALIDATION_QUERY);

        setDefaultAutoCommit(false);

    }
}