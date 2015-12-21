/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcreator;

import de.codecentric.centerdevice.MenuToolkit;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jfxcreator.core.ProjectTree;
import jfxcreator.view.Dependencies;
import jfxcreator.view.Writer;

/**
 *
 * @author Aniket
 */
public class JFxCreator extends Application {

    public static final String OS = System.getProperty("os.name").toLowerCase();
    public static final String stylesheet = JFxCreator.class.getResource(OS.contains("win") ? "" : "mac_os.css").toExternalForm();
    public static final Image icon = new Image(JFxCreator.class.getResourceAsStream("icon.png"));
    public static HostServices host;

    @Override
    public void start(Stage env) {
        host = getHostServices();
        Writer script;
        env.setScene(new Scene(script = new Writer()));
        env.getScene().getStylesheets().add(getClass().getResource("java-keywords.css").toExternalForm());
        env.setOnCloseRequest((e) -> {
            if (!script.processCheck()) {
                e.consume();
                return;
            }
            if (script.canSave()) {
                Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                al.setHeaderText("Would you like to save before closing?");
                al.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL, ButtonType.NO);
                ((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(icon);
                al.initOwner(env);
                Optional<ButtonType> show = al.showAndWait();
                if (show.isPresent()) {
                    if (show.get() == ButtonType.OK) {
                        script.saveAll();
                    } else if (show.get() == ButtonType.CANCEL) {
                        e.consume();
                        return;
                    }
                }
            }
            script.saveOpenProjectsInformation();
            ProjectTree.getTree().close();
            Platform.exit();
            System.exit(0);
        });
        env.setTitle("JFxCreator");
        Writer.currentProject.addListener((ob, older, newer) -> {
            if (newer != null) {
                env.setTitle("JFxCreator - " + newer.getProjectName());
            } else {
                env.setTitle("JFxCreator");
            }
        });
        env.setMaximized(true);
        if (OS.contains("mac")) {
            env.setFullScreen(true);
            env.getScene().getStylesheets().add(stylesheet);
        }
        env.getIcons().add(icon);
        env.show();
        setMenuBar(script);
        Dependencies.load(env);
    }

    private void setMenuBar(Writer script) {
        if (OS.contains("mac")) {
            MenuToolkit tk = MenuToolkit.toolkit();
            Menu def = tk.createDefaultApplicationMenu("JFxCreator");
            tk.setApplicationMenu(def);
            def.getItems().get(3).setOnAction((E) -> {
                if (!script.processCheck()) {
                    E.consume();
                    return;
                }
                if (script.canSave()) {
                    Alert al = new Alert(Alert.AlertType.CONFIRMATION);
                    al.setHeaderText("Would you like to save before closing?");
                    al.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL, ButtonType.NO);
                    ((Stage) al.getDialogPane().getScene().getWindow()).getIcons().add(icon);
                    al.initOwner(script.getScene().getWindow());
                    Optional<ButtonType> show = al.showAndWait();
                    if (show.isPresent()) {
                        if (show.get() == ButtonType.OK) {
                            script.saveAll();
                        } else if (show.get() == ButtonType.CANCEL) {
                            E.consume();
                            return;
                        }
                    }
                }
                script.saveOpenProjectsInformation();
                ProjectTree.getTree().close();
                Platform.exit();
                System.exit(0);
            });
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }
        launch(args);
    }

}
