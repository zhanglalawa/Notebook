算法分析还有三周考试。。感觉有必要整理一下基础知识，以便通过考试hhhhh。
## 快速排序
影响快排性能的几个因素，边看代码，边看这里的总结。
- **pivot的选取**
  如果pivot直接去选l下标的元素，那么在数组近乎有序的情况下，递归树的高度可能接近n，就导致最后的复杂到到了O(n^2)，这是不可接受的，所以在选取pivot时候，我们用random随机数的方法，这样出现上述情况的概率会小很多。如partition中那样写就可以了，**nums[l+1.....j]<pivot**，用一个i遍历，发现小于pivot的就和j+1位置swap即可。
- **数组本身属性** 是否近乎有序 /是否有重复元素
  实际上上面pivot的选取也可以归到这一点，因为就是为了避免当数组近乎有序的情况下递归深度过大，才随机选取的pivot。那么上述的pivot也只能解决近乎有序的情况。而如果数组中存在大量重复元素，在partition中，我们是把数组分成了两半，这样就导致一个问题，大量重复元素集中于右侧（左侧都是nums[i]<pivot，所以右侧集中了nums[i] == pivot的元素）那么依然会造成递归深度过大的情况，快排退化到O(n^2)了。

  所以partition1中提供了双路快排的思路。左侧遇到 **nums[i] < pivot** 一直右移i，右侧遇到**nums[j] > pivot** 一直左移j，那么**右移结束的i的位置**就是第一个大于等于pivot的数，而**左移结束的j的位置**就是第一个小于等于pivot的数，不管是等于还是大于小于，这时候交换i和j位置的数，循环结束时候，返回j的值作为partition的index，那么这样一来等于pivot的数集中于某一侧的概率就大大降低了。
