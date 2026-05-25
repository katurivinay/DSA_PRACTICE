class Solution {
    int[][][][] sparse; // sparse[i][j][p][q] = max over rect [i..i+2^p-1][j..j+2^q-1]
    int[] log;
    int n, m;
    int[][] mat;
    
    public int countLocalMaximums(int[][] matrix) {
        this.mat = matrix;
        n = matrix.length;
        m = matrix[0].length;
        
        // Precompute log table
        int maxDim = Math.max(n, m) + 1;
        log = new int[maxDim + 1];
        for (int i = 2; i <= maxDim; i++) log[i] = log[i / 2] + 1;
        
        int LOGN = log[n] + 1;
        int LOGM = log[m] + 1;
        
        // Build 2D sparse table for max
        sparse = new int[n][m][LOGN][LOGM];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                sparse[i][j][0][0] = matrix[i][j];
        
        // Fill column dimension first
        for (int p = 0; p < LOGN; p++) {
            for (int q = 0; q < LOGM; q++) {
                if (p == 0 && q == 0) continue;
                for (int i = 0; i + (1 << p) <= n; i++) {
                    for (int j = 0; j + (1 << q) <= m; j++) {
                        if (q > 0) {
                            sparse[i][j][p][q] = Math.max(
                                sparse[i][j][p][q-1],
                                sparse[i][j + (1 << (q-1))][p][q-1]
                            );
                        } else { // p > 0
                            sparse[i][j][p][q] = Math.max(
                                sparse[i][j][p-1][q],
                                sparse[i + (1 << (p-1))][j][p-1][q]
                            );
                        }
                    }
                }
            }
        }
        
        int count = 0;
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < m; c++) {
                int x = matrix[r][c];
                if (x == 0) continue;
                
                int r1 = Math.max(0, r - x), r2 = Math.min(n - 1, r + x);
                int c1 = Math.max(0, c - x), c2 = Math.min(m - 1, c + x);
                
                if (isLocalMax(r, c, x, r1, r2, c1, c2)) count++;
            }
        }
        return count;
    }
    
    boolean isLocalMax(int r, int c, int x, int r1, int r2, int c1, int c2) {
        // Decompose [r1..r2] x [c1..c2] minus 4 corners into 3 sub-rectangles
        // The 4 corners (if inside the matrix) are at (r-x,c-x), (r-x,c+x), (r+x,c-x), (r+x,c+x)
        // Top row (r1) excluding corners: cols [c1+1, c2-1] if r1 == r-x, else cols [c1, c2]
        // Bottom row (r2) excluding corners: same logic
        // Middle: rows [r1+1, r2-1], cols [c1, c2]
        
        boolean topIsCornerRow = (r1 == r - x);
        boolean botIsCornerRow = (r2 == r + x);
        boolean leftIsCornerCol = (c1 == c - x);
        boolean rightIsCornerCol = (c2 == c + x);
        
        // Check top row
        if (r1 <= r2) {
            int tc1 = c1, tc2 = c2;
            if (topIsCornerRow && leftIsCornerCol) tc1++;
            if (topIsCornerRow && rightIsCornerCol) tc2--;
            if (tc1 <= tc2) {
                if (queryMax(r1, r1, tc1, tc2) > x) return false;
            }
        }
        
        // Check bottom row (only if different from top)
        if (r2 > r1) {
            int bc1 = c1, bc2 = c2;
            if (botIsCornerRow && leftIsCornerCol) bc1++;
            if (botIsCornerRow && rightIsCornerCol) bc2--;
            if (bc1 <= bc2) {
                if (queryMax(r2, r2, bc1, bc2) > x) return false;
            }
        }
        
        // Check middle rows
        if (r1 + 1 <= r2 - 1) {
            if (queryMax(r1 + 1, r2 - 1, c1, c2) > x) return false;
        }
        
        return true;
    }
    
    int queryMax(int r1, int r2, int c1, int c2) {
        int p = log[r2 - r1 + 1];
        int q = log[c2 - c1 + 1];
        int a = sparse[r1][c1][p][q];
        int b = sparse[r2 - (1 << p) + 1][c1][p][q];
        int d = sparse[r1][c2 - (1 << q) + 1][p][q];
        int e = sparse[r2 - (1 << p) + 1][c2 - (1 << q) + 1][p][q];
        return Math.max(Math.max(a, b), Math.max(d, e));
    }
}