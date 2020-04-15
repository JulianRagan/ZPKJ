/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.comment;

import com.jr.extra.CommentBorder;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 *
 * @author Julian Ragan
 */
public class CommentDisplay extends JTextArea{

    public CommentDisplay() {
        initCommentDisplay();
    }

    public CommentDisplay(String text) {
        super(text);
        initCommentDisplay();
    }

    public CommentDisplay(int rows, int columns) {
        super(rows, columns);
        initCommentDisplay();
    }

    public CommentDisplay(String text, int rows, int columns) {
        super(text, rows, columns);
        initCommentDisplay();
    }

    public CommentDisplay(Document doc) {
        super(doc);
        initCommentDisplay();
    }

    public CommentDisplay(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        initCommentDisplay();
    }
    
    private void initCommentDisplay(){
        setEditable(false);
        setBorder(new CommentBorder(8));
    }
    
    
}
