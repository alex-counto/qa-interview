package com.xion.interview.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Occupation {
    Manager,
    Engineer,
    Accountant,
    Tester;

    public static String[] names(){
        List<String> collect = Arrays.stream(Occupation.values()).map(o -> o.name()).collect(Collectors.toList());
        return collect.toArray(new String[0]);
    }
}
