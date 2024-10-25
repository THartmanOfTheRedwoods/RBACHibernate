package edu.redwoods.rbachibernate.service;

import edu.redwoods.rbachibernate.Role;
import edu.redwoods.rbachibernate.Permission;
import edu.redwoods.rbachibernate.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RoleService {

    public void insertRoleWithPermissions(String roleName, List<String> permissionNames) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Fetch the role based on its name
            Query<Role> rquery = session.createQuery("FROM Role WHERE name = :name", Role.class);
            rquery.setParameter("name", roleName);
            Role role = rquery.uniqueResult();

            if (role == null) {  // We need to create a new role.
                role = new Role(roleName);
            }

            // Fetch permissions and add them to the role, clearly the Permission needs to exist.
            for (String permName : permissionNames) {
                Query<Permission> query = session.createQuery("FROM Permission WHERE name = :name", Permission.class);
                query.setParameter("name", permName);
                Permission permission = query.uniqueResult();

                if (permission != null) {
                    role.getPermissions().add(permission);
                }
            }

            session.save(role);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
