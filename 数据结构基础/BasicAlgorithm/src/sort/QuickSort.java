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

	private static void qSort(int[] nums, int l, int r) {// [l,...r]�������������
		if (l >= r) {
			return;
		}

		int j = partition2(nums, l, r);
		qSort(nums, l, j - 1);
		qSort(nums, j + 1, r);
	}

	private static int partition(int[] nums, int l, int r) {
		// Pivot��ѡ��ܹؼ�
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		// arr[l+1....j]>pivot, arr[j+1...i)<pivot i�����ڿ����Ԫ��
		int j = l;
		for (int i = l + 1; i <= r; i++) {
			if (nums[i] < pivot) {
				// j֮ǰ��ȫ����С��pivot�ģ�j+1�ǵ�һ�����ڵ���pivot���±�
				swap(nums, i, j + 1);
				j++;
			}
		}

		nums[l] = nums[j];

		nums[j] = pivot;
		return j;
	}

	// ˫·���ţ������ظ�Ԫ��̫�࣬����O(n^2)
	// ��pivot�ظ�Ԫ�ؾ����ܷ���������
	private static int partition2(int[] nums, int l, int r) {
		// Pivot��ѡ��ܹؼ�
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		int i = l + 1;// arr[i+1....i) <= pivot
		int j = r;// arr(j....r] >= pivot
		while (true) {
			while (i <= r && nums[i] < pivot)
				i++;// i��ָ����������е�һ���ڵ���pivot�ĵط�
			while (j >= l + 1 && nums[j] > pivot)
				j--;// j��ָ����������е�һ��С�ڵ���pivot�ĵط�
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

	// ��·���� i�±���� ��Ϊ3����
	// arr[l+1...lt]<v arr[lt+1...i-1] == v arr[gt....r] > v ��󽻻�lt��l
	// �ݹ��<v��>v���ֽ�����·����
	private static void qSortInThreeWays(int[] nums, int l, int r) {
		if (l >= r) {
			return;
		}

		// Pivot��ѡ��ܹؼ�
		Random random = new Random();
		int m = random.nextInt(r - l + 1) + l;
		swap(nums, l, m);
		int pivot = nums[l];

		int lt = l;// nums[l+1...lt]<v
		int gt = r + 1;// nums[gt...r]>v
		int i = l + 1;// nums[lt+1....i)==v i���ڿ���
		// ��ʼ��������ȫ�ǿյ�
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
