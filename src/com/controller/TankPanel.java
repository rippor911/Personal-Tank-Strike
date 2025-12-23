package com.controller;
import java.awt.Graphics2D;

import com.Tank;

public interface TankPanel {
    public Tank getTank();

    public void update();

    public void draw(Graphics2D g2);
}
