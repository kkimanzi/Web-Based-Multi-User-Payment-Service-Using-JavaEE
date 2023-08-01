package com.webapps2022.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.webapps2022.entity.User;
import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.jsf.RegisterView;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.TypedQuery;


import javax.xml.bind.DatatypeConverter;

@Stateless
public class MoneyTransferEJB {
    
    @PersistenceContext(unitName="WebappsDBPU")
    private EntityManager em;
    private static Logger log = Logger.getLogger(RegisterView.class.getName());
    
    // Function to get money transfers to username id that have not been read
    public List<MoneyTransfer> getUnreadInboundTransfers(String id){
       TypedQuery<MoneyTransfer> inboundUnreadQuery = em.createNamedQuery("getUnreadInboundTransfers", MoneyTransfer.class);
        inboundUnreadQuery.setParameter("username", id);
        List<MoneyTransfer> inboundUnreadList = null;
        try {
            inboundUnreadList = inboundUnreadQuery.getResultList();
        } catch (Exception e) {
            // Handled by original null assignment
        } 
        
        return inboundUnreadList;
    }
    
    // Function to get all transactions of username id
    // Returns a list with two elements
    // Element 1 is a list of transactions for money sent from user
    // Element 2 is a list of transactions for money received from user
    public List<List<MoneyTransfer>> getUserTransactions(String id) {
        
        // Get list of transactions of money sent
        TypedQuery<MoneyTransfer> outboundQuery = em.createNamedQuery("getOutboundTransfers", MoneyTransfer.class);
        outboundQuery.setParameter("username", id);
        List<MoneyTransfer> outboundTransfers = null;
        try {
            outboundTransfers = outboundQuery.getResultList();
        } catch (Exception e) {
            // Handled by original null assignment
        }
        
        // Get list of transactions of money received
        TypedQuery<MoneyTransfer> inboundQuery = em.createNamedQuery("getInboundTransfers", MoneyTransfer.class);
        inboundQuery.setParameter("username", id);
        List<MoneyTransfer> inbooundTransfers = null;
        try {
            inbooundTransfers = inboundQuery.getResultList();
        } catch (Exception e) {
            // getSingleResult throws NoResultException in case there is no user in DB
            // ignor-e exception and return NULL for user instead
        }
        
        List<List<MoneyTransfer>> allTransactions = new ArrayList<>();
        allTransactions.add(outboundTransfers);
        allTransactions.add(inbooundTransfers);
        
        return allTransactions;
    }
    
    // Mark a transactions as being checked by the user (in notifications)
    public void markTransferAsChecked(Long id){
        TypedQuery<MoneyTransfer> markChecked = em.createNamedQuery("markChecked", MoneyTransfer.class);
        markChecked.setParameter("transferId", id);
        markChecked.executeUpdate();
        
    }
    
    // Get balance of account
    public BigDecimal getBalance(String id){
        BigDecimal balance = new BigDecimal(0);
        try {
            // Kludgy method
            // Inefficient implelemtation but works
            // Review use of COUNT sql query instead
            List<List<MoneyTransfer>> transactions = getUserTransactions(id);
            List<MoneyTransfer> outboundTransfers = transactions.get(0);
            List<MoneyTransfer> inboundTransfers = transactions.get(1);
            
            // Get sum of money sent
            BigDecimal moneySent = new BigDecimal(0);
            if (outboundTransfers != null){
                for (int i =0; i < outboundTransfers.size(); i++){
                    BigDecimal tmp = outboundTransfers.get(i).getAmount();
                    moneySent = moneySent.add(tmp);
                }
            }
            
            log.info (String.valueOf(moneySent));
            // Get sum of money received
            BigDecimal moneyReceived = new BigDecimal(0);
            if (inboundTransfers != null){
                for (int i =0; i < inboundTransfers.size(); i++){
                    BigDecimal tmp = inboundTransfers.get(i).getAmount();
                    moneyReceived = moneyReceived.add(tmp);
                }
            }
            log.info (String.valueOf(moneyReceived));
            
            // balance = moneyreceived - moneysent
            balance = balance.add(moneyReceived);
            balance = balance.subtract(moneySent);
            
        } catch (Exception e) {
            log.info(e.getMessage());
            // handled by initial 0 assignment
        }
        
        return balance;
    }
    
    
}
