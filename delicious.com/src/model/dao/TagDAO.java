/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import model.pojo.Tag;
import model.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class TagDAO extends ObjectDAO<Tag, Integer> {

    private static Tag getObjectByValue(String key) throws HibernateException {

        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select t from Tag t where t.tagName = :name";
        Query query = session.createQuery(hql);
        query.setParameter("name", key);
        Tag obj = (Tag) query.uniqueResult();
        session.close();
        return obj;
    }

    public static int nextIndex(String s) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Tag t = getObjectByValue(s);
        if (t != null) {
            return t.getTagId();
        } else {
            String hql = "select max(obj.tagId) from Tag obj";
            Query query = session.createQuery(hql);
            Object kq = query.uniqueResult();
            if (kq != null) {
                int maxItem = Integer.parseInt(kq.toString());

                return maxItem + 1;
            } else {
                return 1;
            }
        }
    }

    @Override
    protected Class getPOJOClass() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
