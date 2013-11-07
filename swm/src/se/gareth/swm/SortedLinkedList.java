package se.gareth.swm;

import java.util.Comparator;
import java.util.ArrayList;

import android.util.Log;

public class SortedLinkedList<E> extends ArrayList<E> {

	private static final String TAG = SortedLinkedList.class.getName();
	private static final long serialVersionUID = 1L;
	
	private final Comparator<E> mComparator;

	public SortedLinkedList(Comparator<E> c) {
		mComparator = c;
	}

	@Override
	public boolean add(E newObject) {
		return addSort(newObject);
	}
	
	
	/*
	 * Add element to list and keep list sorted
	 */
	public boolean addSort(E newObject) {
		
		int i;
		if (mComparator.compare(newObject, get(size() - 1)) >= 0) {
			/* Optimize: Add straight to end of array */
			i = size();
		}
		else {
			for (i = 0; i < size(); i ++) {
				if (mComparator.compare(newObject, get(i)) < 0) {
					break;
				}
			}
		}
		super.add(i, newObject);
		return true;
	}
	
	@Override
	public String toString() {
		String string = "[";
		for (int i = 0; i < size(); i ++) {
			string += get(i);
			if (i != size()) {
				string += ",";
			}
		}
		string += "]";
		return string;
	}

}