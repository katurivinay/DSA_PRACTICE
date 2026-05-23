class Solution {
    public void dfs(int node, int[][] isConnected , int[] vis , int n){
        vis[node] = 1;
        for(int i = 0 ; i < n ; i++){
            if(isConnected[node][i] == 1 && vis[i] == 0){
                dfs(i , isConnected , vis , n);
            }
        }
    }
    public int findCircleNum(int[][] isConnected) {
        int ans = 0;
        int n = isConnected.length;
        int vis[] = new int[n];
        for(int i = 0; i < n ; i++){
            if(vis[i] == 0){
                dfs(i ,isConnected , vis , n);
                ans += 1;
            }
        }
        return ans;
    }
}