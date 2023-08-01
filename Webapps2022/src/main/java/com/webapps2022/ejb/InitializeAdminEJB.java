package com.webapps2022.ejb;

import static com.webapps2022.ejb.UserEJB.encodeSHA256;
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

import javax.xml.bind.DatatypeConverter;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Startup
@Singleton
public class InitializeAdminEJB {
  private String status;
  
  @PersistenceUnit(unitName="WebappsDBPU")
  EntityManagerFactory emf;
  
  private static Logger log = Logger.getLogger(RegisterView.class.getName());
  
  @PostConstruct
  public void InitalizeAdminEJB (){
        // Create first admin account
    User admin = new User("admin1", "admin1");
    
    try {
        admin.setPassword(encodeSHA256(admin.getPassword()));
    } catch (Exception e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        e.printStackTrace();
    }
    Group group = new Group(admin.getUsername(), Group.ADMINS_GROUP);
       
    // Save them to the DB
    EntityManager em = emf.createEntityManager();
    em.persist(group);
    em.persist(admin);
    
  }
  
  
  public static String encodeSHA256(String password) 
            throws UnsupportedEncodingException, NoSuchAlgorithmException {
      // Produce HASH of password  
      MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] digest = md.digest();
        return DatatypeConverter.printBase64Binary(digest).toString();
    }
}
