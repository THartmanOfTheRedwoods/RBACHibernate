package edu.redwoods.rbachibernate.service;

import edu.redwoods.rbachibernate.HibernateUtil;
import edu.redwoods.rbachibernate.User;
import edu.redwoods.rbachibernate.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserService {

    public void insertUserWithRole(String username, String password, String roleName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            // Fetch the role based on its name
            Query<User> rquery = session.createQuery("FROM User WHERE username = :name", User.class);
            rquery.setParameter("name", username);
            User user = rquery.uniqueResult();

            if (user == null) {  // We need to create a new User
                user = new User(username);
            }
            if(password != null) { user.setPasswordHash(password); } // In case we're updating the password hash

            // Fetch the role based on its name
            Query<Role> query = session.createQuery("FROM Role WHERE name = :name", Role.class);
            query.setParameter("name", roleName);
            Role role = query.uniqueResult();

            if (role != null) {
                user.getRoles().add(role);
            }

            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

