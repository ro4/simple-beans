package me.ro4.beans.util;

@SuppressWarnings("unused")
public class Assert {
    public static void isNull(Object object, String message) {
        if (null != object) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotNull(Object object, String message) {
        if (null == object) {
            throw new IllegalArgumentException(message);
        }
    }
}
