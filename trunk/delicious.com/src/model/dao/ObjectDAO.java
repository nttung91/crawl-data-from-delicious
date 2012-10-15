/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.io.Serializable;
import java.util.List;
import model.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public abstract class ObjectDAO<P,K extends Serializable>{
    protected abstract Class getPOJOClass();
    public P getObject(K key)throws HibernateException{
        P obj = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        obj = (P)session.get(getPOJOClass(),key);
        session.close();
        return obj;
    }
    public List<P> getList() throws HibernateException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<P> list= null;
        String hql = String.format("select obj from %s obj", getPOJOClass().getName());
        Query query = session.createQuery(hql);
        list = query.list();
        session.close();
        return list;
    }
    public void saveOrUpdateObject(P obj) throws HibernateException{
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
     public void deleteObject(P obj) throws HibernateException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction trans = null;
        try {
        trans = session.beginTransaction();
        session.delete(obj);
        trans.commit();
        } catch (HibernateException ex){
            trans.rollback();
            throw ex;
        } finally {
            session.close();
         }
     }   
     //tao khoa tu dong Obj: ten bang, keyColumn: ten cot muon tao tu dong, prefix
     public String generateKeyCode(String Obj,String keyColumn,String prefix){
             Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select max(obj."+keyColumn+") from "+Obj+" obj";
        Query query = session.createQuery(hql);
        Object kq = query.uniqueResult();
        if (kq!=null){
        String maxItem = kq.toString();
        maxItem = maxItem.replaceFirst(prefix,"");
        long max = Long.parseLong(maxItem);
        max++;
        String res = prefix + String.format("%05d",max);
        return res;
        }
        else {
            return prefix+String.format("%05d", 1);
        }
     }
}
