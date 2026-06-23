package com.shh.foodeliverybackendapp.util;

public final class MaskingUtils {

    private static final int PHONE_VISIBLE_DIGITS = 2;
    private static final String MASK_SYMBOL = "*";

    private MaskingUtils() {
        throw new UnsupportedOperationException(
                "Utility class cannot be instantiated");
    }

    public static String maskPhone(String phone) {

        if (phone == null || phone.length() <= PHONE_VISIBLE_DIGITS * 2) {
            return "****";
        }

        String masked = MASK_SYMBOL.repeat(phone.length() - PHONE_VISIBLE_DIGITS * 2);

        return phone.substring(0, PHONE_VISIBLE_DIGITS)
                + masked
                + phone.substring(phone.length() - PHONE_VISIBLE_DIGITS);
    }
}