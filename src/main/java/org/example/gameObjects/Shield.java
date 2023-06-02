package org.example.gameObjects;

import org.example.gameObjects.interfaces.IFallingObject;
import org.example.gameObjects.interfaces.IShowableObject;

public class Shield implements IShowableObject, IFallingObject {
    @Override
    public boolean Fall() {
return true;
    }

    @Override
    public void showObject() {

    }
}
