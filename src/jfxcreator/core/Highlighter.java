/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jfxcreator.core;

import jfxcreator.view.Editor;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

/**
 *
 * @author Aniket
 */
public class Highlighter {

    public static void highlight(CodeArea area, Editor ed) {
        if (ed.getScript().getType() == Program.JAVA) {
            JavaKeywordsAsync as = new JavaKeywordsAsync(area);
            as.apply();
        } else if (isSupported(ed.getScript().getFile().getFileName().toString())) {
            KeywordsAsync ka = new KeywordsAsync(area);
            ka.setKeywords(getKeywords(ed.getScript().getFile().getFileName().toString()));
            ka.apply();
        } else {
            String name = ed.getScript().getFile().getFileName().toString();
            if (name.contains(".")) {
                String extension = name.substring(name.indexOf('.') + 1);
                if (extension.endsWith("ml")) {
                    TagHighlighter th = new TagHighlighter(area, ed);
                    th.apply();
                }
            }
        }
    }

    public static void highlight(CodeArea area) {
        JavaKeywordsAsync as = new JavaKeywordsAsync(area);
        area.setParagraphGraphicFactory(LineNumberFactory.get(area));
        as.apply();
    }

    private static boolean isSupported(String s) {
        return s.endsWith(".js")
                || s.endsWith(".c")
                || s.endsWith(".h");
    }

    private static String[] getKeywords(String s) {
        if (s.endsWith(".js")) {
            return JAVASCRIPT_KEYWORDS;
        }
        return null;
    }

    private static final String[] JAVASCRIPT_KEYWORDS = new String[]{
        "abstract", "arguments", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "debugger", "default", "delete", "do",
        "double", "else", "enum", "eval", "export",
        "extends", "false", "final", "finally", "float",
        "for", "function", "goto", "if", "implements",
        "import", "in", "instanceof", "int", "interface",
        "let", "long", "native", "new", "null",
        "package", "private", "protected", "public", "return",
        "short", "static", "super", "switch", "synchronized",
        "this", "throw", "throws", "transient", "true",
        "try", "typeof", "var", "void", "volatile",
        "while", "with", "yield"};
}
