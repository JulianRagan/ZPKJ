package com.jr.comment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.JComponent;

import com.jr.data.CommentContainer;
import com.jr.data.PastelPalette;
import com.jr.extra.CommentBorder;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

/**
 * Handles building of comment display panel for individual comment
 *
 * @author Julian Ragan
 *
 */
public class CommentDisplay extends JPanel {

    private static final long serialVersionUID = 1L;
    private List<JComponent> resizableComponents;
    private List<JComponent> controlComponents;

    /**
     * Constructor for dummy demo text shown in gui builders
     *
     * @param text - demo text to display
     */
    public CommentDisplay(String text) {
        JTextArea jt = new JTextArea(text);
        jt.setEditable(false);
        jt.setBorder(new CommentBorder(8));
        //Workaround to windows look and feel font bug
        JLabel fontSrc = new JLabel("empty");
        Font f = fontSrc.getFont();
        jt.setFont(f);
        add(jt);
    }

    /**
     * Constructor for comment container, builds actual panel with controls and
     * comment content
     *
     * @param cc Comment container from which to build comment display
     */
    public CommentDisplay(CommentContainer cc) {
        resizableComponents = new ArrayList<JComponent>();
        controlComponents = new ArrayList<JComponent>();
        setLayout(new BorderLayout());
//Author panel
        JPanel author = new JPanel();
        author.setLayout(new BoxLayout(author, BoxLayout.LINE_AXIS));
        SimpleDateFormat sf = new SimpleDateFormat("dd.MM.yyyy");
        author.add(new JLabel(cc.getAuthor() + " " + sf.format(cc.getTimestamp()) + " skomentował:"));
        author.add(Box.createHorizontalGlue());
        if (cc.isEditPermission()) {
            JButton btnEdit = new JButton("Edycja");
            btnEdit.setName("edit");
            controlComponents.add(btnEdit);
            author.add(btnEdit);
        }
        if (cc.isDeletePermission()) {
            JButton btnDel = new JButton("Edycja");
            btnDel.setName("delete");
            controlComponents.add(btnDel);
            author.add(btnDel);
        }
//Content panel
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout(FlowLayout.LEFT));
        JTextPane comDis = new JTextPane(new HTMLDocument());
        comDis.setEditorKit(new HTMLEditorKit());
        if (cc.getHtmlContent() != null) {
            comDis.setText(cc.getHtmlContent());
        } else {
            comDis.setText(cc.getPlainTextContent());
        }
        comDis.setEditable(false);
        content.add(comDis);
//TagsPanel
        JPanel tags = new JPanel();
        tags.setLayout(new FlowLayout(FlowLayout.LEFT));
        List<String> tagsList = cc.getTagContents();
        for (int i = 0; i < tagsList.size(); i++) {
            JLabel newTag = new JLabel(tagsList.get(i));
            Color c;
            try {
                c = PastelPalette.getColor(tagsList.size() % 10);
            } catch (NoSuchElementException e) {
                c = Color.gray;
            }
            LineBorder lb = new LineBorder(c, 2, true);
            newTag.setBackground(c);
            newTag.setBorder(lb);
            tags.add(newTag);
        }
//Finalization
        add(author, BorderLayout.PAGE_START);
        add(content, BorderLayout.CENTER);
        add(tags, BorderLayout.PAGE_END);
        resizableComponents.add(comDis);
        resizableComponents.add(this);
        setBorder(new CommentBorder(8));
    }

    /**
     * @return list of components that need to be fitted into scrollpane
     * viewport
     */
    public List<JComponent> getResizableComponents() {
        return resizableComponents;
    }

    /**
     * @return list of control components (edit and delete buttons), if are
     * available for this comment
     */
    public List<JComponent> getControlComponents() {
        return controlComponents;
    }

}
