package se.gareth.swm;


public class TimeStep {

    private final AverageValue mTimeStepAverage;
	private long mLastUpdate;
	public long timeStepDiff;
	public double timeStep;
	public double timeStepSquared;

	public TimeStep() {
		mTimeStepAverage = new AverageValue(30);
		reset();
	}

	public void reset() {
		mLastUpdate = System.currentTimeMillis();
	}

	public void update() {
		long ticks = System.currentTimeMillis();
		timeStepDiff = ticks - mLastUpdate;
		mTimeStepAverage.addValue(timeStepDiff);
		mLastUpdate = ticks;
		timeStep = mTimeStepAverage.getAvarageDouble()/1000.0;
		timeStepSquared = timeStep * timeStep;
	}

	/*
	  Get time step (in seconds)
	*/
	public double get() {
		return timeStep;
	}

	/*
	  Get time step squared (in seconds)
	*/
	public double getSquared() {
		return timeStepSquared;
	}

	/*
	  Get time step (in milliseconds)
	*/
	public long getDiff() {
		return timeStepDiff;
	}
} 