package me.ezjs.core.util;

import java.util.Random;

/**
 * Created by Zjs-yd on 2016/8/4.
 */
public class MathUtil {

    /**
     * 获取4位随机数字作为验证码.
     *
     * @return
     */
    public static final String get4Numbers() {
        int ran = (int) (Math.random() * 1000);
        if (ran < 10) {
            return "000" + ran;
        }
        if (ran < 100) {
            return "00" + ran;
        }
        if (ran < 1000) {
            return "0" + ran;
        }
        return "" + ran;
    }

    public static int randomInt(int min, int max) {
        Random r = new Random();
        int res = r.nextInt(max - min + 1) + min;
        return res;
    }

    public static void main(String[] args) {
        MathUtil.randomInt(10, 30);
        System.out.println(MathUtil.get4Numbers());
    }

}
