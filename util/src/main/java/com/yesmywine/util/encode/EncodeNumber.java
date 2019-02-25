package com.yesmywine.util.encode;

import java.util.Random;

/**
 * Created by wangdiandian on 2017/4/17.
 */
public class EncodeNumber {
    public static String getSalt(int length) {
            char[] chr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            Random random = new Random();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < length; i++) {
                buffer.append(chr[random.nextInt(62)]);
            }
            return buffer.toString();
        }
    }