package com.nicecredit.pilot.rule;

import java.util.List;

public class Context {
    public static ThreadLocal<List<String>> container = new ThreadLocal<List<String>>();
}