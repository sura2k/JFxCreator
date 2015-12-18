/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcreator.view;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jfxcreator.core.Program;
import jfxcreator.core.Project;
import net.sf.image4j.codec.ico.ICODecoder;

/**
 *
 * @author Aniket
 */
public class Viewer extends EnvironmentTab {

    public Viewer(Program scr, Project pro) {
        super(scr, pro);
        if (scr.getFile().toAbsolutePath().toString().endsWith(".ico")) {
            Image ico = null;
            try {
                ico = SwingFXUtils.toFXImage(ICODecoder.read(scr.getFile().toFile()).get(0), null);
            } catch (Exception ex) {
            }
            if (ico != null) {
                getCenter().setCenter(new ScrollPane(new ImageView(ico)));
            }
        } else {
            getCenter().setCenter(new ScrollPane(new ImageView(scr.getFile().toUri().toString())));
        }
    }

}
