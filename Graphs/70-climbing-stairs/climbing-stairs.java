class Solution {
    public int climbStairs(int n) {
        int prev1 = 1,prev2 = 2 , ans = 0;
        if(n == 1)return prev1;
        if(n == 2)return prev2;
        for(int i = 2 ; i < n ; i++){
            ans = prev1 + prev2;
            prev1 = prev2;
            prev2 = ans;
        }
        return ans;
    }
}