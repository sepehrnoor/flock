package com.company;

import java.awt.*;

/**
 * Created by Sepehr on 2016-08-06.
 */
public class Bird {

    public double x, y, vx, vy, startingSpeed = 5;
    public int id;
    public double maxSpeed = 5;
    public Color color;

    public Bird(double x, double y, int id){
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = startingSpeed/2 - Math.random() * startingSpeed * 2;
        this.vy = startingSpeed/2 - Math.random() * startingSpeed * 2;
        this.color = new Color((int)(Math.random() * 255),(int)(Math.random() * 255),(int)(Math.random() * 255));
    }

    public Bird(Bird b){
        id = b.id;
        x = b.x;
        y = b.y;
        vx = b.vx;
        vy = b.vy;
        color = b.color;
    }

    public boolean isInRange(Bird other, double range){
        if (other.id == id) return false;
        if (Math.sqrt((x - other.x)*(x - other.x) + (y - other.y)*(y - other.y)) <= range) return true;
        return false;
    }

    public void step(){
        if (Math.sqrt(vx*vx + vy*vy) >= 20) {
            double angle = Math.atan2(vx,vy);
            vx = maxSpeed * Math.cos(angle);
            vy = maxSpeed * Math.sin(angle);
        }
        this.x += vx;
        this.y += vy;
    }

    public void paint(Graphics g){
        g.setColor(color);
        g.drawLine((int)x,(int)y,(int)(x+vx),(int)(y+vy));
    }

}
