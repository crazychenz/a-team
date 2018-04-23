/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clueless.client.gooey;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;

/** @author chenz */
public class GooeyRoom extends GooeySpace {
    public GooeyRoom(Pane pane, String name, double x, double y, Image img) {
        super(pane, name, x, y, 100, 100, "white", img);
    }
}
