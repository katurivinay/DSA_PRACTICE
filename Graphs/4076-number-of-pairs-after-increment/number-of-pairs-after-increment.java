import java.util.*;

class Solution {
    public int[] numberOfPairs(int[] nums1, int[] nums2, int[][] queries) {
        int n = nums2.length;
        int B = Math.max(1, (int) Math.sqrt(n));
        int numBlocks = (n + B - 1) / B;

        // Invariant: true(nums2[k]) = vals[b][k - b*B] + lazy[b]
        long[][] vals = new long[numBlocks][];
        long[] lazy = new long[numBlocks];
        HashMap<Long, Integer>[] freq = new HashMap[numBlocks];

        for (int b = 0; b < numBlocks; b++) {
            int lo = b * B, hi = Math.min(n, (b + 1) * B);
            vals[b] = new long[hi - lo];
            freq[b] = new HashMap<>();
            for (int i = lo; i < hi; i++) {
                vals[b][i - lo] = nums2[i];
                freq[b].merge((long) nums2[i], 1, Integer::sum);
            }
        }

        List<Integer> answer = new ArrayList<>();
        for (int[] q : queries) {
            if (q[0] == 1) {
                rangeAdd(q[1], q[2], q[3], B, vals, lazy, freq);
            } else {
                long tot = q[1];
                long cnt = 0;
                for (int a : nums1) cnt += countEqual(tot - a, numBlocks, lazy, freq);
                answer.add((int) cnt);
            }
        }

        int[] out = new int[answer.size()];
        for (int i = 0; i < out.length; i++) out[i] = answer.get(i);
        return out;
    }

    private void partialUpdate(int b, int lo, int hi, long val, int B,
                               long[][] vals, HashMap<Long, Integer>[] freq) {
        int offset = b * B;
        long[] bv = vals[b];
        HashMap<Long, Integer> bf = freq[b];
        for (int k = lo; k <= hi; k++) {
            int idx = k - offset;
            long old = bv[idx];
            int c = bf.get(old) - 1;
            if (c == 0) bf.remove(old); else bf.put(old, c);
            long nw = old + val;
            bv[idx] = nw;
            bf.merge(nw, 1, Integer::sum);
        }
    }

    private void rangeAdd(int x, int y, long val, int B,
                          long[][] vals, long[] lazy, HashMap<Long, Integer>[] freq) {
        int bx = x / B, by = y / B;
        if (bx == by) {
            partialUpdate(bx, x, y, val, B, vals, freq);
            return;
        }
        partialUpdate(bx, x, (bx + 1) * B - 1, val, B, vals, freq);
        for (int b = bx + 1; b < by; b++) lazy[b] += val;
        partialUpdate(by, by * B, y, val, B, vals, freq);
    }

    private long countEqual(long target, int numBlocks,
                            long[] lazy, HashMap<Long, Integer>[] freq) {
        long total = 0;
        for (int b = 0; b < numBlocks; b++) {
            long key = target - lazy[b];
            Integer c = freq[b].get(key);
            if (c != null) total += c;
        }
        return total;
    }
}