package com.webapps2022.ejb;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.webapps2022.entity.User;
import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.jsf.RegisterView;


import javax.xml.bind.DatatypeConverter;

@Stateless
public class SendMoneyEJB {
    
    @PersistenceContext(unitName="WebappsDBPU")
    private EntityManager em;
    private static Logger log = Logger.getLogger(RegisterView.class.getName());
    
    public MoneyTransfer sendMoney(MoneyTransfer transaction){
        em.persist(transaction);
        return transaction;
    }
    
}
