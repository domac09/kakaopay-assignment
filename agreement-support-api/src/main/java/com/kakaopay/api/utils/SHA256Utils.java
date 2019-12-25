package com.kakaopay.api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SHA256Utils {
    public static String encrypt(String originalString) {
        return DigestUtils.sha256Hex(originalString);
    }
}
