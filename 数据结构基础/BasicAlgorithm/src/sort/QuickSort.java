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

		// arr[l+1....j]>pivot, arr[j+1...i)<pivot i是正在考察的元素
		int j = l;
		for (int i = l + 1; i <= r; i++) {
			if (nums[i] < pivot) {
				// j之前的全部是小于pivot的，j+1是第一个大于等于pivot的下标
				swap(nums, i, j + 1);
				j++;
			}
		}

		nums[l] = nums[j];

		nums[j] = pivot;
		return j;
	}

	// 双路快排，避免重复元素太多，导致O(n^2)
	// 把pivot重复元素尽可能分在了两侧
	private static int partition2(int[] nums, int l, int r) {
		// Pivot的选择很关键
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		int i = l + 1;// arr[i+1....i) <= pivot
		int j = r;// arr(j....r] >= pivot
		while (true) {
			while (i <= r && nums[i] < pivot)
				i++;// i会指向遍历过程中第一大于等于pivot的地方
			while (j >= l + 1 && nums[j] > pivot)
				j--;// j会指向遍历过程中第一个小于等于pivot的地方
			if (i > j) {
				break;
			}

			swap(nums, i, j);
			i++;
			j--;
		}

		swap(nums, l, j);
		return j;

	}

	// 三路快排 i下标遍历 分为3部分
	// arr[l+1...lt]<v arr[lt+1...i-1] == v arr[gt....r] > v 最后交换lt和l
	// 递归对<v和>v部分进行三路快排
	private static void qSortInThreeWays(int[] nums, int l, int r) {
		if (l >= r) {
			return;
		}

		// Pivot的选择很关键
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		int lt = l;// nums[l+1...lt]<v
		int gt = r + 1;// nums[gt...r]>v
		int i = l + 1;// nums[lt+1....i)==v i正在考察
		// 初始三个区间全是空的
		while (i < gt) {
			if (nums[i] < pivot) {
				swap(nums, i, lt + 1);
				lt++;
				i++;
			} else if (nums[i] > pivot) {
				swap(nums, i, gt - 1);
				gt--;
			} else {
				i++;
			}
		}
		
		
		swap(nums, l, lt);

		qSortInThreeWays(nums, l, lt - 1);
		qSortInThreeWays(nums, gt, r);

	}

	public static void main(String[] args) {
		int[] nums = new int[] { 2, 1, 5, 8, 8, 7, 9, 6 };
		qSort(nums);
		for (int i = 0; i < nums.length; i++) {
			System.out.println(nums[i]);
		}
	}
}
