package com.jr.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to contain basic comment data as loaded or saved to database
 *
 * @author Julian Ragan
 */
public class CommentContainer {

    private String plainTextContent;
    private String author;
    private String htmlContent;
    private String lastEditAuthor;
    private Date timestamp;
    private Date lastEditTimestamp;
    private int commentId;
    private int referencedId;
    private int authorId;
    private int lastEditAuthorId;
    private int refStart;
    private int refStop;
    private boolean editPermission;
    private boolean deletePermission;
    private List<IntStrPair> tags;

    public CommentContainer() {
        plainTextContent = null;
        author = null;
        htmlContent = null;
        lastEditAuthor = null;
        timestamp = null;
        lastEditTimestamp = null;
        commentId = 0;
        referencedId = 0;
        authorId = 0;
        lastEditAuthorId = 0;
        refStart = -1;
        refStop = -1;
        editPermission = false;
        deletePermission = false;
        tags = new ArrayList<IntStrPair>();
    }

    /**
     *
     * @param plainTextContent plain text content of the comment
     * @param author author's full name
     * @param htmlContent html content for formatting
     * @param lastEditAuthor full name of the last person to edit the comment,
     * may be null
     * @param timestamp comment's posting time
     * @param lastEditTimestamp comment's last edit date, may be null
     * @param commentId id of the comment
     * @param referencedId id of cited comment
     * @param authorId id of the author
     * @param lastEditAuthorId id of the last edit author
     * @param refStart start of citation in referenced comment
     * @param refStop end of citation in referenced comment
     * @param editPermission decides whether current user has permission to edit this comment
     * @param deletePermission decides wheter current user has deletion permission fo this comment
     */
    public CommentContainer(String plainTextContent, String author, String htmlContent,
            String lastEditAuthor, Date timestamp, Date lastEditTimestamp,
            int commentId, int referencedId, int authorId, int lastEditAuthorId,
            int refStart, int refStop, boolean editPermission, boolean deletePermission) {
        this.plainTextContent = plainTextContent;
        this.author = author;
        this.htmlContent = htmlContent;
        this.lastEditAuthor = lastEditAuthor;
        this.timestamp = timestamp;
        this.lastEditTimestamp = lastEditTimestamp;
        this.commentId = commentId;
        this.referencedId = referencedId;
        this.authorId = authorId;
        this.lastEditAuthorId = lastEditAuthorId;
        this.refStart = refStart;
        this.refStop = refStop;
        this.editPermission = editPermission;
        this.deletePermission = deletePermission;
        tags = new ArrayList<IntStrPair>();
    }

    public String getPlainTextContent() {
        return plainTextContent;
    }

    public void setPlainTextContent(String plainTextContent) {
        this.plainTextContent = plainTextContent;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getLastEditAuthor() {
        return lastEditAuthor;
    }

    public void setLastEditAuthor(String lastEditAuthor) {
        this.lastEditAuthor = lastEditAuthor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getLastEditTimestamp() {
        return lastEditTimestamp;
    }

    public void setLastEditTimestamp(Date lastEditTimestamp) {
        this.lastEditTimestamp = lastEditTimestamp;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getReferencedId() {
        return referencedId;
    }

    public void setReferencedId(int referencedId) {
        this.referencedId = referencedId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getLastEditAuthorId() {
        return lastEditAuthorId;
    }

    public void setLastEditAuthorId(int lastEditAuthorId) {
        this.lastEditAuthorId = lastEditAuthorId;
    }

    public int getRefStart() {
        return refStart;
    }

    public void setRefStart(int refStart) {
        this.refStart = refStart;
    }

    public int getRefStop() {
        return refStop;
    }

    public void setRefStop(int refStop) {
        this.refStop = refStop;
    }

    public boolean isEditPermission() {
        return editPermission;
    }

    public void setEditPermission(boolean editPermission) {
        this.editPermission = editPermission;
    }

    public boolean isDeletePermission() {
        return deletePermission;
    }

    public void setDeletePermission(boolean deletePermission) {
        this.deletePermission = deletePermission;
    }

    public void updateContent(CommentContainer cc) {
        plainTextContent = cc.getPlainTextContent();
        htmlContent = cc.getHtmlContent();
        lastEditAuthor = cc.getLastEditAuthor();
        lastEditAuthorId = cc.getLastEditAuthorId();
        lastEditTimestamp = cc.getLastEditTimestamp();
        referencedId = cc.getReferencedId();
        refStart = cc.getRefStart();
        refStop = cc.getRefStop();

    }

    public void addTag(int id, String value) {
        for (IntStrPair tag : tags) {
            if (tag.getKey() == id) {
                if (tag.getValue().contentEquals(value)) {
                    return;//avoid duplicates
                }
            }
        }
        IntStrPair tag = new IntStrPair(id, value);
        tags.add(tag);
    }

    public void addTag(IntStrPair tag) {
        for (IntStrPair tag1 : tags) {
            if (tag1.equals(tag)) {
                return;//avoid duplicates
            }
        }
        tags.add(tag);
    }

    public void removeTag(int id, String value) {
        List<IntStrPair> tagsToRemove = new ArrayList<IntStrPair>();
        for (IntStrPair tag : tags) {
            if (tag.getKey() == id) {
                if (tag.getValue().contentEquals(value)) {
                    tagsToRemove.add(tag);
                }
            }
        }
        for (IntStrPair tag : tagsToRemove) {
            tags.remove(tag);
        }
    }

    public void removeTag(IntStrPair tag) {
        removeTag(tag.getKey(), tag.getValue());
    }

    public List<String> getTagContents() {
        List<String> retval = new ArrayList<String>();
        for (IntStrPair tag : tags) {
            retval.add(tag.getValue());
        }
        return retval;
    }

    public List<IntStrPair> getTags() {
        return tags;
    }

}
