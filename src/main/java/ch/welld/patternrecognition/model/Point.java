package ch.welld.patternrecognition.model;

import java.util.Comparator;
import java.util.Objects;

public class Point implements Comparable<Point> {
	
	private final int x;
	private final int y;
	
	public Point(Integer x, Integer y) {
		if (x == null) {
			throw new IllegalArgumentException("Argument \"x\" is missing");
		}
		if (x < 0) {
			throw new IllegalArgumentException("Argument \"x\" cannot be negative");
		}
		if (y == null) {
			throw new IllegalArgumentException("Argument \"y\" is missing");
		}
		if (y < 0) {
			throw new IllegalArgumentException("Argument \"y\" cannot be negative");
		}
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public double slopeWith(Point other) {
		if (other == null) {
			return Double.NaN;
		}
		if (this.equals(other)) {
			return Double.NEGATIVE_INFINITY;
		}
		if (this.y == other.y) {
			return 0.0;
		}
		if (this.x == other.x) {
			return Double.POSITIVE_INFINITY;
		}
		return (double) (other.y - this.y) / (other.x - this.x);
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public int compareTo(Point other) {
		if (other == null) {
			return -1;
		}
		if (this.equals(other)) {
			return 0;
		}
		return Comparator.comparing(Point::getX)
				.thenComparing(Point::getY)
				.compare(this, other);
	}

}
