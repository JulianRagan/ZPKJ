package com.jr.comment;

import com.jr.data.CommentContainer;
import com.jr.data.UserErrorContainer;
import com.jr.extra.UserErrorListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Handles control of all components and interaction with comment provider
 * @author Julian Ragan
 */
public class Controller {

    private Comments view;
    private Model model;
    private CommentProvider cp;
    private Map<String, JComponent> registeredComponents;
    private List<JComponent> registeredResizableCommentComponents;
    private List<Action> registeredActions;
    private int authorId;
    private String authorFullName;
    private boolean addPermission;
    private JFrame owner;
    private boolean errorDialog = true;
    private boolean existingEditingMode = false;
    private boolean noComments = true;
    private List<UserErrorListener> errorListeners = null;
    private Color fg;
    private JPopupMenu viewerMenu;
    private JPopupMenu editorMenu;
    private JMenuItem cite;

    /**
     * Constructs controller for Comments component
     *
     * @param view Comments view
     * @param model Comments model
     */
    protected Controller(Comments view, Model model) {
        //This contructor is called before components from view are registered
        //Any and all modification should keep this in mind
        this.view = view;
        this.model = model;
        registeredResizableCommentComponents = new ArrayList<JComponent>();
        registeredActions = new ArrayList<Action>();
        view.getCdpScroll().getViewport().addChangeListener(new CommentViewPortChangeListener());
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                owner = (JFrame) SwingUtilities.getWindowAncestor(view);
            }
        });
        viewerMenu = new JPopupMenu();
        editorMenu = new JPopupMenu();
        HTMLEditorKit.CopyAction copy = new HTMLEditorKit.CopyAction();
        HTMLEditorKit.CutAction cut = new HTMLEditorKit.CutAction();
        HTMLEditorKit.PasteAction paste = new HTMLEditorKit.PasteAction();
        copy.putValue(Action.NAME, "Kopiuj");
        cut.putValue(Action.NAME, "Wytnij");
        paste.putValue(Action.NAME, "Wklej");
        registeredActions.add(copy);
        registeredActions.add(cut);
        registeredActions.add(paste);
        viewerMenu.add(new JMenuItem(copy));
        viewerMenu.add(new JSeparator());
        cite = new JMenuItem("Cytuj");
        cite.addActionListener(new CiteActionListener());
        viewerMenu.add(cite);
        editorMenu.add(new JMenuItem(copy));
        editorMenu.add(new JMenuItem(cut));
        editorMenu.add(new JMenuItem(paste));
    }

    /**
     * Registers basic control components from view with this controller
     *
     * @param registeredComponents
     */
    protected void registerComponents(Map<String, JComponent> registeredComponents) {
        this.registeredComponents = registeredComponents;
        initComponentControl();
    }

    /**
     * Registers components that are to be resized along with Comment Display
     * panel scroll pane's viewport
     *
     * @param comp
     */
    private void registerResizableCommentComponent(JComponent comp) {
        registeredResizableCommentComponents.add(comp);
    }

    /**
     * Registers Comment Provider and loads available comments
     *
     * @param cp comment provider for current application context
     */
    public void registerCommentProvider(CommentProvider cp) {
        this.cp = cp;
        addPermission = cp.addPermission();
        loadComments();
        if (!addPermission) {
            JTextPane editor = (JTextPane) registeredComponents.get("editor");
            editor.setEnabled(false);
            JButton btn = (JButton) registeredComponents.get("btnAddComment");
            btn.setEnabled(false);
        }
    }

    /**
     * Registers comment/edit author information
     *
     * @param id Author's id in the system or in the context of the provider
     * @param fullName Author's full name, for comment display purposes
     */
    public void registerAuthor(int id, String fullName) {
        authorId = id;
        authorFullName = fullName;
    }

    /**
     * loads all available comments from comment provider and displays them
     */
    private void loadComments() {
        int commentCount = 0;

        if (cp.next()) {
            do {
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
                cc.setEditPermission(cp.editPermission());
                cc.setDeletePermission(cp.deletePermission());
                if (cp.nextTag()) {
                    do {
                        cc.addTag(cp.getTagId(), cp.getTagValue());
                    } while (cp.nextTag());
                }
                model.addComment(cc);
                commentCount++;
            } while (cp.next());
            clearCommentDisplayPane();
            for (int i = 0; i < commentCount; i++) {
                CommentDisplay cd = new CommentDisplay(model.getComment(i));
                displayComment(cd);
                registerCommentDisplayComponents(i, cd);
            }
            noComments = false;
        } else {
            clearCommentDisplayPane();
            JLabel noCom = new JLabel("<HTML><H1>Brak komentarzy</H1></HTML>");
            view.getCommentDisplayPane().add(noCom);
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JScrollBar bar = view.getCdpScroll().getVerticalScrollBar();
                if (bar.isVisible()) {
                    bar.setValue(bar.getMinimum());
                }
            }
        });

    }

    /**
     * Registers necessary components from comment display panes with this
     * controller
     *
     * @param index index of the comment container in the model
     * @param cd comment display panel
     */
    private void registerCommentDisplayComponents(int index, CommentDisplay cd) {
        for (JComponent comp : cd.getResizableComponents()) {
            registerResizableCommentComponent(comp);
            if (comp instanceof JTextPane) {
                comp.addMouseListener(new PopupListener());
                model.associateTextPane(index, (JTextPane) comp);
            }
        }
        for (JComponent comp : cd.getControlComponents()) {
            JButton btn;
            switch (comp.getName()) {
                case "edit":
                    btn = (JButton) comp;
                    btn.setIcon(createImageIcon("/res/PencilIcon.png", "edycja"));
                    btn.setText("");
                    btn.addActionListener(new EditCommentActionListener(model.getComment(index).getCommentId()));
                    btn.setMargin(new Insets(2, 2, 2, 2));
                    break;
                case "delete":
                    btn = (JButton) comp;
                    btn.setIcon(createImageIcon("/res/DelIcon.png", "usuwanie"));
                    btn.setText("");
                    btn.addActionListener(new DeleteCommentActionListener(model.getComment(index).getCommentId(), cd));
                    btn.setMargin(new Insets(2, 2, 2, 2));
                    break;
            }
        }
    }

    /**
     * Registers UserErrorListener, use to suppress built in error dialog
     *
     * @param uel User Error Listener
     */
    public void addUserErrorListener(UserErrorListener uel) {
        if (errorListeners == null) {
            errorListeners = new ArrayList<UserErrorListener>();
            errorDialog = false;
        }
        errorListeners.add(uel);
    }

    /**
     * Deregisters UserErrorListener, if all listeners are removed, errors will
     * be displayed by built in error dialof
     *
     * @param uel User Error Listener
     */
    public void removeUserErrorListener(UserErrorListener uel) {
        if (errorListeners != null) {
            if (errorListeners.contains(uel)) {
                errorListeners.remove(uel);
            }
        }
        if (errorListeners.size() == 0) {
            errorListeners = null;
            errorDialog = true;
        }
    }

    /**
     * Fires user error event, should be always invoked by
     * SwingUtilities.InvokeLater() method
     *
     * @param uec Container for user error message
     */
    private void fireUserErrorEvent(UserErrorContainer uec) {
        if (errorListeners != null) {
            for (UserErrorListener uel : errorListeners) {
                uel.errorOccured(uec);
            }
        }
    }

    /**
     * Handles new comment add proces and adds it to the comment display pane
     *
     * @param cc
     */
    private void addComment(CommentContainer cc) {
        cc.setCommentId(cp.addComment(cc));
        model.addComment(cc);
        CommentDisplay cd = new CommentDisplay(cc);
        displayComment(cd);
        registerCommentDisplayComponents(model.getCommentCount() - 1, cd);
        cleanEditor();
    }

    /**
     * Updates comment in view, model and system beyond through comment provider
     *
     * @param cc comment to be updated
     */
    private void updateComment(CommentContainer cc) {
        cp.updateComment(cc);
        model.getTextPaneForCommentInEdition().setText(cc.getHtmlContent());
        view.getCommentDisplayPane().validate();
//		view.getCommentDisplayPane().repaint();
        view.getCdpScroll().validate();
//		view.getCdpScroll().repaint();
        cleanEditor();
    }

    /**
     * Cleans up editor after use
     */
    private void cleanEditor() {
        JTextPane editor = (JTextPane) registeredComponents.get("editor");
        editor.setDocument(new HTMLDocument());
        if (existingEditingMode) {
            existingEditingMode = false;
        }
        model.setNewCommentForEdition();
    }

    /**
     * Initializes initial controls
     */
    private void initComponentControl() {
        JTextPane editor = (JTextPane) registeredComponents.get("editor");
        editor.setDocument(new HTMLDocument());
        editor.setEditorKit(new HTMLEditorKit());
        editor.addCaretListener(new TextStyleCaretListener());
        editor.addMouseListener(new PopupListener());
        JButton btn;
        JToggleButton tbtn;
        for (Map.Entry<String, JComponent> entry : registeredComponents.entrySet()) {

            switch (entry.getKey()) {
                case "tbtnBold":
                    tbtn = (JToggleButton) entry.getValue();
                    HTMLEditorKit.BoldAction ba = new HTMLEditorKit.BoldAction();
                    ba.putValue(Action.LARGE_ICON_KEY, createImageIcon("/res/Bold16.png", "Pogrubienie"));
                    tbtn.setAction(ba);
                    tbtn.setFocusable(false);
                    tbtn.setHideActionText(true);
                    break;
                case "tbtnItalic":
                    tbtn = (JToggleButton) entry.getValue();
                    HTMLEditorKit.ItalicAction ui = new HTMLEditorKit.ItalicAction();
                    ui.putValue(Action.LARGE_ICON_KEY, createImageIcon("/res/Italic16.png", "Pochylenie"));
                    tbtn.setAction(ui);
                    tbtn.setFocusable(false);
                    tbtn.setHideActionText(true);
                    break;
                case "tbtnUScore":
                    tbtn = (JToggleButton) entry.getValue();
                    HTMLEditorKit.UnderlineAction ua = new HTMLEditorKit.UnderlineAction();
                    ua.putValue(Action.LARGE_ICON_KEY, createImageIcon("/res/Uline16.png", "Podkreślenie"));
                    tbtn.setAction(ua);
                    tbtn.setFocusable(false);
                    tbtn.setHideActionText(true);
                    break;
                case "btnColorPicker":
                    btn = (JButton) entry.getValue();
                    btn.addActionListener(new ForegroundColorPickerActionListener());
                    btn.setFocusable(false);
                    btn.setText("");
                    btn.setIcon(createImageIcon("/res/FontColor16.png", "Kolor czcionki"));
                    break;
                case "btnColor":
                    btn = (JButton) entry.getValue();
                    fg = Color.black;
                    HTMLEditorKit.ForegroundAction fga = new HTMLEditorKit.ForegroundAction("", fg);
                    fga.putValue(Action.LARGE_ICON_KEY, new RectangularColorIcon(16, 16, fg));
                    btn.setAction(fga);
                    btn.setFocusable(false);
                    btn.setHideActionText(false);
                    break;
                case "btnAddComment":
                    btn = (JButton) entry.getValue();
                    btn.addActionListener(new AddCommentListener());
                    break;
                case "btnCancel":
                    btn = (JButton) entry.getValue();
                    btn.addActionListener(new CancelActionListener());

            }
        }
    }

    /**
     * removes all components from comment display pane
     */
    private void clearCommentDisplayPane() {
        view.getCommentDisplayPane().removeAll();
        view.getCommentDisplayPane().validate();
        view.getCdpScroll().validate();
    }

    /**
     * Adds comment display panel to comment display pane
     *
     * @param cd comment display panel to be displayed
     */
    private void displayComment(CommentDisplay cd) {
        view.getCommentDisplayPane().add(cd);
        view.getCommentDisplayPane().validate();
        view.getCdpScroll().validate();
    }

    /**
     * creates swing icon from resource
     *
     * @param path path to resource (image file)
     * @param description description of the created icon
     * @return ImageIcon based on the image or null, if path is incorrect
     */
    private ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Brak pliku: " + path);
            return null;
        }
    }

    /**
     * Shows error dialog in case user did not register error listeners
     *
     * @param message error message to be displayed
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(owner, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Handles error message
     *
     * @param message - error message to be delivered to the user
     */
    private void handleError(String message) {
        if (errorDialog) {
            showErrorDialog(message);
        } else {
            final UserErrorContainer uec = new UserErrorContainer(message);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    fireUserErrorEvent(uec);
                }
            });
        }
    }

    /**
     * This class is designed to control context menu for JTextPanes
     *
     * @author Jualin Ragan
     *
     */
    private class PopupListener extends MouseAdapter {

        Action cut;
        Action copy;
        Action paste;

        public PopupListener() {
            for (Action a : registeredActions) {
                switch ((String) a.getValue(Action.NAME)) {
                    case "Kopiuj":
                        copy = a;
                        break;
                    case "Wklej":
                        paste = a;
                        break;
                    case "Wytnij":
                        cut = a;
                        break;
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                if (e.getComponent() instanceof JTextPane) {
                    JTextPane pane = (JTextPane) e.getComponent();
                    boolean selection = false;
                    if (pane.getSelectionStart() != pane.getSelectionEnd()) {
                        selection = true;
                        copy.setEnabled(true);
                    } else {
                        copy.setEnabled(false);
                    }

                    if (pane.isEditable()) {
                        if (selection) {
                            cut.setEnabled(true);
                        } else {
                            cut.setEnabled(false);
                        }
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        DataFlavor[] dfs = clipboard.getAvailableDataFlavors();
                        for (DataFlavor df : dfs) {
                            if (df.isFlavorTextType()) {
                                paste.setEnabled(true);
                                break;
                            } else {
                                paste.setEnabled(false);
                            }
                        }
                        editorMenu.show(pane, e.getX(), e.getY());
                    } else {
                        if (selection) {
                            cite.setEnabled(true);
                        } else {
                            cite.setEnabled(false);
                        }
                        viewerMenu.show(pane, e.getX(), e.getY());
                        registeredComponents.put("currentViewer", pane);
                    }
                }
            }
        }
    }

    /**
     * Handles add/update action
     *
     * @author Jualin Ragan
     *
     */
    private class AddCommentListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextPane editor = (JTextPane) registeredComponents.get("editor");
            if (editor.getDocument().getLength() == 0) {
                handleError("Wprowadź treść komentarza!");
            } else {

                try {
                    CommentContainer cc = model.getCommentInEdition();
                    Date now = new Date();
                    cc.setHtmlContent(editor.getText());
                    cc.setPlainTextContent(editor.getDocument().getText(0, editor.getDocument().getLength()));
                    if (!existingEditingMode) {
                        cc.setAuthor(authorFullName);
                        cc.setAuthorId(authorId);
                        cc.setTimestamp(now);
                        cc.setDeletePermission(true);
                        cc.setEditPermission(true);
                    }
                    cc.setLastEditAuthor(authorFullName);
                    cc.setLastEditAuthorId(authorId);
                    cc.setLastEditTimestamp(now);

                    if (!existingEditingMode) {
                        if (noComments) {
                            clearCommentDisplayPane();
                            noComments = false;
                        }
                        addComment(cc);
                    } else {
                        updateComment(cc);
                    }

                } catch (BadLocationException ex) {
                    handleError("Nie udało się zapisać komentarza");
                }
            }

        }

    }

    /**
     * Handles cancel action
     *
     * @author Jualin Ragan
     *
     */
    private class CancelActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (existingEditingMode) {
                existingEditingMode = false;
                model.setNewCommentForEdition();
            }
            JTextPane editor = (JTextPane) registeredComponents.get("editor");
            JButton btn = (JButton) registeredComponents.get("btnAddComment");
            btn.setText("Dodaj");
            editor.setText("");
        }

    }

    /**
     * Handles cite action
     *
     * @author Jualin Ragan
     *
     */
    private class CiteActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JTextPane pane = (JTextPane) registeredComponents.get("currentViewer");
            String citation = pane.getSelectedText();
            if (citation != null) {
                JTextPane editor = (JTextPane) registeredComponents.get("editor");
                int caretpos = editor.getCaretPosition();
                MutableAttributeSet attr = editor.getInputAttributes();
                try {
                    editor.getDocument().insertString(caretpos, "\"" + citation + "\"", attr);
                } catch (BadLocationException ex) {
                    handleError("Nie udało się zacytować zaznaczonego tekstu");
                }

            }
        }

    }

    /**
     * Handles edition of existing comments
     *
     * @author Jualin Ragan
     *
     */
    private class EditCommentActionListener implements ActionListener {

        private int id;

        public EditCommentActionListener(int id) {
            this.id = id;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            model.setExistingCommentForEditionById(id);
            JTextPane editor = (JTextPane) registeredComponents.get("editor");
            if (model.getCommentInEdition().getHtmlContent() != null) {
                editor.setText(model.getCommentInEdition().getHtmlContent());
            } else {
                editor.setText(model.getCommentInEdition().getPlainTextContent());
            }
            editor.grabFocus();
            existingEditingMode = true;
            JButton btn = (JButton) registeredComponents.get("btnAddComment");
            btn.setText("Aktualizuj");
        }
    }

    /**
     * Handles deletion of comments
     *
     * @author Jualin Ragan
     *
     */
    private class DeleteCommentActionListener implements ActionListener {

        private int id;
        private CommentDisplay cd;

        public DeleteCommentActionListener(int id, CommentDisplay cd) {
            this.id = id;
            this.cd = cd;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            CommentContainer forDeletion = model.getCommentById(id);
            cp.removeComment(forDeletion);
            model.removeComment(model.getCommentIndexById(id));
            view.getCommentDisplayPane().remove(cd);
            view.getCommentDisplayPane().validate();
            view.getCdpScroll().validate();

        }

    }

    /**
     * Handles color selection from color picker and assigns selected color to a
     * button on a toolbar
     *
     * @author Jualin Ragan
     *
     */
    private class ForegroundColorPickerActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Color c = JColorChooser.showDialog(owner, "Choose a color", owner.getBackground());
            if (c != null) {
                fg = c;
                HTMLEditorKit.ForegroundAction fga = new HTMLEditorKit.ForegroundAction("", fg);
                fga.putValue(Action.LARGE_ICON_KEY, new RectangularColorIcon(16, 16, fg));
                JButton btnColor = (JButton) registeredComponents.get("btnColor");
                btnColor.setAction(fga);
                fga.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
            }
        }

    }

    /**
     * Handles movement of the carret in relation to enabled text styles and
     * sets control toolbar buttons accordingly
     *
     * @author Jualin Ragan
     *
     */
    private class TextStyleCaretListener implements CaretListener {

        HTMLEditorKit eKit;
        JToggleButton tbtnBold;
        JToggleButton tbtnItalic;
        JToggleButton tbtnUScore;
        JButton btnColor;

        public TextStyleCaretListener() {
            JTextPane editor = (JTextPane) registeredComponents.get("editor");
            eKit = (HTMLEditorKit) editor.getEditorKit();
            tbtnBold = (JToggleButton) registeredComponents.get("tbtnBold");
            tbtnItalic = (JToggleButton) registeredComponents.get("tbtnItalic");
            tbtnUScore = (JToggleButton) registeredComponents.get("tbtnUScore");
            btnColor = (JButton) registeredComponents.get("btnColor");
        }

        @Override
        public void caretUpdate(CaretEvent e) {
            if (e.getDot() == e.getMark()) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        MutableAttributeSet attr = eKit.getInputAttributes();
                        if (StyleConstants.isBold(attr)) {
                            tbtnBold.setSelected(true);
                        } else {
                            tbtnBold.setSelected(false);
                        }
                        if (StyleConstants.isUnderline(attr)) {
                            tbtnUScore.setSelected(true);
                        } else {
                            tbtnUScore.setSelected(false);
                        }
                        if (StyleConstants.isItalic(attr)) {
                            tbtnItalic.setSelected(true);
                        } else {
                            tbtnItalic.setSelected(false);
                        }
                        if (!StyleConstants.getForeground(attr).equals(fg)) {
                            fg = StyleConstants.getForeground(attr);
                            HTMLEditorKit.ForegroundAction fga = new HTMLEditorKit.ForegroundAction("", fg);
                            fga.putValue(Action.LARGE_ICON_KEY, new RectangularColorIcon(16, 16, fg));
                            btnColor.setAction(fga);
                        }
                    }
                });
            }
        }
    }

    /**
     * Tracks Comment display pane viewport size change and resizes appropriate
     * components to match width
     *
     * @author Jualin Ragan
     *
     */
    private class CommentViewPortChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            JViewport jvp = (JViewport) e.getSource();
            for (JComponent comp : registeredResizableCommentComponents) {
                if (comp instanceof JTextPane) {
                    comp.setSize(new Dimension(jvp.getWidth() - 16, 10));
                    comp.setPreferredSize(new Dimension(jvp.getWidth() - 16, comp.getPreferredSize().height));
                } else {
                    comp.setSize(new Dimension(jvp.getWidth(), 10));
                    comp.setPreferredSize(new Dimension(jvp.getWidth(), comp.getPreferredSize().height));
                }
            }
        }
    }

    /**
     * Generates swing icon for color button
     *
     * @author Jualin Ragan
     *
     */
    private class RectangularColorIcon implements Icon {

        private int w, h;
        private Color color;

        public RectangularColorIcon(int w, int h, Color color) {
            this.w = w;
            this.h = h;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRoundRect(x, y, w, h, w / 8, h / 8);
        }

        @Override
        public int getIconWidth() {
            return w;
        }

        @Override
        public int getIconHeight() {
            return h;
        }
    }
}
