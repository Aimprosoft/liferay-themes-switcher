package com.aimprosoft.lfs.utils;

/**
 * @author AimProSoft
 */
public class Timer {

    private long start = System.currentTimeMillis();

    public long getTime() {
        return System.currentTimeMillis() + start;
    }

    public double getSeconds() {
        return (System.currentTimeMillis() - start) / 1000.;
    }
}
