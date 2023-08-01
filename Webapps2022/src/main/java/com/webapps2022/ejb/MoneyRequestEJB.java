package com.webapps2022.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.webapps2022.entity.User;
import com.webapps2022.entity.MoneyRequest;
import com.webapps2022.jsf.RegisterView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;


import javax.xml.bind.DatatypeConverter;
import com.webapps2022.entity.MoneyRequest;
import com.webapps2022.entity.MoneyTransfer;

@Stateless
public class MoneyRequestEJB {
    
    @PersistenceContext(unitName="WebappsDBPU")
    private EntityManager em;
    private static Logger log = Logger.getLogger(RegisterView.class.getName());
    
    // Function to save money request to the DB
    public MoneyRequest requestMoney(MoneyRequest request){
        em.persist(request);
        return request;
    }
    
    // Function to get the money requests sent to user with username id
    public List<MoneyRequest> getRequestsSentFromMe(String id){
       TypedQuery<MoneyRequest> requestsSentQuery = em.createNamedQuery("getRequestsSentFromMe", MoneyRequest.class);
        requestsSentQuery.setParameter("username", id);
        List<MoneyRequest> requestsSentList = null;
        try {
            requestsSentList = requestsSentQuery.getResultList();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignor-e exception and return NULL for user instead
        } 
        
        return requestsSentList;
    }
    
    // Function to get the pending money requests to user with username id
    public List<MoneyRequest> getPendingRequestsSentToMe(String id) {
        
        TypedQuery<MoneyRequest> pendingRequetstsQuery = em.createNamedQuery("getPendingRequestsSentToMe", MoneyRequest.class);
        pendingRequetstsQuery.setParameter("username", id);
        pendingRequetstsQuery.setParameter("status", "pending");
        List<MoneyRequest> pendingRequestsList = null;
        try {
            pendingRequestsList = pendingRequetstsQuery.getResultList();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignor-e exception and return NULL for user instead
        }
        
        return pendingRequestsList;
    }
    
    // Function to mark money request as being accepted
    public void markRequestAsConfirmed(Long id){
        TypedQuery<MoneyRequest> changeStatus = em.createNamedQuery("updateRequestStatus", MoneyRequest.class);
        changeStatus.setParameter("requestId", id);
        changeStatus.setParameter("status", "confirmed");
        changeStatus.executeUpdate();
        
    }
    
    // Function to mark money request as being rejected
    public void markRequestAsRejected(Long id){
        TypedQuery<MoneyRequest> changeStatus = em.createNamedQuery("updateRequestStatus", MoneyRequest.class);
        changeStatus.setParameter("requestId", id);
        changeStatus.setParameter("status", "rejected");
        changeStatus.executeUpdate();
        
    }
    
}
