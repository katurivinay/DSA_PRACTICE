class Solution {
    public boolean isValid(int i , int j , int n , int m){
        if(i >= 0 && i < n && j >= 0 && j < m)return true;
        return false;
    }
    public void dfs(char[][] grid , int[][] vis ,int  i , int j , int n ,int  m){
        vis[i][j] = 1;
        int dr[] = {0 , 0 , 1 , -1};
        int dc[] = {1, -1 , 0 , 0};
        for(int l = 0 ; l < 4 ; l++){
            if(isValid(i+ dr[l] , j + dc[l] , n , m) && vis[i+ dr[l]][j + dc[l]] == 0 && grid[i+ dr[l]][j + dc[l]] == '1'){
                dfs(grid , vis , i + dr[l], j + dc[l] , n , m);
            }
        }
    }
    public int numIslands(char[][] grid) {
        int n = grid.length , m = grid[0].length;
        int vis[][] = new int[n][m];
        int count = 0;
        for(int i = 0 ; i < n ; i++){
            for(int j = 0 ; j < m ; j++){
                if(vis[i][j] == 0 && grid[i][j] == '1'){
                    dfs(grid , vis , i , j , n , m);
                    count += 1;
                }
            }
        }
        return count;
    }
}