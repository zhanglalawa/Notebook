package queue;

import java.util.ArrayList;
import java.util.List;

import array.Array;

public class LoopQueue<E> implements Queue<E> {
	private E[] data;
	private int front, tail;
	private int size;

	public LoopQueue(int capacity) {
		data = (E[]) new Object[capacity];
		size = 0;
		front = tail = 0;
	}

	public LoopQueue() {
		this(10);
	}

	@Override
	public int getSize() {
		return size;
	}

	public int getCapacity() {
		return data.length - 1;
	}

	// 我們空一個位置出來，front == tail 表示空了，如果(tail+1)%data.length = front就説明滿了
	// 不然是没有办法区分满了还是空的
	@Override
	public boolean isEmpty() {
		return front == tail;
	}

	@Override
	public void enqueue(E e) {
		if ((tail + 1) % data.length == front) {
			resize(getCapacity() * 2);
		}

		data[tail] = e;
		tail = (tail+1)%data.length;
		size++;
	}

	@Override
	public E dequeue() {
		if (isEmpty()) {
			throw new IllegalArgumentException("Cannot dequeue from an empty queue.");
		}
		E ret = data[front];
		data[front] = null;
		front = (front + 1) % data.length;
		size--;
		if (size == getCapacity() / 4 && getCapacity() / 2 != 0) {
			resize(getCapacity() / 2);
		}
		return ret;
	}

	@Override
	public E getFront() {
		if (isEmpty()) {
			throw new IllegalArgumentException("Queue is empty");
		}
		return data[front];
	}

	private void resize(int newCapacity) {
		E[] newData = (E[]) new Object[newCapacity + 1];
		for (int i = 0; i < size; i++) {
			newData[i] = data[(i + front) % data.length];
		}
		data = newData;
		front = 0;
		tail = size;
	}
	
	public static void main(String[] args){
        LoopQueue<Integer> queue = new LoopQueue<>(5);
        for(int i = 0 ; i < 10 ; i ++){
            queue.enqueue(i);
            System.out.println(queue);

            if(i % 3 == 2){
                queue.dequeue();
                System.out.println(queue);
            }
        }  
    }
}
