/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import model.pojo.Posts;
import model.pojo.Tag;
import model.pojo.TagCollect;
import model.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class TagCollectDAO extends ObjectDAO<TagCollect, Integer> {

    @Override
    protected Class getPOJOClass() {
        return TagCollect.class;
    }

    public static int nextIndex() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        String hql = "select max(obj.id) from TagCollect obj";
        Query query = session.createQuery(hql);
        Object kq = query.uniqueResult();
        if (kq != null) {
            int maxItem = Integer.parseInt(kq.toString());
            return maxItem + 1;
        } else {
            return 1;
        }
    }
   
        
     public boolean checkDuplicate(String author, Timestamp date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "from TagCollect t where t.tagName =:name and t.dateTagged=:time";
       
        Query query = session.createQuery(hql);
        query.setParameter("name",author);
        query.setParameter("time",date,Hibernate.TIMESTAMP);
        TagCollect t = (TagCollect)query.uniqueResult();
        if (t != null ){
            return true;
        }
        return false;
    }
}
