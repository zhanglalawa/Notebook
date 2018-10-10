package sort;

import java.util.Random;

public class QuickSort {
	public static void qSort(int[] nums) {
		qSort(nums, 0, nums.length - 1);
	}

	private static void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}

	private static void qSort(int[] nums, int l, int r) {// [l,...r]闭区间快速排序
		if (l >= r) {
			return;
		}

		int j = partition2(nums, l, r);
		qSort(nums, l, j - 1);
		qSort(nums, j + 1, r);
	}

	private static int partition(int[] nums, int l, int r) {
		// Pivot的选择很关键
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		// arr[l+1....j]<pivot, arr[j+1...i)>pivot i是正在考察的元素
		int j = l;
		for (int i = l + 1; i <= r; i++) {
			if (nums[i] < pivot) {
				swap(nums, i, j + 1);
				j++;
			}
		}

		nums[l] = nums[j];
		nums[j] = pivot;
		return j;
	}

	// 双路快排，避免重复元素太多，导致O(n^2)
	private static int partition2(int[] nums, int l, int r) {
		// Pivot的选择很关键
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		int i = l + 1, j = r;
		while (i < j) {
			while (nums[i] <= pivot && i<j) {
				i++;
			}

			while (nums[j] >= pivot && i<j) {
				j--;
			}
			
			if (i >= j) {
				break;
			}
			
			swap(nums, i, j);
		}
		
		nums[l] = nums[i];
		nums[i] = pivot;
		return i;
	}

	public static void main(String[] args) {
		int[] nums = new int[] { 7, 8, 8, 8, 6 };
		qSort(nums);
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
	}
}
