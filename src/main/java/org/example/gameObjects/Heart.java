package org.example.gameObjects;

import org.example.Main;
import org.example.gameObjects.interfaces.IFallingObject;
import org.example.gameObjects.interfaces.IShowableObject;

public class Heart implements IShowableObject, IFallingObject {

    private int x;
    private int y;
    private int width;
    private int speed;
    public Heart(int width,int speed){
        this.width = width;
        this.speed = speed;

        if (Main.lastBlockX>Main.getWidth()/2){
            x = Main.lastBlockX/2;
        }else {
            x = (Main.lastBlockX+Main.getWidth())/2;
        }

        showObject();
    }
    @Override
    public boolean Fall() {
        y+=speed;
        showObject();
        if (y>Main.getHeight()-Main.human.height-width){
            if (x>(Main.human.x-(Main.human.width/2)-width)&& x<(Main.human.x+(Main.human.width/2))){
                Main.hitHeart();
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
        Main.processing.stroke(255,0,0);
        Main.processing.fill(255,0,0);
        Main.processing.rect(x,y,width,width);
        Main.processing.stroke(0);
    }
}
