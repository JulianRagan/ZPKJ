/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.data;

import java.util.Date;

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

    public CommentContainer(){
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
    }
    
    /**
     * 
     * @param plainTextContent - plain text content of the comment
     * @param author - author's full name
     * @param htmlContent - html content for formatting
     * @param lastEditAuthor - full name of the last person to edit the comment, may be null
     * @param timestamp - comment's posting time
     * @param lastEditTimestamp - comment's last edit date, may be null
     * @param commentId - id of the comment 
     * @param referencedId - id of cited comment
     * @param authorId - id of the author
     * @param lastEditAuthorId - id of the last edit author
     * @param refStart - start of citation in referenced comment
     * @param refStop  - end of citation in referenced comment
     */
    public CommentContainer(String plainTextContent, String author, String htmlContent, 
            String lastEditAuthor, Date timestamp, Date lastEditTimestamp, 
            int commentId, int referencedId, int authorId, int lastEditAuthorId, 
            int refStart, int refStop) {
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
    
    
}
