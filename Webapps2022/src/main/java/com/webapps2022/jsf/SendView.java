package com.webapps2022.jsf;

import com.webapps2022.ejb.MoneyTransferEJB;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.webapps2022.ejb.UserEJB;
import com.webapps2022.entity.User;

import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.ejb.SendMoneyEJB;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@ManagedBean
@SessionScoped
public class SendView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());

    @Inject
    private SendMoneyEJB sendMoneyEJB;
    @Inject
    private UserEJB userEJB;
    @Inject
    private MoneyTransferEJB transactionsEJB;

    private String sender;
    private String recipient;
    private String amount;
    private String currency;
    
    @PostConstruct
    public void init(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userSender = (User)sessionMap.get("User");
        
        currency = userSender.getCurrency();
    }
    
    public void validateFields(ComponentSystemEvent event){
        // Verify recipient exists
        log.info("Verifying before send");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userSender = (User)sessionMap.get("User");
        
        UIInput uiRecipient = (UIInput) components.findComponent("recipient");
        String recipientStr = uiRecipient.getLocalValue() == null ? "" : uiRecipient.getLocalValue().toString();
        String recipientId = uiRecipient.getClientId();
                
        User userRecipient = userEJB.findUserById(recipientStr);
        if (userRecipient == null) {
            log.info("Recipient is invalid");
            
            FacesMessage msg = new FacesMessage("Recipient does not exist");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(recipientId, msg);
            facesContext.renderResponse();
        }
        if (new String(recipientStr).equals(userSender.getUsername())){
            log.info("Recipient is invalid");
            
            FacesMessage msg = new FacesMessage("Cannot send to self");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(recipientId, msg);
            facesContext.renderResponse();
        }
        
        UIInput uiAmount = (UIInput) components.findComponent("amount");
        String amountStr = uiAmount.getLocalValue() == null ? "" : uiAmount.getLocalValue().toString();
        BigDecimal amountEntered = new BigDecimal(amountStr);
        
        // Get account balance
        BigDecimal balance = transactionsEJB.getBalance(userSender.getUsername());
        // Convert balance from GB Pound used internally for the DB to user's currency
        BigDecimal convertedBalance = JAX_RS_Client.convertCurrency("GB Pound", userSender.getCurrency(), balance);
        convertedBalance = convertedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        
        if (convertedBalance.compareTo(amountEntered) != 1){
            // balance =< amountEntered
            // user can't send
            
            FacesMessage msg = new FacesMessage("Account balance less than amount");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(recipientId, msg);
            facesContext.renderResponse();
        }
    }
    public String sendMoney(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userSender = (User)sessionMap.get("User");
        
        User userRecipient = userEJB.findUserById(recipient);
        
        BigDecimal amt = new BigDecimal(amount);
        log.info(String.valueOf(amt));
        /* Convert from user's currency to GB Pound for consistent book keeping */
        BigDecimal roundedAmount = JAX_RS_Client.convertCurrency(userSender.getCurrency(), "GB Pound", amt);
        
        log.info(String.valueOf(roundedAmount));
        MoneyTransfer transaction = new MoneyTransfer(
                userSender.getUsername(), userRecipient.getUsername(), roundedAmount);
        
        log.info(String.valueOf(transaction.getAmount()));
        sendMoneyEJB.sendMoney(transaction);
        
        return "dashboard?faces-redirect=true";
        
        //log.info(amount);
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    
    
}
