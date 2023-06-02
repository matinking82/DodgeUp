package org.example.gameObjects;

import org.example.Main;
import org.example.gameObjects.interfaces.IFallingObject;
import org.example.gameObjects.interfaces.IShowableObject;

import java.util.Random;

public class Block implements IShowableObject, IFallingObject {
    private boolean check = false;
    private int x;
    private int y;
    private int width;
    private int height;
    private int speed;
    private int r;
    private int g;
    private int b;

    public Block(int width, int height, int speed){
        this.width = width;
        this.height = height;
        this.speed = speed;

        do {
            x = new Random().nextInt((Main.getWidth()-width)+1);
        }while (x>(Main.lastBlockX-(2.5*width))&&x<(Main.lastBlockX+(3.5*width)));

        Main.lastBlockX = x;
        Random random = new Random();
        r = random.nextInt(256);
        g = random.nextInt(256);
        b = random.nextInt(256);

        showObject();
    }

    @Override
    public boolean Fall() {
        y+=speed;
        showObject();
        if (!check&& y>height*1.1){
            check = true;
            Main.canAdd = true;
        }

        if (y>Main.getHeight()-Main.human.height-height){
            if (Main.human.x>x-(Main.human.width/2)&&Main.human.x<x+width+(Main.human.width/2)){
                Main.hitBlock();
                return true;
            }
        }

        if (y>Main.getHeight()){
            return true;
        }
        return false;
    }
    @Override
    public void showObject() {
        Main.processing.fill(r,g,b);
        Main.processing.rect(x,y,width,height);
    }
}
