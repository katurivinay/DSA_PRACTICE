class Solution {
    public int findMaxLength(int[] nums) {
        int n = nums.length , sum = 0;
        Map<Integer,Integer> mp = new HashMap<>();
        mp.put(0 , -1);
        int ans =0;
        for(int i = 0 ; i < n ; i++){
            sum += ( (nums[i] != 1) ? -1 : 1 );
            if(mp.containsKey(sum)){
                ans = Math.max(ans , i - mp.get(sum));
            }
            mp.put(sum , mp.getOrDefault(sum , i));
        }
        return ans;
    }
}