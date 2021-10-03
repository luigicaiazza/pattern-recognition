package ch.welld.patternrecognition.model;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class LineSegment {
	
	private final Set<Point> points;
	
	public LineSegment() {
		points = new TreeSet<>();
	}
	
	public Collection<Point> getPoints() {
		return points;
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	@JsonIgnore
	public int getNumPoints() {
		return points.size();
	}

	@Override
	public int hashCode() {
		return Objects.hash(points);
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
		LineSegment other = (LineSegment) obj;
		return Objects.equals(this.points, other.points);
	}

	@Override
	public String toString() {
		return "LineSegment [points=" + points + "]";
	}

}
