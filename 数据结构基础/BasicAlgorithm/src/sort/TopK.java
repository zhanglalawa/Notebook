package sort;


import java.util.Random;

/*
 * 求解第K大的元素
 * O（n）利用快排的Partition
 */
public class TopK {
	public static void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	public static int partition(int[] nums, int l, int r) {
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];
		int j = l;
		for (int i = l + 1; i <= r; i++) {
			if (nums[i] > pivot) {
				swap(nums, i, j + 1);
				j++;
			}
		}

		swap(nums, l, j);
		return j;
	}

	public static int findTopK(int[] nums, int K) {
		if (K <= 0 || K > nums.length) {
			return -1;
		}
		return findTopK(nums, 0, nums.length - 1, K);
	}

	public static int findTopK(int[] nums, int l, int r, int K) {
		
		int ans = partition(nums, l, r) ;
		if (ans+1  == K) {
			return nums[ans];
		}else if(ans+1 < K){
			return findTopK(nums, ans+1,r,K);
		}else {
			return findTopK(nums, l,ans-1,K);
		}
	}
	
	public static void main(String[] args) {
		int[] nums = new int[] {1,2,3,4,5,0};
		int ans = findTopK(nums, 4);
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
		System.out.println("ans = " + ans);
	}
}
