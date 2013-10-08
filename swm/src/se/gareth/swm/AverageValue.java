package se.gareth.swm;


public class AverageValue {

	private final int mMaxNumValues;
    private final long mValueArray[];
    private int mIndexValue;
    private int mNumValues;
    private long mAverage;
	
    
    public AverageValue(int numValues) {
    	mMaxNumValues = numValues;
    	mValueArray = new long[mMaxNumValues];

    	reset();
    }
    
    public void reset() {
    	for (int i = 0; i < mMaxNumValues; i ++) {
    		mValueArray[i] = 0;
    	}
    	mAverage = 0;
    	mIndexValue = 0;
    	mNumValues = 0;
    }
    
    
    public void addValue(long value) {
    	if (mNumValues >= mMaxNumValues) {
    		mAverage -= mValueArray[mIndexValue];
    		
    	}
    	mValueArray[mIndexValue] = value;
    	mAverage += value;
    	
    	mIndexValue ++;
    	if (mIndexValue >= mMaxNumValues) {
    		mIndexValue = 0;
    	}
    	
    	if (mNumValues < mMaxNumValues) {
    		mNumValues ++;
    	}
    }
    
    public double getAvarageDouble() {
    	return (double)mAverage/(double)mNumValues;
    }
    
    public long getAverage() {
    	return mAverage/mNumValues;
    }
}
