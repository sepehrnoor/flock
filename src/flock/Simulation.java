package flock;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sepehr on 2016-08-06.
 */
public class Simulation extends JFrame implements Runnable {

    boolean running = true;
    long lastUpdateTime = 0;
    final long frameWait = 17;
    final int windowSize = 1000;
    boolean windowBounce = true;
    public double maxSpeed = 20;
    BufferStrategy strategy;

    public double avoidRange = 10,
            alignRange = 2000,
            totalMult = 1.0;

    List<Bird> birds = new ArrayList<>();


    public Simulation(int maxBirds) {
        for (int i = 0; i < maxBirds; i++) {
            birds.add(new Bird(Math.random() * windowSize, Math.random() * windowSize, i));
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(windowSize, windowSize);
        setVisible(true);
        createBufferStrategy(2);
        strategy = getBufferStrategy();
    }


    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            long deltaTime = currentTime - lastUpdateTime;
            if (deltaTime > frameWait && running) {
                update();
                render();
                lastUpdateTime = currentTime;
            }
        }
    }

    void update() {
        List<Bird> newBirds = new ArrayList<>();

        for (Bird b : birds) {
            Bird newB = new Bird(b);
            newB.velocity = newB.velocity.add(getAlignmentVector(newB));
            newB.velocity = newB.velocity.add(getAvoidanceVector(newB));
            newB.velocity = newB.velocity.add(getGravityVector(newB));
            newB.velocity = newB.velocity.scale(totalMult);
            newB.velocity = newB.velocity.clip(maxSpeed);
            newB.step();
            if (windowBounce) {
                if (newB.position.getX() < 0) {
                    newB.position = new Vector2D(0, newB.position.getY());
                    newB.velocity = new Vector2D(-newB.velocity.getX(), newB.velocity.getY());
                }

                if (newB.position.getX() > windowSize) {
                    newB.position = new Vector2D(windowSize, newB.position.getY());
                    newB.velocity = new Vector2D(-newB.velocity.getX(), newB.velocity.getY());
                }

                if (newB.position.getY() < 0) {
                    newB.position = new Vector2D(newB.position.getX(), 0);
                    newB.velocity = new Vector2D(newB.velocity.getX(), -newB.velocity.getY());
                }

                if (newB.position.getY() > windowSize) {
                    newB.position = new Vector2D(newB.position.getX(), windowSize);
                    newB.velocity = new Vector2D(newB.velocity.getX(), -newB.velocity.getY());
                }
            }
            newBirds.add(newB);
        }

        birds = newBirds;
    }

    Vector2D getAvoidanceVector(Bird b) {
        Vector2D c = new Vector2D(0, 0);
        List<Bird> birdsInRange = getBirdsInRange(b, avoidRange);
        for (Bird other : birdsInRange) {
            c = c.add(b.position.difference(other.position)); //generate one vector for each, should be a pretty strong push
        }
        return c;
    }

    Vector2D getAlignmentVector(Bird b) {
        Vector2D c = new Vector2D(0, 0);
        List<Bird> birdsInRange = getBirdsInRange(b, alignRange);
        for (Bird other : birdsInRange) {
            c = c.add(other.velocity);
        }
        c = c.scale(1.0 / ((double) birdsInRange.size())); //get the average
        return c.scale(1.0 / 8.0);   //return an eighth for balance
    }

    Vector2D getGravityVector(Bird b) {
        return b.position.difference(centerVector(b)).scale(1.0 / 100); //gain momentum towards middle of flock
    }

    List<Bird> getBirdsInRange(Bird b, double range) {
        List<Bird> inRange = new ArrayList<>();
        for (Bird currentBird : birds) {
            if (b.isInRange(currentBird, range)) {
                inRange.add(currentBird);
            }
        }
        return inRange;
    }


    public Vector2D centerVector(Bird b) {
        List<Bird> bs = getBirdsInRange(b, Double.MAX_VALUE);
        Vector2D newVector = new Vector2D(0, 0);
        for (Bird other : bs) {
            newVector = newVector.add(other.position);
        }
        newVector = newVector.scale(1.0 / ((double) bs.size())); //get average of points
        return newVector;
    }

    public void render() {
        do {
            do {
                Graphics graphics = strategy.getDrawGraphics();

                graphics.setColor(new Color(0, 0, 0));
                graphics.fillRect(0, 0, windowSize, windowSize);
                for (Bird b : birds) {
                    b.paint(graphics);
                }

                graphics.dispose();
            } while (strategy.contentsRestored());

            strategy.show();
        } while (strategy.contentsLost());
    }

    public void shutdown() {
        running = false;
        setVisible(false);
        strategy.dispose();
        strategy = null;
        dispose();
    }

}
