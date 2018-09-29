package sort;
/*
 * MergeSort�ĵݹ�д��
 * �и����ռ�
 * �Ż��ķ�����
 * 1.��merge֮ǰ�ж�һ�£����nums[mid] <= nums[mid+1]��û�б�Ҫmerge�ˣ����ڽ�����������飬�Ǽ��п��ܳ�����������ģ�
 * 2.�ڵݹ鵽��ײ���ʱ������ԽС�Ĳ��֣���������ĸ��ʾͻ��һЩ���������InsertionSort��ôЧ�ʾͿ�����ߣ���Ȼ��O(n^2)�����Ǹ��Ӷ�ǰ���и�������InsertionSort��n^2ǰ��ĳ���Ҫ��MergeSortСһЩ��������nС������£�Insertion���ʺ�һЩ��
 */
public class MergeSort {
	
	//merge������Ǵ���һ��l��ƫ����
	public static void merge(int[] nums, int l, int mid, int r) {
		//[l,....,r]������һ����r-l+1���ռ�
		int[] aux = new int[r - l + 1];
		
		//�����ռ临�ƺ�
		for(int i = 0; i < aux.length;i++) {
			aux[i] = nums[i+l];
		}
		
		
		//Ȼ��Ҫ����merge������
		//��aux���ص�nums����ȥ
		int newMid = mid - l;
		int left = 0;
		int right = newMid + 1;
		//[l,.....,mid]    [mid,.....,r]
		//�µ�midӦ���ǵ�mid - l +1 ��������ô�±�Ӧ����mid -1
		//����newMid = mid - l
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
	public static void mergeSort(int[] nums,int l, int r) {//[l,....r]ǰ�պ��
		//�Ż���2
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
		
		if (l >= r) {//���ݼ��ǿյĻ���ֻ��һ��Ԫ��
			return ;
		}
		int mid = l + (r -l )/2;
		mergeSort(nums, l,mid);
		mergeSort(nums,mid+1, r);
		//if(nums[mid+1]>nums[mid) �����Ż���һ
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
