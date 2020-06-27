package com.jr.comment;

import java.beans.SimpleBeanInfo;

/**
 *
 * @author Julian Ragan
 */
public class CommentsBeanInfo extends SimpleBeanInfo {

    public java.awt.Image getIcon(int iconKind) {
        return loadImage("/res/icon16.png");
    }
}
