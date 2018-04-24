/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import javafx.scene.layout.Pane;

/** @author chenz */
public class GooeyHallway extends GooeySpace {

    public GooeyHallway(Pane pane, String name, double x, double y, String color) {
        super(pane, "", x, y, 50, 50, color, null);
    }
}
