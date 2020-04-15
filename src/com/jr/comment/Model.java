/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jr.comment;

import com.jr.data.CommentContainer;
import com.jr.data.CommentDocument;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Julian Ragan
 */
public class Model {
    List<CommentContainer> rawComments = new LinkedList<CommentContainer>();
    List<CommentDocument> comments = new LinkedList<CommentDocument>();
    
    public Model(){
        
    }

    public void addRawComment(CommentContainer cc){
        rawComments.add(cc);
    }
    
    public void removeRawComment(int index){
        rawComments.remove(index);
    }
    
    public CommentContainer getRawComment(int index){
        return rawComments.get(index);
    }
}
