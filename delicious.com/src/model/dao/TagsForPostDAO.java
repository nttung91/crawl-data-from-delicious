/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.pojo.Document;
import model.pojo.Posts;
import model.pojo.TagsForPost;
import model.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class TagsForPostDAO {
    public static void saveOrUpdateObject(TagsForPost obj) throws HibernateException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = null;
        try {
        trans = session.beginTransaction();
        session.saveOrUpdate(obj);
          trans.commit();
      
        } catch (HibernateException ex){
            trans.rollback();
            throw ex;
        } finally {
            session.close();
         }
    }
    public TagsForPost getObject(int key)throws HibernateException{
        TagsForPost obj = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        obj = (TagsForPost)session.get(TagsForPost.class, key);
        session.close();
        return obj;
    }
    public List<TagsForPost> getList() throws HibernateException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<TagsForPost> list= null;
        String hql = String.format("select obj from %s obj", TagsForPost.class);
        Query query = session.createQuery(hql);
        list = query.list();
        session.close();
        return list;
    }
    public static int nextIndex(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        
        String hql = "select max(obj.postId) from Posts obj";
        Query query = session.createQuery(hql);
        Object kq = query.uniqueResult();
        if (kq!=null){
        int maxItem = Integer.parseInt(kq.toString());
        return maxItem+1;
        }
        else {
            return 1;
        }
     }
    
}
