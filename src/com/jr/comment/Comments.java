package com.jr.comment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.text.html.HTMLDocument;

/**
 * Main class, handles initialization and demo display for RAD tools Serves as
 * view for controller
 *
 * @author Julian Ragan
 */
public class Comments extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel CommentDisplayPane;
    private JScrollPane cdpScroll;
    private Controller controller;
    private Map<String, JComponent> registeredComponents;

    /**
     * Contructor for the Comments component. Constructs controller and model
     * and handles initial registration of control components
     */
    public Comments() {
        registeredComponents = new HashMap<String, JComponent>();
        CommentDisplayPane = initCommentDisplayPane();
        JPanel CommentEditorPane = initCommentEditorPane();
        cdpScroll = new JScrollPane(CommentDisplayPane) {
            private static final long serialVersionUID = 1L;

            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int desired = (int) (getParent().getSize().height * 0.60);
                return new Dimension(d.width, desired);
            }
        };
        cdpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        cdpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(cdpScroll);
        add(CommentEditorPane);
        Model model = new Model();
        controller = new Controller(this, model);
        controller.registerComponents(registeredComponents);
    }

    /**
     * Builds container for comments with demo data
     *
     * @return JPanel
     */
    private JPanel initCommentDisplayPane() {
        JPanel retval = new JPanel();
        retval.setLayout(new BoxLayout(retval, BoxLayout.Y_AXIS));
        retval.add(new CommentDisplay("Autor Demo 01.01.2020 o 15:50 napisał: \n Demo komentarza"));
        retval.add(new CommentDisplay("Autor Demo 02.01.2020 o 10:00 napisał: \n Demo komentarza"));
        return retval;
    }

    /**
     * Builds comment editor panel with controls
     *
     * @return JPanel
     */
    private JPanel initCommentEditorPane() {
        JPanel retval = new JPanel();
        retval.setLayout(new BorderLayout());
//Editor toolbar
        JToolBar controllBar = new JToolBar();
        controllBar.setFloatable(false);
        controllBar.setRollover(true);
        JToggleButton tbtnBold = new JToggleButton("B");
        JToggleButton tbtnItalic = new JToggleButton("I");
        JToggleButton tbtnUScore = new JToggleButton("U");
        controllBar.add(tbtnBold);
        controllBar.add(tbtnItalic);
        controllBar.add(tbtnUScore);
        JButton btnColorPicker = new JButton("Wybierz kolor");
        JButton btnColor = new JButton("Kolor");
        controllBar.add(new JToolBar.Separator());
        controllBar.add(btnColor);
        controllBar.add(btnColorPicker);
        retval.add(controllBar, BorderLayout.PAGE_START);
//Editor
        JTextPane editor = new JTextPane(new HTMLDocument());
        JScrollPane eScroll = new JScrollPane(editor) {

            private static final long serialVersionUID = 1L;

            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int desired = (int) (getParent().getSize().height * 0.30);
                return new Dimension(d.width, desired);
            }
        };
        eScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        eScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        retval.add(eScroll, BorderLayout.CENTER);
//Editor controls
        JPanel control = new JPanel();
        control.setLayout(new BoxLayout(control, BoxLayout.LINE_AXIS));
        JButton btnAddComment = new JButton("Dodaj");
        JButton btnCancel = new JButton("Anuluj");
        control.add(btnAddComment);
        control.add(Box.createHorizontalGlue());
        control.add(btnCancel);
        retval.add(control, BorderLayout.PAGE_END);
        //Register components
        registeredComponents.put("btnColorPicker", btnColorPicker);
        registeredComponents.put("btnColor", btnColor);
        registeredComponents.put("tbtnBold", tbtnBold);
        registeredComponents.put("tbtnItalic", tbtnItalic);
        registeredComponents.put("tbtnUScore", tbtnUScore);
        registeredComponents.put("btnAddComment", btnAddComment);
        registeredComponents.put("btnCancel", btnCancel);
        registeredComponents.put("editor", editor);
        return retval;
    }

    /**
     * Allows access to comment display panel
     *
     * @return JPanel
     */
    protected JPanel getCommentDisplayPane() {
        return CommentDisplayPane;
    }

    /**
     * Allows access to Primary controller of Comments component
     *
     * @return controller
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Allows access to scroll pane wraping comment display panel
     *
     * @return JScrollPane
     */
    protected JScrollPane getCdpScroll() {
        return cdpScroll;
    }
}
