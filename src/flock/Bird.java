package flock;

import java.awt.*;

/**
 * Created by Sepehr on 2016-08-06.
 */
public class Bird {

    public Vector2D position, velocity;
    public double startingSpeed = 5;
    public int id;
    public Color color;

    public Bird(double x, double y, int id) {
        this.id = id;
        position = new Vector2D(x, y);
        double vx = startingSpeed - Math.random() * startingSpeed * 2;
        double vy = startingSpeed - Math.random() * startingSpeed * 2;
        velocity = new Vector2D(vx, vy);
        color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public Bird(Bird b) {
        id = b.id;
        position = b.position.clone();
        velocity = b.velocity.clone();
        color = b.color;
    }

    public boolean isInRange(Bird other, double range) {
        if (other.id == id) return false;
        if (position.getDistanceTo(other.position) <= range) return true;
        return false;
    }

    public void step() {
        position = position.add(velocity);
    }

    public void paint(Graphics g) {
        g.setColor(color);
        int x = (int) Math.round(position.getX());
        int y = (int) Math.round(position.getY());
        int dx = (int) Math.round(velocity.getX());
        int dy = (int) Math.round(velocity.getY());
        g.drawLine(x, y, x + dx, y + dy);
    }

}
