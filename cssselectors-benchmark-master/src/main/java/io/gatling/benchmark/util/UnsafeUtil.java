package io.gatling.benchmark.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtil {
    private static final Unsafe UNSAFE;
    private static final long STRING_VALUE_FIELD_OFFSET;

    static {
        if (System.getProperty("java.version").startsWith("1.8")) {
            Unsafe unsafe;
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe) unsafeField.get(null);

            } catch (Throwable cause) {
                unsafe = null;
            }

            long stringValueFieldOffset = -1L;

            if (unsafe != null) {
                try {
                    stringValueFieldOffset = unsafe.objectFieldOffset(String.class.getDeclaredField("value"));
                } catch (Throwable cause) {
                }
            }

            UNSAFE = unsafe;
            STRING_VALUE_FIELD_OFFSET = stringValueFieldOffset;
        } else {
            UNSAFE = null;
            STRING_VALUE_FIELD_OFFSET = -1;
        }
    }

    public static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    public static char[] getChars(String string) {
        return hasUnsafe() ?
                (char[]) UNSAFE.getObject(string, STRING_VALUE_FIELD_OFFSET) :
                string.toCharArray();
    }
}
