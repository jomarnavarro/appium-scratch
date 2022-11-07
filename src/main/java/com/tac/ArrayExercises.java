package com.tac;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ArrayExercises {

    @Test
    public void testMergeEmptyArrays() {
        Assert.assertArrayEquals(new int[0], mergeArrays2(new int[0], new int[0]));
    }

    @Test
    public void tesstMergeEmptyAndNonEmpty() {
        Assert.assertArrayEquals(new int[] {1, 2, 3}, mergeArrays2(new int[] {}, new int[] {1, 2, 3}));
    }

    @Test
    public void tesstMergeNonEmptyArrays() {
        Assert.assertArrayEquals(new int[] {1, 2, 3}, mergeArrays2(new int[] {2}, new int[] {1, 3}));
    }



    public int[] mergeArrays(int[] array1, int[] array2) {
        int[] merged = new int[array1.length + array2.length];
        int counter = 0;
        for(int x: array1) {
            merged[counter] = x;
            counter++;
        }

        for(int x: array2) {
            merged[counter] = x;
            counter++;
        }
        Arrays.sort(merged);
        return merged;

    }

    public int[] mergeArrays2(int[] array1, int[] array2) {
        Map<Integer, Boolean> sortedMap = new TreeMap<>();
        int[] merged  = new int[array1.length + array2.length];
        for(int x: array1) {
            sortedMap.put(x, true);
        }

        for(int x: array2) {
            sortedMap.put(x, true);
        }
        int k = 0;
        for(int x: sortedMap.keySet()) {
            merged[k] = x;
            k++;
        }
        return merged;
    }
}
