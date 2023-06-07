package org.example.gameObjects;

import org.example.Main;
import org.example.gameObjects.interfaces.IFallingObject;
import org.example.gameObjects.interfaces.IShowableObject;

public class Shield implements IShowableObject, IFallingObject {
    private int x;
    private int y;
    private int width;
    public Shield(int width){
        this.width = width;

        if (Main.lastBlockX>Main.getWidth()/2){
            x = Main.lastBlockX/2;
        }else {
            x = (Main.lastBlockX+Main.getWidth())/2;
        }
        y = -(3*width);

        showObject();
    }
    @Override
    public boolean Fall() {
        y+=Main.speed;
        showObject();
        if (y>Main.getHeight()-Main.human.height-width){
            if (x>(Main.human.x-(Main.human.width/2)-width)&& x<(Main.human.x+(Main.human.width/2))){
                Main.hitShield();
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
        Main.processing.stroke(20,255,150);
        Main.processing.fill(20,255,150);
        Main.processing.rect(x,y,width,width);
        Main.processing.stroke(0);
    }
}
