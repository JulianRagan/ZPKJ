/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.comment;

import com.jr.data.CommentContainer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Julian Ragan
 */
public class Controller {
    private Comments view;
    private Model model;
    private CommentProvider cp;
    private Map<String, JComponent> registeredComponents;
    private int authorId;
    private String authorFullName;

    public Controller(Comments view, Model model) {
        this.view = view;
        this.model = model;
    }
    
    public void init(){
        view.getCommentDisplayPane().removeAll();
    }
    
    public void registerComponents(Map<String, JComponent> registeredComponents){
        this.registeredComponents = registeredComponents;
        initComponentControl();
    }
    
    public void registerCommentProvider(CommentProvider cp){
        this.cp = cp;
        loadComments();
    }
    
    public void registerAuthor(int id, String fullName){
        authorId = id;
        authorFullName = fullName;
    }
    
    private void loadComments(){
        if(cp.next()){
            do{
                CommentContainer cc = new CommentContainer();
                cc.setPlainTextContent(cp.getPlainTextContent());
                cc.setHtmlContent(cp.getPlainTextContent());
                cc.setAuthor(cp.getAuthor());
                cc.setLastEditAuthor(cp.getLastEditAuthor());
                cc.setAuthorId(cp.getAuthorId());
                cc.setLastEditAuthorId(cp.getLastEditAuthorId());
                cc.setCommentId(cp.getCommentId());
                cc.setReferencedId(cp.getReferencedId());
                cc.setRefStart(cp.getRefStart());
                cc.setRefStop(cp.getRefStop());
                cc.setTimestamp(cp.getTimestamp());
                cc.setLastEditTimestamp(cp.getLastEditTimestamp());
                model.addRawComment(cc);
            }while(cp.next());
            //TODO trigger view update, create document for display
            //view.add(new CommentDisplay(doc))
        }
    }
    
    public void addComment(CommentContainer cc){
        cc.setCommentId(cp.addComment(cc));
        model.addRawComment(cc);
        //TODO trigger view update, create document for display
        //view.add(new CommentDisplay(doc))
    }
    
    private void initComponentControl(){
        JTextPane editor = (JTextPane)registeredComponents.get("editor");
        for(Map.Entry<String,JComponent> entry : registeredComponents.entrySet()){
            switch(entry.getKey()){
                case "btnBold":
                case "btnItalic":
                case "btnUScore":
                case "btnColorPicker":
                case "btnUnorderedList":
                    JButton btn = (JButton)entry.getValue();
                    btn.addActionListener(new EditorManipulationActionListener(entry.getKey(), editor));
                    break;
            }
        }
    }
    
    
    class EditorManipulationActionListener implements ActionListener{
        private String buttonName;
        private JTextPane editor;
        
        public EditorManipulationActionListener(String buttonName, JTextPane editor){
            this.buttonName = buttonName;
            this.editor = editor;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean selection = false;
            if(editor.getSelectionStart() != editor.getSelectionEnd()){
                selection = true;
            }
            StyledDocument doc = editor.getStyledDocument();
            switch(buttonName){
                case "btnBold":
                    if(selection){
                        Element el = doc.getCharacterElement(editor.getSelectionStart());
                        AttributeSet as = el.getAttributes();
                        MutableAttributeSet mas = new SimpleAttributeSet(as.copyAttributes());
                        StyleConstants.setBold(mas, !StyleConstants.isBold(as));
                        doc.setCharacterAttributes(editor.getSelectionStart(), editor.getSelectedText().length(), mas, true);
                    }else{
                        
                    }
                    break;
                case "btnItalic":
                    break;
                case "btnUScore":
                    break;
                case "btnColorPicker":
                    break;
                case "btnUnorderedList":
                    break;
            }
        }
        
    }
}
