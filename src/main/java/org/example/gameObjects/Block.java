package org.example.gameObjects;

import org.example.Main;
import org.example.gameObjects.interfaces.IFallingObject;
import org.example.gameObjects.interfaces.IShowableObject;

import java.util.Random;

public class Block implements IShowableObject, IFallingObject {
    public boolean check = false;
    public int x;
    public int y;
    public int width;
    public int height;
    public int r;
    public int g;
    public int b;
    public Block(int width, int height,int x,int y,int r,int g, int b,boolean check) {
        this.width = width;
        this.height= height;
        this.x = x;
        this.y = y;
        this.r = r;
        this.g = g;
        this.b = b;
        this.check = check;
    }
    public Block(int width, int height){
        this.width = width;
        this.height = height;

        do {
            x = new Random().nextInt((Main.getWidth()-width)+1);
        }while (x>(Main.lastBlockX-(2.5*width))&&x<(Main.lastBlockX+(3.5*width)));
        y = -height;
        Main.lastBlockX = x;
        Random random = new Random();
        r = random.nextInt(256);
        g = random.nextInt(256);
        b = random.nextInt(256);

        showObject();
    }

    @Override
    public boolean Fall() {
        y+=Main.speed;
        showObject();
        if (!check&& y>10){
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
