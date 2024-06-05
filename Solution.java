/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/5/2024 2:26 PM
@Last Modified 6/5/2024 2:26 PM
Version 1.0
*/

import java.util.Arrays;
import java.util.stream.IntStream;

public class Solution {

    public static void main(String[] args) {
        int[] input = {5,4,3,2,1,0,-1,-2,-3,-4};
        bubbleSort(input);
        System.out.println(Arrays.toString(input));
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            boolean noSwap = true;

            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    noSwap = false;
                }

            }
            if(noSwap) {
                break;
            }
        }

    }
}
