/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.comment;

import java.beans.SimpleBeanInfo;

/**
 *
 * @author Julian Ragan
 */
public class CommentsBeanInfo extends SimpleBeanInfo{
    
    public java.awt.Image getIcon(int iconKind){
        return loadImage("/res/icon16.png");
    }
}
