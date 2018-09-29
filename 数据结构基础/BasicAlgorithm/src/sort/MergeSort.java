package sort;
/*
 * MergeSort的递归写法
 * 有辅助空间
 * 优化的方案：
 * 1.在merge之前判断一下，如果nums[mid] <= nums[mid+1]就没有必要merge了！对于近乎有序的数组，是极有可能出现这种情况的！
 * 2.在递归到快底部的时候，由于越小的部分，近乎有序的概率就会大一些，如果改用InsertionSort那么效率就可以提高（虽然是O(n^2)，但是复杂度前面有个常数，InsertionSort的n^2前面的常数要比MergeSort小一些，所以在n小的情况下，Insertion更适合一些）
 */
public class MergeSort {
	
	//merge里面就是处理一个l的偏移量
	public static void merge(int[] nums, int l, int mid, int r) {
		//[l,....,r]闭区间一共是r-l+1个空间
		int[] aux = new int[r - l + 1];
		
		//辅助空间复制好
		for(int i = 0; i < aux.length;i++) {
			aux[i] = nums[i+l];
		}
		
		
		//然后要进行merge操作了
		//把aux返回到nums里面去
		int newMid = mid - l;
		int left = 0;
		int right = newMid + 1;
		//[l,.....,mid]    [mid,.....,r]
		//新的mid应该是第mid - l +1 个数，那么下标应该是mid -1
		//所以newMid = mid - l
		//[left,...,newMid] [right,...,aux.length-1]
		
		int index = l;
		while(left <= newMid && right < aux.length ) {
			if (aux[left] <= aux[right]) {
				nums[index] = aux[left++];
				index++;
			}else {
				nums[index] = aux[right++];
				index++;
			}
		}
		
		if (left > newMid && right == aux.length) {
			return;
		}
		
		if (left > newMid) {
			for(int i = right; i < aux.length; i++) {
				nums[index] = aux[i];
				index++;
			}
		}else if (right == aux.length) {
			for(int i = left; i <= newMid;i++) {
				nums[index] = aux[i];
				index++;
			}
		}
	}
	public static void mergeSort(int[] nums,int l, int r) {//[l,....r]前闭后闭
		//优化点2
		/*
		 * if(r - l <= 15){
		 * 		for(int i = l; i < r; i++){
		 * 			int tmp = nums[i+1];
		 * 			int j = i + 1;
		 * 			while(j>0 && nums[j] > tmp){
		 * 				nums[j] = nums[j-1];
		 * 				j--;
		 * 			}
		 * 			nums[j] = tmp;
		 * 		}
		 * }
		 */
		
		if (l >= r) {//数据集是空的或者只有一个元素
			return ;
		}
		int mid = l + (r -l )/2;
		mergeSort(nums, l,mid);
		mergeSort(nums,mid+1, r);
		//if(nums[mid+1]>nums[mid) 这是优化点一
		merge(nums,l,mid,r);
	}
	
	
	
	public static void main(String[] args) {
		int[] nums = new int[] {4,5,9,6,3,2,1,5,7,8,9,-2,9,8,5,1};
		mergeSort(nums, 0, nums.length-1);
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
	}
}
