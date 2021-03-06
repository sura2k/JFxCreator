/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tachyon.view;

import java.io.InputStream;
import java.util.Scanner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.fxmisc.richtext.CodeArea;
import tachyon.framework.core.Project;
import tachyon.features.Highlighter;
import tachyon.view.EnvironmentTab;
import tachyon.view.Writer;

/**
 *
 * @author Aniket
 */
public class ClassReader extends EnvironmentTab {

    private final CodeArea area;

    public ClassReader(Project pro, String name, InputStream input) {
        super(null, pro);
        getGraph().setText(name);
        area = new CodeArea();
        area.setEditable(false);
        area.setStyle("-fx-font-size:" + Writer.fontSize.get().getSize() + ";");
        Writer.fontSize.addListener((ob, older, newer) -> {
            area.setStyle("-fx-font-size:" + newer.getSize() + ";");
        });
        Writer.wrapText.addListener((ob, older, neweer) -> {
            area.setWrapText(neweer);
        });
        getCenter().setCenter(area);
        HBox hb = new HBox(15);
        hb.setAlignment(Pos.CENTER_RIGHT);
        hb.setPadding(new Insets(5, 10, 5, 10));
        getCenter().setBottom(hb);
        Label caret;
        hb.getChildren().add(caret = new Label(""));
        area.caretPositionProperty().addListener((ob, older, newer) -> {
            caret.setText(getRow(area.getCaretPosition()) + ":" + area.getCaretColumn());
        });
        bindMouseListeners();
        readInput(input);
    }

    private void readInput(InputStream is) {
        StringBuilder sb = new StringBuilder();
        Scanner in = new Scanner(is);
        while (in.hasNextLine()) {
            sb.append(in.nextLine()).append("\n");
        }
        area.appendText(sb.toString());
    }

    public CodeArea getCodeArea() {
        return area;
    }

    private void bindMouseListeners() {
        Highlighter.highlight(area);
        area.setOnKeyPressed((e) -> {
            if ((e.isControlDown()) && e.getCode() == KeyCode.F) {
                HBox box = new HBox(15);
                BorderPane main = new BorderPane(box);
                box.setPadding(new Insets(5, 10, 5, 10));
                box.setStyle("-fx-background-fill:gray;");
                TextField fi;
                Button prev, next;
                box.getChildren().addAll(fi = new TextField(),
                        prev = new Button("Previous"),
                        next = new Button("Next"));
                fi.setPromptText("Find");
                fi.setOnAction((ea) -> {
                    next.fire();
                });
                prev.setOnAction((efd) -> {
                    int start = area.getSelection().getStart();
                    String a = area.getText().substring(0, start);
                    int index = a.lastIndexOf(fi.getText());
                    if (index != -1) {
                        area.selectRange(index, index + fi.getText().length());
                    }
                });
                next.setOnAction((sdfsdfsd) -> {
                    if (area.getSelection().getLength() == 0) {
                        String a = fi.getText();
                        int index = area.getText().indexOf(a);
                        if (index != -1) {
                            area.selectRange(index, index + a.length());
                        }
                    } else {
                        int end = area.getSelection().getEnd();
                        String a = area.getText().substring(end);
                        int index = a.indexOf(fi.getText());
                        if (index != -1) {
                            index += end;
                            area.selectRange(index, index + fi.getText().length());
                        }
                    }
                });
                Button close;
                main.setRight(close = new Button("X"));
                BorderPane.setMargin(main.getRight(), new Insets(5, 10, 5, 10));
                getCenter().setBottom(main);
                fi.requestFocus();
                close.setOnAction((se) -> {
                    getCenter().setBottom(null);
                });
            }
        });
    }

    private int getRow(int caret) {
        String spl[] = area.getText().split("\n");
        int count = 0;
        for (int x = 0; x < spl.length; x++) {
            count += spl[x].length() + 1;
            if (caret <= count) {
                return x + 1;
            }
        }
        return -1;
    }

}
