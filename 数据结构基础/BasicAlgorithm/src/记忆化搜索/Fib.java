package 记忆化搜索;

public class Fib {
	private static long[] memory;

	public static long fib(int n) {
		if (n == 0) {
			memory[n] = 1;
			return 1;
		}

		if (n == 1) {
			memory[n] = 1;
			return 1;
		}

		if (memory[n] == 0) {
			memory[n] = fib(n - 1) + fib(n - 2);
		}

		return memory[n];
	}

	/*
	 * 最简单的动态规划
	 */
	public static void fib2(int n) {
		memory[0] = 1;
		memory[1] = 1;

		for (int i = 2; i <= n; i++) {
			memory[i] = memory[i - 1] + memory[i - 2];
		}
	}

	public static void main(String[] args) {
		int num = 10;
		memory = new long[num + 1];

		fib2(num);
		for (long number : memory)
			System.out.println(number);
	}



}
