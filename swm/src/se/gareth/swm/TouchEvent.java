package se.gareth.swm;


class TouchEvent {
	
	enum TouchType {
		Down,
		Up,
		Move,
		Unknown,
	};
	
	public final int id;
	public final TouchType type;
	public final double x;
	public final double y;
	
	public TouchEvent() {
		type = TouchType.Unknown;
		x = 0;
		y = 0;
		id = -1;
	}
	
	public TouchEvent(TouchType touchType, int id, double x, double y) {
		this.x = x;
		this.y = y;
		this.type = touchType;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "" + getClass() + " (type=" + type + ", id=" + id + ", x=" + x + ", y=" + y + ")";
	}
}
