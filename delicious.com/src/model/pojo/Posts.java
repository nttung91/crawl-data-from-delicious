package model.pojo;
// Generated Oct 14, 2012 3:19:08 PM by Hibernate Tools 3.2.1.GA


import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Posts generated by hbm2java
 */
public class Posts  implements java.io.Serializable {


     private int postId;
     private Document document;
     private String author;
     private String description;
     private String note;
     private Timestamp datePost;
     private Set tagsForPosts = new HashSet(0);

    public Posts() {
    }

	
    public Posts(int postId) {
        this.postId = postId;
    }
    public Posts(int postId, Document document, String author, String description, String note, Timestamp datePost, Set tagsForPosts) {
       this.postId = postId;
       this.document = document;
       this.author = author;
       this.description = description;
       this.note = note;
       this.datePost = datePost;
       this.tagsForPosts = tagsForPosts;
    }
   
    public int getPostId() {
        return this.postId;
    }
    
    public void setPostId(int postId) {
        this.postId = postId;
    }
    public Document getDocument() {
        return this.document;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
    public String getAuthor() {
        return this.author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    public Timestamp getDatePost() {
        return this.datePost;
    }
    
    public void setDatePost(Timestamp datePost) {
        this.datePost = datePost;
    }
    public Set getTagsForPosts() {
        return this.tagsForPosts;
    }
    
    public void setTagsForPosts(Set tagsForPosts) {
        this.tagsForPosts = tagsForPosts;
    }




}


