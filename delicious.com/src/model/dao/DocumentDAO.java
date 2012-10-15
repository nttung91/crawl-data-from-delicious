/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import model.pojo.Document;
import model.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class DocumentDAO {
     public static void saveOrUpdateObject(Document obj) throws HibernateException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = null;
        try {
        trans = session.beginTransaction();
        session.save(obj);
          trans.commit();
      
        } catch (HibernateException ex){
            trans.rollback();
            throw ex;
        } finally {
            session.close();
         }
    }
       public static int nextIndex(){
         Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select max(obj.documentId) from Document obj";
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
