package sort;

public class InsertionSort {
	public static void insertionSort(int[] nums, int l, int r) {
		for(int i = l; i < r; i++){
			 int tmp = nums[i+1];
			 int j = i+1;
			 while(j>0 && nums[j-1] > tmp){
				 nums[j] = nums[j-1];
				 j--;
			 }
			 nums[j] = tmp;
		}
			  
	}
}
