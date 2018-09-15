package heap;

import java.util.Random;

public class Main {
	public static void main(String[] args) {
		int n = 1000000;
		MaxHeap<Integer> maxHeap = new MaxHeap<>();
		
		Random random = new Random();
		
		for(int i = 0; i < n; i++) {
			maxHeap.add(random.nextInt(Integer.MAX_VALUE));
		}
		
		for(int i = 0; i < n; i++) {
			System.out.println(maxHeap.extractMax());
		}
	}
}
