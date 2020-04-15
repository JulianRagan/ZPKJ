/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.comment;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;

/**
 * Main class, handles initialization and demo display for RAD tools
 * Serves as view for controller
 * @author Julian Ragan
 */
public class Comments extends JPanel{
    private JPanel CommentDisplayPane;
    private JPanel CommentEditorPane;
    private Controller controller;
    private Model model;
    private Map<String, JComponent> registeredComponents;
    
    
    public Comments(){
        registeredComponents = new HashMap<String, JComponent>();
        CommentDisplayPane = initCommentDisplayPane();
        CommentEditorPane = initCommentEditorPane();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JScrollPane cdpScroll = new JScrollPane(CommentDisplayPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(cdpScroll);
        add(CommentEditorPane);
        model = new Model();
        controller = new Controller(this, model);
        controller.registerComponents(registeredComponents);
    }
    
    private JPanel initCommentDisplayPane(){
        JPanel retval = new JPanel();
        retval.setLayout(new BoxLayout(retval, BoxLayout.Y_AXIS));
        retval.add(new CommentDisplay("Autor Demo 01.01.2020 o 15:50 napisał: \n Demo komentarza"));
        retval.add(new CommentDisplay("Autor Demo 02.01.2020 o 10:00 napisał: \n Demo komentarza"));
        return retval;
    }
    
    private JPanel initCommentEditorPane(){
        JPanel retval = new JPanel();
        JPanel options = new JPanel();
        JPanel control = new JPanel();
        JButton btnBold = new JButton("B");
        JButton btnItalic = new JButton("I");
        JButton btnUScore = new JButton("U");
        JButton btnColorPicker = new JButton("Kolor");
        JButton btnUnorderedList = new JButton("Ul");
        JButton btnAddComment = new JButton("Dodaj");
        JTextPane editor = new JTextPane(new HTMLDocument());
        retval.setLayout(new BoxLayout(retval, BoxLayout.Y_AXIS));
        options.setLayout(new FlowLayout(FlowLayout.LEFT));
        options.add(btnBold);
        options.add(btnItalic);
        options.add(btnUScore);
        options.add(btnUnorderedList);
        options.add(btnColorPicker);
        retval.add(options);
        retval.add(editor);
        control.setLayout(new FlowLayout(FlowLayout.LEFT));
        control.add(btnAddComment);
        retval.add(control);
        registeredComponents.put("btnBold", btnBold);
        registeredComponents.put("btnItalic", btnItalic);
        registeredComponents.put("btnUScore", btnUScore);
        registeredComponents.put("btnColorPicker", btnColorPicker);
        registeredComponents.put("btnUnorderedList", btnUnorderedList);
        registeredComponents.put("btnAddComment", btnAddComment);
        registeredComponents.put("editor", editor);
        return retval;
    }

    public JPanel getCommentDisplayPane() {
        return CommentDisplayPane;
    }

    public JPanel getCommentEditorPane() {
        return CommentEditorPane;
    }

    public Controller getController() {
        return controller;
    }

    public Model getModel() {
        return model;
    }
    
}
