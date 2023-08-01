/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the edirecipientr.
 */
package com.webapps2022.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(name = "getOutboundTransfers", query = "SELECT t FROM MoneyTransfer t WHERE t.sender = :username"),
    @NamedQuery(name = "getInboundTransfers", query = "SELECT t FROM MoneyTransfer t WHERE t.recipient = :username"),
    @NamedQuery(name = "getUnreadInboundTransfers", query = "SELECT t FROM MoneyTransfer t WHERE t.recipient = :username AND t.checked=false"),
    @NamedQuery(name = "markChecked", query = "UPDATE MoneyTransfer t SET t.checked = true WHERE t.id = :transferId"),
    @NamedQuery(name = "getSumReceived", query = "SELECT SUM(t.amount) FROM MoneyTransfer t WHERE t.recipient = :username"),
    @NamedQuery(name = "getSumSent", query = "SELECT SUM(t.amount) FROM MoneyTransfer t WHERE t.sender = :username")
        
})

@Table(name="moneytransfers")
public class MoneyTransfer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="sender", nullable=false, length=64)
    private String sender;
    @Column(name="recipient", nullable=false, length=64)
    private String recipient;
    private BigDecimal amount;
    @Column(name="checked")
    private boolean checked;

    public MoneyTransfer(){}
    public MoneyTransfer(String sender, String recipient, BigDecimal amount){
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.checked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    
}
