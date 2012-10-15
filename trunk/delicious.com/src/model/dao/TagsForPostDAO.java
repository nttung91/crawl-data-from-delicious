/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import model.pojo.TagsForPost;

/**
 *
 * @author THANHTUNG
 */
public class TagsForPostDAO extends ObjectDAO<TagsForPost, Integer> {

    @Override
    protected Class getPOJOClass() {
        return TagsForPost.class;
    }
    
    
    
    
}
