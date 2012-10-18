/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delicious.com;

import java.util.List;
import model.util.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author THANHTUNG
 */
public class DatabaseHelper {

    public static List<String> getMostPopularTag(int count) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select t.tagName from TagCollect t \n"
                + "group by  t.tagName\n"
                + "having count(t.id)>100\n"
                + "order by count(t.id) desc";
        Query query = session.createQuery(hql);
        query.setMaxResults(count);
        List<String> l = query.list();
       
        session.close();
        return l;
    }
}
