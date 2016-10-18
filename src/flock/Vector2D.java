package flock;

/**
 * Created by sepehr on 2016-10-18.
 * Immutable 2D vector for use with boids
 */

public class Vector2D {
    final double x, y;

    public static Vector2D makeVectorFromPoints(double x1, double y1, double x2, double y2) {
        return new Vector2D(x2 - x1, y2 - y1);
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLength() {
        return Math.hypot(x, y);
    }

    public Vector2D clip(double maxLength) {
        if (getLength() <= maxLength) {
            return clone();
        }
        double angle = Math.atan2(getY(), getX());
        return new Vector2D(maxLength * Math.cos(angle), maxLength * Math.sin(angle));
    }

    public double getDistanceTo(Vector2D other) {
        return difference(other).getLength();
    }

    public Vector2D difference(Vector2D other) {
        return new Vector2D(
                other.getX() - getX(),
                other.getY() - getY()
        );
    }

    public Vector2D scale(double factor) {
        return new Vector2D(x * factor, y * factor);
    }

    public Vector2D getInverse() {
        return scale(-1);
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(
                getX() + other.getX(),
                getY() + other.getY()
        );
    }

    public Vector2D clone() {
        return new Vector2D(getX(), getY());
    }
}
