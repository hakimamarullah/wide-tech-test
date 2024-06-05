### Time Complexity Analysis of Bubble Sort

Let's analyze the time complexity of the given Bubble Sort implementation in detail.

#### Pseudocode
```java
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
        if (noSwap) {
            break;
        }
    }
}
```

### Analysis

#### Outer Loop
The outer loop runs `n` times:
```java
for (int i = 0; i < n; i++)
```

#### Inner Loop
The inner loop runs `n - 1 - i` times for each iteration of the outer loop:
```java
for (int j = 0; j < n - 1 - i; j++)
```

Let's break down the total number of operations:

- On the first pass (`i = 0`), the inner loop runs `n - 1` times.
- On the second pass (`i = 1`), the inner loop runs `n - 2` times.
- On the third pass (`i = 2`), the inner loop runs `n - 3` times.
- ...
- On the last pass (`i = n - 1`), the inner loop runs `0` times (this iteration doesn't actually happen because the loop condition is not met).

### Total Number of Comparisons

The total number of comparisons made by the inner loop can be expressed as the sum of the first `n-1` natural numbers:
$
(n - 1) + (n - 2) + (n - 3) + \cdots + 1 + 0
$

This is equivalent to the arithmetic series sum:
$$
\sum_{i=0}^{n-1} (n - 1 - i)
$$

Which simplifies to:
$$
\sum_{i=1}^{n-1} i = \frac{(n-1) \cdot n}{2}
$$

Thus, the total number of comparisons is:
$$
\frac{(n-1) \cdot n}{2}
$$

### Simplifying to Big-O Notation

In Big-O notation, we are interested in the asymptotic behavior of the function as `n` grows larger. We focus on the leading terms and ignore constant factors and lower-order terms.

1. The expression \(\frac{(n-1) \cdot n}{2}\) can be expanded to:
   $$
   \frac{n^2 - n}{2}
  $$

2. When considering Big-O notation, we drop the constant factor $\frac{1}{2}$ and the lower-order term (-n) because they do not significantly affect the growth rate as (n) becomes large.

3. This leaves us with:
   $$
   O(n^2)
   $$


### Explanation

- **Dropping Constants**: The constant factor $/frac{1}{2}$ is dropped because Big-O notation focuses on the growth rate relative to the input size, not the exact number of operations. Constants do not change the growth rate classification.
- **Dropping Lower-Order Terms**: The term (-n) is much smaller compared to $(n^2)$ as (n) becomes very large. For large \(n\), \(n^2\) dominates the expression, making the \(-n\) term negligible.

### Best Case Complexity: O(n)
- **Scenario**: The array is already sorted.
- In the best case, the algorithm will make a single pass through the array without making any swaps.
- The inner loop will complete `n-1` comparisons, and the `noSwap` flag will remain `true`, causing the algorithm to break out of the loop early.
- Therefore, the best case time complexity is: *O(n)*

### Worst Case Complexity: O(n^2)
- **Scenario**: The array is sorted in reverse order.
- In the worst case, the algorithm will make the maximum number of swaps. Each element will have to be moved to the correct position through multiple passes.
- The inner loop will run for `n-1` comparisons on the first pass, `n-2` on the second pass, and so on, until `1` comparison on the last pass.
- The total number of comparisons is: *O(n<sup>2</sup>)*

### Average Case Complexity: O(n^2)
- **Scenario**: The array elements are in random order.
- On average, the algorithm will make a number of swaps that is proportional to the size of the array.
- The average case time complexity can be derived similarly to the worst case, as the algorithm will still have to perform a quadratic number of comparisons and swaps in the general case.
- Therefore, the average case time complexity is: *O(n<sup>2</sup>)*

### Summary
- **Best Case**: *O(n)*
- **Worst Case**: *O(n<sup>2</sup>)*
- **Average Case**: *O(n<sup>2</sup>)*

The Bubble Sort algorithm is simple and easy to understand but inefficient for large datasets due to its quadratic time complexity in the average and worst cases.