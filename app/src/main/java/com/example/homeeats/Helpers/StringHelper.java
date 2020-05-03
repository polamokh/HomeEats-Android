package com.example.homeeats.Helpers;

import java.util.ArrayList;
import java.util.Arrays;

public class StringHelper {
    private static ArrayList<Integer> prefix_function(String s) {
        int n = (int)s.length();
        ArrayList<Integer> pi = new ArrayList<Integer>(Arrays.asList(new Integer[n]));
        pi.set(0, 0);
        for (int i = 1; i < n; i++) {
            int j = pi.get(i-1);
            while (j > 0 && Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(s.charAt(j)))
                j = pi.get(j-1);
            if (Character.toLowerCase(s.charAt(i)) == Character.toLowerCase(s.charAt(j)))
                j++;
            pi.set(i, j);
        }
        return pi;
    }
    public static boolean Contains(String text, String pattern){
        ArrayList<Integer> pi = prefix_function(pattern + "#" + text);
        for(Integer val : pi)
            if(val == pattern.length())
                return true;
        return false;
    }
}
