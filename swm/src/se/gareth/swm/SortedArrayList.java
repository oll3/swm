package se.gareth.swm;

import java.util.Comparator;
import java.util.ArrayList;

public class SortedArrayList<E> extends ArrayList<E> {

	private static final String TAG = SortedArrayList.class.getName();
	private static final long serialVersionUID = 1L;
	
	private final Comparator<E> mComparator;

	public SortedArrayList(Comparator<E> c) {
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
		
		if (size() > 0) {
			int i = 0;
			if (mComparator.compare(newObject, get(size() - 1)) >= 0) {
				/* Optimize: Add straight to end of array */
				super.add(newObject);
			}
			else {
				for (i = 0; i < size(); i ++) {
					if (mComparator.compare(newObject, get(i)) < 0) {
						break;
					}
				}
				super.add(i, newObject);
			}
		}
		else {
			super.add(newObject);
		}
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