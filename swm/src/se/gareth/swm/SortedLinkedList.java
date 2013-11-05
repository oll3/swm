package se.gareth.swm;

import java.util.Comparator;
import java.util.LinkedList;

import android.util.Log;

public class SortedLinkedList<E> extends LinkedList<E> {

	private static final String TAG = SortedLinkedList.class.getName();
	private static final long serialVersionUID = 1L;
	
	private final LinkedList<E> mTmpList;
	private final Comparator<E> mComparator;

	public SortedLinkedList(Comparator<E> c) {
		mTmpList = new LinkedList<E>();
		mComparator = c;
	}

	@Override
	public boolean add(E newObject) {
		return addSort(newObject);
	}
	
	
	/*
	 * Add element to list and keep list sorted
	 */
E	public boolean addSort(E newObject) {
		while (size() > 0) {
			if (mComparator.compare(newObject, getFirst()) < 0) {
				break;
			}
			mTmpList.add(removeFirst());
		}
		
		mTmpList.add(newObject);
		super.addAll(0, mTmpList);
		mTmpList.clear();
		return true;
	}
	
	@Override
	public String toString() {
		String string = "[";
		for (E obj: this) {
			string += obj;
			if (obj != getLast()) {
				string += ",";
			}
		}
		string += "]";
		return string;
	}

}