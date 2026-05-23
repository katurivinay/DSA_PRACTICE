class Solution {
    public int maxScore(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int ans = Integer.MIN_VALUE;
        
        // Length-≥-2 Kadane on each row
        for (int i = 0; i < m; i++) {
            int prev = grid[i][0];
            for (int j = 1; j < n; j++) {
                int extended = prev + grid[i][j];
                ans = Math.max(ans, extended);
                prev = Math.max(grid[i][j], extended);
            }
        }
        
        // Length-≥-2 Kadane on each column
        for (int j = 0; j < n; j++) {
            int prev = grid[0][j];
            for (int i = 1; i < m; i++) {
                int extended = prev + grid[i][j];
                ans = Math.max(ans, extended);
                prev = Math.max(grid[i][j], extended);
            }
        }
        
        // Interior single cells: 0 < i < m-1 AND 0 < j < n-1
        for (int i = 1; i < m - 1; i++)
            for (int j = 1; j < n - 1; j++)
                ans = Math.max(ans, grid[i][j]);
        
        return ans;
    }
}