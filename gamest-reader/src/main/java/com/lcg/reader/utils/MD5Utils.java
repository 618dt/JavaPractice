package com.lcg.reader.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String md5Digest(String source, Integer salt) {
        char[] ca = source.toCharArray();//获取到字符数组
        for (int i = 0; i < ca.length; i++) {
            ca[i] = (char) (ca[i] + salt);//对原始字符串的每一位字符加盐混淆
        }
        String target = new String(ca);//再将字符数组转换为原始字符串
        String md5 = DigestUtils.md5Hex(target);//MD5摘要
        return md5;
    }
}
