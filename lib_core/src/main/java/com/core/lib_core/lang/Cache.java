package com.core.lib_core.lang;

import android.os.AsyncTask;

import java.io.Serializable;

public abstract class Cache {

    public static <T> T get(Class<T> clazz) {
        return ObjectSerialize.read(clazz, null);
    }

    public static synchronized void write(Serializable obj) {
        ObjectSerialize.write(obj, null);
    }

    public static <T> void remove(Class<T> clazz) {
        ObjectSerialize.remove(clazz, null);
    }

    public static void asyncAppend(final Serializable obj) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ObjectSerialize.append(obj);
                return null;
            }
        }.execute();
    }

    public static synchronized void asyncWrite(final Serializable obj, final Class<?> classType) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ObjectSerialize.write(obj, classType);
                return null;
            }
        }.execute();
    }
}
