package com.carrental.availability.utils;

import java.time.Instant;

public class timeRangeToStringUtil {
    public static String rng(Instant from, Instant to){
        return String.format("[\"%s\",\"%s\")", from.toString(), to.toString());
    }
}