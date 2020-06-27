package com.jr.comment;

import com.jr.data.CommentContainer;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.swing.JTextPane;

/**
 *
 * @author Julian Ragan
 */
public class Model {

    List<CommentContainer> comments = new LinkedList<CommentContainer>();
    Map<CommentContainer, JTextPane> panes = new HashMap<CommentContainer, JTextPane>();
    int count;
    CommentContainer commentInEdition;

    public Model() {
        commentInEdition = new CommentContainer();
        count = 0;
    }

    public void addComment(CommentContainer cc) {
        comments.add(cc);
        count++;
    }

    public void removeComment(int index) {
        CommentContainer cc = comments.get(index);
        comments.remove(index);
        if (panes.containsKey(cc)) {
            panes.remove(cc);
        }
        count--;
    }

    public void associateTextPane(int index, JTextPane pane) {
        panes.put(getComment(index), pane);
    }

    public JTextPane getTextPaneForCommentInEdition() {
        return panes.get(commentInEdition);
    }

    public CommentContainer getComment(int index) {
        return comments.get(index);
    }

    public int getCommentIndexById(int id) throws NoSuchElementException {
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getCommentId() == id) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }

    public CommentContainer getCommentById(int id) throws NoSuchElementException {
        for (CommentContainer cc : comments) {
            if (cc.getCommentId() == id) {
                return cc;
            }
        }
        throw new NoSuchElementException();
    }

    public void setExistingCommentForEdition(int index) {
        commentInEdition = comments.get(index);
    }

    public void setExistingCommentForEditionById(int id) throws NoSuchElementException {
        commentInEdition = getCommentById(id);
    }

    public void setNewCommentForEdition() {
        commentInEdition = new CommentContainer();
    }

    public int getCommentCount() {
        return count;
    }

    public CommentContainer getCommentInEdition() {
        return commentInEdition;
    }
}
