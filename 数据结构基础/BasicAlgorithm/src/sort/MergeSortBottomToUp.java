package sort;

public class MergeSortBottomToUp {
	public static void mergeSort(int[] nums) {
		int n = nums.length;
		for(int sz = 1; sz <= n; sz += sz) {
			for(int i = 0; i + sz < n; i += 2*sz ) {
				//nums[i,....,i+sz-1] nums[i+sz,...,i+2*sz-1]
				MergeSort.merge(nums, i, i + sz - 1, Math.min(n-1, i+2*sz-1));
			}
		}
	}
	
	public static void main(String[] args) {
		int[] nums = new int[] {4,5,9,6,3,2,1,5,7,8,9,-2,9,8,5,1};
		mergeSort(nums);
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
	}
}
