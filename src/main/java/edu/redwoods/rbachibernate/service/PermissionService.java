package edu.redwoods.rbachibernate.service;

import edu.redwoods.rbachibernate.Permission;
import edu.redwoods.rbachibernate.HibernateUtil;
import edu.redwoods.rbachibernate.Role;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PermissionService {

    public void insertPermission(String permissionName) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Fetch the permission by name to make sure it doesn't already exist
            Query<Permission> query = session.createQuery("FROM Permission WHERE name = :name", Permission.class);
            query.setParameter("name", permissionName);
            Permission perm = query.uniqueResult();

            if (perm != null) {
                return;  // Do nothing more, because the permission already exists.
            }

            Permission permission = new Permission(permissionName);

            session.save(permission);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

