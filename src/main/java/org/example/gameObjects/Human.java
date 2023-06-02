package org.example.gameObjects;

import org.example.Main;
import org.example.gameObjects.interfaces.IShowableObject;

public class Human implements IShowableObject {

    public int x;
    public int height;
    public int width;


    public Human(int x, int height, int width){
        this.x = x;
        this.height = height;
        this.width = width;
    }

    @Override
    public void showObject() {
        Main.processing.strokeWeight(5);
        Main.processing.line(x, (float) (Main.getHeight()-(0.8*height)),x,(float) (Main.getHeight()-(0.4*height)));
        Main.processing.line(x,(float) (Main.getHeight()-(0.4*height)),x-(width/2),Main.getHeight());
        Main.processing.line(x,(float) (Main.getHeight()-(0.4*height)),x+(width/2),Main.getHeight());
        Main.processing.line(x,(float) (Main.getHeight()-(0.8*height)),x-(width/2)
                ,(float) (Main.getHeight()-(0.4*height)));
        Main.processing.line(x,(float) (Main.getHeight()-(0.8*height)),x+(width/2)
                ,(float) (Main.getHeight()-(0.4*height)));
        Main.processing.noStroke();
        Main.processing.fill(230, 17, 2);
        Main.processing.rect((float) (x-(width*0.2)),Main.getHeight()-height,(float)(width*0.4),(float)(width*0.4));
        Main.processing.stroke(0);
        Main.processing.strokeWeight(1);
    }

    public void moveObject(){
        x = Main.processing.mouseX;
    }
}
