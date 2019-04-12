package com.core.lib_core.http.okhttp;

/**
 * @author 李澄锋
 */
public class Exceptions
{
    public static void illegalArgument(String msg, Object... params)
    {
        throw new IllegalArgumentException(String.format(msg, params));
    }


}