![双路快排示意图](https://upload-images.jianshu.io/upload_images/13852523-480690918cdb79a3.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![真正是含有等于号的，这样即便是等号也会交换，也就避免了出现等于pivot的数集中在某一侧](https://upload-images.jianshu.io/upload_images/13852523-5e75f100d8307750.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


  当然还有更优化的做法，我们采用三路快排的思路，三路快排把数组分成了三个部分，根据pivot分为小于的和等于的还有大于的。用i去遍历，涉及分界点lt和gt **nums[l....lt] < pivot nums[lt+1...i)==pivot i是正在遍历考察的元素 nums[gt.....r] > pivot**，递归只针对第一个和第三个部分。哎，人类真是聪明.....下面是示意图。具体实现在代码qSortInThreeWays中。 
![三路快排示意图](https://upload-images.jianshu.io/upload_images/13852523-8b87ec9f0bc94756.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

```Java
  class QuickSort {
		public static void swap(int[] nums, int i , int j) {
			int tmp = nums[i];
			nums[i] = nums[j];
			nums[j] = tmp;
		}
		public static int partition(int[] nums, int l, int r) {
			Random random = new Random();
			int randomIndex = random.nextInt(r-l+1)+l;
			swap(nums, l, randomIndex);
			int pivot = nums[l];
			int j = l;
			for(int i = l +1; i <= r; i++) {
				if (nums[i] < pivot) {
					swap(nums, i, j+1);
					j++;
				}
			}
			swap(nums, l, j);
			return j;
		}
		
		public static int partiton1(int[] nums, int l, int r) {
			Random random = new Random();
			int randomIndex = random.nextInt(r-l+1)+l;
			swap(nums, l, randomIndex);
			int pivot = nums[l];
			int i = l+1;
			int j = r;
			while(true) {
				while(i <= r && nums[i] < pivot) i++;
				while(j >= l+1 && nums[j] > pivot) j--;
				if (i>=j) {
					break;
				}
				swap(nums, i, j);
				
				i++;
				j--;
			}
			swap(nums, l, j);
			return j;
		}
		
		public static void qSortInThreeWays(int [] nums, int l, int r) {
			if (l >= r) {
				return;
			}
			
			Random random = new Random();
			int randomIndex = random.nextInt(r-l+1)+l;
			swap(nums, l, randomIndex);
			
			int pivot = nums[l];
			
			int lt = l;//nums[l+1...lt]<pivot
			int gt = r+1;//nums[gt...r]>pivot
			int i = l+1;//nums[lt+1...i)==pivot
			
			while(i < gt) {
				if (nums[i]<pivot) {
					swap(nums, i, lt+1);
					lt++;
					i++;
				}else if (nums[i] > pivot) {
					swap(nums, i, gt-1);
					gt--;
				}else {
					i++;
				}
			}
			swap(nums, l, lt);
			qSortInThreeWays(nums, l, lt-1);
			qSortInThreeWays(nums, gt, r);
			
		}
		public static void qSort(int[] nums) {
			qSort(nums,0,nums.length-1);
		}
		
		public static void qSort(int[] nums,int l, int r) {
			if (l >= r) {
			return;
		}
			
			int index = partition(nums, l, r);
			qSort(nums,l,index-1);
			qSort(nums,index+1,r);
		}
	}
```

## 归并排序
归并排序的时间性能相对快排就要稳定许多，每次都是对半分，所以是稳定的O(nlogn）,但是需要**O(n)的辅助空间**。copy辅助空间再copy回来，可以想象在复杂度表达式中，nlogn后面的其他项还是很多的，这也是为什么大多数情况下qSort会表现更优秀一些的原因。
MergeSort有两种实现方法，自顶向下的递归写法，和自底向上的迭代写法。
最最重要的就是那个merge函数。自顶向下的递归代码见下：
```Java
class MergeSort{
	private static void merge(int[] nums, int l, int mid, int r) {
		int[] assit = new int[r-l+1];
		
		int i = 0;
		int newMid = i + mid - l;
		int j = newMid+1;
		int assitIndex = 0;
		while(i<= newMid && j <= assit.length-1) {
			if (nums[l+i]<nums[l+j]) {
				assit[assitIndex++] = nums[l+i];
				i++;
			}else {
				assit[assitIndex++] = nums[l+j];
				j++;
			}
		}
		
		if (i > newMid && j > assit.length-1) {
			return;
		}
		
		if (i <= newMid) {
			while(i <= newMid) {
				assit[assitIndex++] = nums[l+i];
				i++;
			}
		} else {
			while(j <= assit.length-1) {
				assit[assitIndex++] = nums[l+j];
				j++;
			}
		}
		
		for(int k = 0; k < assitIndex; k++) {
			nums[l+k] = assit[k];
		}
	}
	
	public static void mergeSort(int[] nums) {
		mergeSort(nums, 0, nums.length-1);
	}
	
	public static void mergeSort(int[] nums, int l, int r) {
		if (l >= r) {
			return;
		}
		int mid = l + (r - l) / 2;
		mergeSort(nums,l,mid);
		mergeSort(nums,mid+1,r);
		merge(nums, l, mid, r);
	}
}
```
下面是自底向上的迭代写法：sz就是分割的数组长度
```Java
    public static void mergeSortBottomToUp(int[] nums) {
		//[i,....i+sz-1] [i+sz....i+2sz-1] [i+2sz....
		for(int sz = 2; sz <= nums.length; sz += sz) {
			for(int i = 0; i + sz < nums.length; i += 2*sz) {
				merge(nums,i, i+sz-1, Math.min(i+2*sz-1, nums.length-1));
			}
		}
	}
```

最后我们再谈两个qSort和mSort的partition和merge函数的其他用处。

## 使用partition函数在O(n)时间内找到数组中第K大的元素

想找到数组中第K大的元素，我们第一感觉就是降序排序，然后直接看下标k-1，这样的时间复杂度就是O(nlogn)，而利用partition函数，我们可以在O(n)时间内解决这个问题。partition函数的作用就是把pivot归位到它本应该在的位置上面，我们的思路就是，对一个数组按照快排(降序)的模式进行partition，如果partition过程中得到的下标index+1和K相等，那么就找到了这个元素。而做到O(n)的关键是，我们会对每次partition返回的下标进行检查，+1后和K比较，如果大于K，那么我们知道要目标元素肯定在index左边，只去考察l到index-1即可。如果小于K，那么目标元素肯定在index右边，只去考察index+1到r即可。每次可以近似是n+n/2+n/4+n/8+n/16.....这样最后的结果用等比数列求和就是O(2n）
```Java
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
```

## 使用merge函数在O(nlogn)时间内解决逆序对问题
逆序对的定义百度吧，要找到逆序对，暴力解法就是O(n^2），一个一个去遍历。不过我们可以利用mergeSort，在merge的过程中就是找逆序对数的好时机。我们可以设置一个counter。由于merge操作的基本思想是认为merge的两段是排好序的。然后merge的时候，后半段如果先于前半段进入辅助空间，那么counter就要干活了，前半段包括目前正在考察的元素到mid之间全部都可以和后半段正在考察的这个元素构成逆序对，计算下标可以得出counter加的数值，而前半段如果先于后半段进入辅助空间，那么counter就不考虑++。