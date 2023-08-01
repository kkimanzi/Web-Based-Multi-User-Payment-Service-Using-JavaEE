package com.webapps2022.ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.webapps2022.entity.Group;
import com.webapps2022.entity.User;
import com.webapps2022.jsf.RegisterView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

@Stateless
public class UserEJB {
    
    @PersistenceContext(unitName="WebappsDBPU")
    private EntityManager em;
    private static Logger log = Logger.getLogger(RegisterView.class.getName());
    
    // Function to create user
    // Takes user object and role for the user
    public User createUser(User user, String role) {
        try {
            user.setPassword(encodeSHA256(user.getPassword()));
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
        Group group = new Group();
        group.setUsername(user.getUsername());
        if (role == "users"){
            group.setGroupname(Group.USERS_GROUP);
            log.info("Creating user with users_group");
        } else {
            group.setGroupname(Group.ADMINS_GROUP);
        }
        em.persist(user);
        em.persist(group);
        return user;
    }
    
    // Function to get user with username id
    public User findUserById(String id) {
        TypedQuery<User> query = em.createNamedQuery("findUserById", User.class);
        query.setParameter("username", id);
        User user = null;
        try {
            user = query.getSingleResult();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignore exception and return NULL for user instead
        }
        return user;
    }
    
    public List<User> getAllUsers(){
        TypedQuery<User> query = em.createNamedQuery("getAllUsers", User.class);
        List<User> user = null;
        try {
            user = query.getResultList();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignore exception and return NULL for user instead
        }
        return user;
    }
    // Function to get hash of password
    public static String encodeSHA256(String password) 
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return DatatypeConverter.printBase64Binary(digest).toString();
    }
}