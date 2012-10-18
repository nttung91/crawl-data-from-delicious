/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.util.List;
import model.pojo.Document;
import model.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.classic.Session;

/**
 *
 * @author THANHTUNG
 */
public class DocumentDAO extends ObjectDAO<Document, Integer> {
     
       public static int nextIndex(){
         Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select max(obj.documentId) from Document obj";
        Query query = session.createQuery(hql);
        Object kq = query.uniqueResult();
        
        if (kq!=null){
        int maxItem = Integer.parseInt(kq.toString());
        session.close();
        return maxItem+1;
        }
        else {
            return 1;
        }
        
     }
     public int processDuplicate(String url){
         //if has a doc that exist in database it'll delete that doc and reference object 
         //check dup
         Session session = HibernateUtil.getSessionFactory().openSession();
         String hql = "from Document d where d.url = :url";
         Query query = session.createQuery(hql);
         query.setParameter("url",url );
         Document doc = (Document)query.uniqueResult();
         //if dup
         session.close();
         
         if (doc != null){
//              DocumentDAO docdao =new DocumentDAO();
//              docdao.deleteObject(doc);
              return doc.getDocumentId();//inform dup
         }
         else {
             return -1; //inform not dup
         }
         
     }

    @Override
    protected Class getPOJOClass() {
        return Document.class;
    }
    
}
