package edu.redwoods.rbachibernate;

import java.util.List;
import org.hibernate.Session;

public class RbacHibernateApp {
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> userList;
        /*
        session.beginTransaction();
        userList = session.createQuery
                ("SELECT a FROM User a", User.class).getResultList();
        // Put code to create/update (AKA persist/merge) users here.
        userList.forEach(u -> {
            System.out.println(u.toString());
            u.setPasswordHash("P@ssw0rd1");
            session.merge(u); // Updates the user with changes (i.e. password in this case).
        });
        session.flush(); // Flush changes and commit them.
        session.getTransaction().commit();
        */
        // Note: This looks like SQL, but it is not, it is selecting from the persistence entity you defined.
        userList = session.createQuery
                ("SELECT a FROM User a WHERE a.username='maverick'", User.class).getResultList();

        userList.forEach(u -> {
            System.out.println(u.toString());
            // Let's check to see if this user is authenticated.
            System.out.println(
                    (Utilities.isAuthenticated("P@ssw0rd1", u) ? "YAAAASSSS" : "NOOOOO")
            );

            u.getRoles().forEach(r -> {
                System.out.println("   " + r.toString());
            });
        });

        HibernateUtil.shutdown();
    }
}
