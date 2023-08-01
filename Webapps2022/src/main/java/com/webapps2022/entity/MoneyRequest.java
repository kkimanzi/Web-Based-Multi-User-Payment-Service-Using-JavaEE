/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the edigiverr.
 */
package com.webapps2022.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(name = "getRequestsSentFromMe", query = "SELECT t FROM MoneyRequest t WHERE t.requestor = :username"),
    @NamedQuery(name = "getPendingRequestsSentToMe", query = "SELECT t FROM MoneyRequest t WHERE t.giver = :username AND t.status = :status"),
    @NamedQuery(name = "updateRequestStatus", query = "UPDATE MoneyRequest t SET t.status = :status WHERE t.id = :requestId")
        
})

@Table(name="moneyrequest")
public class MoneyRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="requestor", nullable=false, length=64)
    private String requestor;
    @Column(name="giver", nullable=false, length=64)
    private String giver;
    private BigDecimal amount;
    @Column(name="status", nullable=false, length=64)
    private String status;

    public MoneyRequest(){}
    public MoneyRequest(String requestor, String giver, BigDecimal amount){
        this.requestor = requestor;
        this.giver = giver;
        this.amount = amount;
        this.status = "pending";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
    
    
}