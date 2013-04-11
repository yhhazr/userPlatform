package com.sz7road.userplatform.ppay.yeepay;

import com.google.common.base.Charsets;
import com.sz7road.utils.HexUtils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author jeremy
 */
class MD5HmacDigestUtil {

    private static final Charset DEFAULT_CHARSET = Charsets.UTF_8;

    public static String digestAsHex(String aValue, String aKey) {
        byte k_ipad[] = new byte[64];
        byte k_opad[] = new byte[64];
        byte keyb[];
        byte value[];

        keyb = aKey.getBytes(DEFAULT_CHARSET);
        value = aValue.getBytes(DEFAULT_CHARSET);

        Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
        Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
        for (int i = 0; i < keyb.length; i++) {
            k_ipad[i] = (byte) (keyb[i] ^ 0x36);
            k_opad[i] = (byte) (keyb[i] ^ 0x5c);
        }

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

            return null;
        }
        md.update(k_ipad);
        md.update(value);
        byte dg[] = md.digest();
        md.reset();
        md.update(k_opad);
        md.update(dg, 0, 16);
        dg = md.digest();
        return HexUtils.toHexString(dg);
    }
}
