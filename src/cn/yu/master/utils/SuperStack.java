package cn.yu.master.utils;

import android.util.Log;

public class SuperStack<E> {

	private static final String TAG = "SuperStack";

	private Object[] stack;
	public static final int INIT_SIZE = 5;
	private int index;

	public SuperStack() {
		stack = new Object[INIT_SIZE];
		index = -1;
	}

	public SuperStack(int initSize) {
		if (initSize < 0) {
			throw new IllegalArgumentException();
		}
		stack = new Object[initSize];
		index = -1;
	}

	public synchronized E pop() {
		if (!isEmpty()) {
			E temp = peek();
			stack[index--] = null;
			return temp;
		}
		return null;
	}

	public synchronized void push(E obj) {
		if (isFull()) {
			// Object[] temp = stack;
			// stack = new Object[2 * stack.length];
			// System.arraycopy(temp, 0, stack, 0, temp.length);
			index = -1;
		}
		stack[++index] = obj;
		Log.e(TAG, "push to index " + index);
	}

	public E peek() {
		if (!isEmpty()) {
			return (E) stack[index];
		}
		return null;
	}

	public E get(int index) {
		if (!isEmpty()) {
			return (E) stack[index];
		}
		return null;
	}

	public boolean isEmpty() {
		return index == -1;
	}

	public boolean isFull() {
		return index >= stack.length - 1;
	}
}
