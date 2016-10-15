package com.company;

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
    final int windowSize = 800;
    BufferStrategy strategy;

    public double avoidRange = 30,
            alignRange = 2000,
            repulsion = 0.25,
            centerGravity = 1,
            peerPressure = 2,
            influence = 0.3;


    List<Bird> birds = new ArrayList<>();


    public Simulation(int maxBirds) {

        for (int i = 0; i < maxBirds; i++) {
            birds.add(new Bird(Math.random() * windowSize, Math.random() * windowSize, i));
        }

        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        //System.out.println("Update was called at: " + System.currentTimeMillis());

        List<Bird> newBirds = new ArrayList<>();

        for (Bird b : birds) {
            double vxv = 0, //avoid x
                    vyv = 0, //avoid y
                    vxa = 0, //align x
                    vya = 0, //align y
                    vxc = 0, //center x
                    vyc = 0; //center y

            int vc = 0, //avoid count
                    ac = 0, //align count
                    cc = 0;

            Bird newB = new Bird(b);

            for (Bird bv : birds) {
                if (b.isInRange(bv, avoidRange)) {
                    vc++;
                    double angle = Math.atan2(bv.x - b.x, bv.y - b.y),
                            distance = Math.sqrt((bv.x - b.x) * (bv.x - b.x) + (bv.y - b.y) * (bv.y - b.y));
                    if (distance > 5) distance = 5;
                    vxv += (avoidRange - distance) / avoidRange * repulsion * Math.cos(angle);
                    vyv += (avoidRange - distance) / avoidRange * repulsion * Math.sin(angle);
                }
            }

            for (Bird ba : birds) {
                if (b.isInRange(ba, alignRange)) {
                    ac++;
                    vxa += ba.vx;
                    vya += ba.vy;
                }
            }

            for (Bird bc : birds) {
                if (bc.id != b.id) {
                    cc++;
                    vxc += bc.x;
                    vyc += bc.y;
                }
            }

            double fvx = 0,
                    fvy = 0;

            if (vc > 0) {
                fvx -= (vxv / vc);
                fvy -= (vyv / vc);
            }

            if (ac > 0) {
                fvx += (vxa / ac) * peerPressure;
                fvy += (vya / ac) * peerPressure;
            }

            if (cc > 0) {
                double averageX = (vxc / cc),
                        averageY = (vyc / cc);
                double deltaX = b.x - averageX,
                        deltaY = b.y - averageY;
                double nvx = deltaX / windowSize * centerGravity,
                        nvy = deltaY / windowSize * centerGravity;
                fvx += nvx;
                fvy += nvy;

            }

            newB.vx -= (fvx * influence);
            newB.vy -= (fvy * influence);
            newB.step();
            newBirds.add(newB);
        }

        birds = newBirds;
        repaint();
    }

    public void render() {
        //System.out.println("Render was called at: " + System.currentTimeMillis());

        do {
            do {
                Graphics graphics = strategy.getDrawGraphics();

                graphics.setColor(new Color(0, 0, 0, 55));
                graphics.fillRect(0, 0, windowSize, windowSize);
                for (Bird b : birds) {
                    b.paint(graphics);
                }

                graphics.dispose();
            } while (strategy.contentsRestored());

            strategy.show();
        } while (strategy.contentsLost());
    }

    public void shutdown(){
        running = false;
        setVisible(false);
        strategy.dispose();
        strategy = null;
        dispose();
    }

    @Override
    public void paint(Graphics g) {

    }
}
