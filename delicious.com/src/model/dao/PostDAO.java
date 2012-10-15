/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import model.pojo.Document;
import model.pojo.Posts;
import model.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class PostDAO extends ObjectDAO<Posts, Integer> {

    public int checkDuplicateItem(int docID, String author, Timestamp date) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "from Posts p where p.author=:user and p.document.documentId=:docId";
       
        Query query = session.createQuery(hql);
        query.setParameter("user",author);
        query.setParameter("docId", docID);
        List<Posts> l = query.list();
        if (l != null && l.size()>0 ){
            Posts p = l.get(0);

            if (date.compareTo(p.getDatePost()) > 0) {
                return p.getPostId();
            } else {
                //cu hon
                return -2;
            }

        } else {
            return -1;
        }
    }

    public static int nextIndex() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select max(obj.postId) from Posts obj";
        Query query = session.createQuery(hql);
        Object kq = query.uniqueResult();
        if (kq != null) {
            int maxItem = Integer.parseInt(kq.toString());
            return maxItem + 1;
        } else {
            return 1;
        }

    }

    @Override
    protected Class getPOJOClass() {
        return Posts.class;
    }
}
