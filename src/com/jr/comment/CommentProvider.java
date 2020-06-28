package com.jr.comment;

import com.jr.data.CommentContainer;
import java.util.Date;

/**
 * Interface for comment provider.
 *
 * @author Julian Ragan
 */
public interface CommentProvider {

    /**
     * Should be used to expose first and following comments contents to the
     * getters
     *
     * @return true, if next comment was exposed, false if there are no new
     * commets
     */
    public boolean next();

    /**
     * @return true, if current user has permission to edit current comment in
     * the context of the application
     */
    public boolean editPermission();

    /**
     * @return true, if current user has permission to delete current comment in
     * the context of the application
     */
    public boolean deletePermission();

    /**
     * this method should return permission status independedly of any comments,
     * since it will be called only once, at initialization stage.
     *
     * @return true, if user has permission to add comments to current
     * application context.
     */
    public boolean addPermission();

    /**
     * Should take container and properly save comment in the context of
     * application and storage medium
     *
     * @param cc contains data of the new comment, new comment id is -1 in
     * container and shuold not be used for saving
     * @return comment id number, which was assigned by the storage management
     * system
     */
    public int addComment(CommentContainer cc);

    /**
     * Should take container and properly upadet earlier comment content in the
     * context of application and storage medium
     *
     * @param cc comment container 
     */
    public void updateComment(CommentContainer cc);

    /**
     * Should take container and properly remove earlier comment content in the
     * context of application and storage medium
     *
     * @param cc comment container 
     */
    public void removeComment(CommentContainer cc);

    /**
     *
     * @return plain text content
     */
    public String getPlainTextContent();

    /**
     *
     * @return authors full name
     */
    public String getAuthor();

    /**
     *
     * @return HTML Content of the comment
     */
    public String getHtmlContent();

    /**
     *
     * @return last edit auhtors full name or null if comment has not been
     * edited
     */
    public String getLastEditAuthor();

    /**
     *
     * @return java.util.Date with date of publication of the comment
     */
    public Date getTimestamp();

    /**
     *
     * @return java.util.Date with date of last edit of the comment
     */
    public Date getLastEditTimestamp();

    /**
     *
     * @return internal id of the comment
     */
    public int getCommentId();

    /**
     *
     * @return internal number of a comment to which this one refers, or 0
     */
    public int getReferencedId();

    /**
     *
     * @return internal author id number
     */
    public int getAuthorId();

    /**
     *
     * @return internal author number id of last edit, or 0
     */
    public int getLastEditAuthorId();

    /**
     *
     * @return 0-based index of citation start, or -1 if no citation used
     */
    public int getRefStart();

    /**
     *
     * @return 0-based index of citation stop, or -1 if no citation used
     */
    public int getRefStop();

    /**
     * Should be used to expose first and following tags of current comment to
     * the getters
     *
     * @return true, if next tag was exposed, false, if there are no more new
     * tags to read
     */
    public boolean nextTag();

    /**
     *
     * @return tag value
     */
    public String getTagValue();

    /**
     * @return tag id
     */
    public int getTagId();
}
