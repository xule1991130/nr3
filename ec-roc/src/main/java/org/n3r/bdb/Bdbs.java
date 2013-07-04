package org.n3r.bdb;

import com.sleepycat.je.*;
import org.apache.commons.io.IOUtils;
import org.n3r.core.lang.RByte;

import java.io.File;

/**
 * bdb utilities。
 */
public class Bdbs {
    public static Environment openEnv(File path, long cacheSize) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true).setCacheSize(cacheSize);
        path.mkdirs();
        return new Environment(path, envConfig);
    }

    public static void closeEnv(Environment env, boolean cleanLog) {
        if (cleanLog && env != null) env.cleanLog();
        IOUtils.closeQuietly(env);
    }

    public static Database openDb(Environment env, String dbName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        return env.openDatabase(null, dbName, dbConfig);
    }

    public static void closeDb(Database db) {
        IOUtils.closeQuietly(db);
    }

    public static OperationStatus put(Database db, byte[] key, byte[] value) {
        return db.put(null, new DatabaseEntry(key), new DatabaseEntry(value));
    }

    public static OperationStatus put(Database db, String key, String value) {
        return put(db, RByte.toBytes(key), RByte.toBytes(value));
    }

    public static byte[] get(Database db, byte[] key) {
        DatabaseEntry queryKey = new DatabaseEntry(key);
        DatabaseEntry value = new DatabaseEntry();
        OperationStatus status = db.get(null, queryKey, value, LockMode.DEFAULT);
        return status == OperationStatus.SUCCESS ? value.getData() : null;
    }

    public static String get(Database db, String key) {
        byte[] bytes = get(db, RByte.toBytes(key));
        return bytes != null ? RByte.toStr(bytes) : null;
    }

    public static boolean delete(Database db, byte[] key) {
        return db.delete(null, new DatabaseEntry(key)) == OperationStatus.SUCCESS;
    }

    public static boolean delete(Database db, String key) {
        return delete(db, RByte.toBytes(key));
    }

    public static Cursor openCursor(Database db) {
        return db.openCursor(null, null);
    }

    public static void closeCursor(Cursor cursor) {
        IOUtils.closeQuietly(cursor);
    }

    public static boolean cursorNext(Cursor cursor, DatabaseEntry foundKey, DatabaseEntry foundValue) {
        OperationStatus next = cursor.getNext(foundKey, foundValue, LockMode.DEFAULT);
        return next == OperationStatus.SUCCESS;
    }

}
