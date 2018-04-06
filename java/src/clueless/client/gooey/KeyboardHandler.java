/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/** @author chenz */
public class KeyboardHandler implements EventHandler<KeyEvent> {
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                System.out.println("UP");
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
            case SHIFT:
                break;
        }
    }
}
