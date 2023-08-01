package com.webapps2022.jsf;

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
import com.webapps2022.ejb.MoneyRequestEJB;
import java.math.BigDecimal;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.event.ComponentSystemEvent;

import com.webapps2022.entity.MoneyRequest;
import java.util.List;

@ManagedBean
@SessionScoped
public class RequestMoneyView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());

    @Inject
    private MoneyRequestEJB moneyRequestEJB;
    @Inject
    private UserEJB userEJB;

    private String requestor;
    private String giver;
    private String amount;
    private String currency;
    
    private List<MoneyRequest> pendingRequests;
    
    @PostConstruct
    public void init(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userRequestor = (User)sessionMap.get("User");
        
        currency = userRequestor.getCurrency();
    }
    
    // Refresh the notifications on page load
    public void onPageLoad(){
         //String transactions = transactionsEJB.getUserTransactions(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userProfile = (User)sessionMap.get("User");
        String user = userProfile.getUsername();
        
        // List of unread transfers
        pendingRequests = moneyRequestEJB.getRequestsSentFromMe(user);
        
        // Convert request amount from the native GB Pounds used for all transactions in the DB
        // GB Pounds is used for consistency in the DB
        // But can be converted back to the user's currency for display
        
        log.info("Getting requests list");
                
        if (pendingRequests != null){
            log.info("List not null");
            for (int i = 0; i < pendingRequests.size(); i++){
                // Mark the transactions as checked, since they are now rendered on the screen
                BigDecimal originalAmount = pendingRequests.get(i).getAmount();

                BigDecimal convertedAmount = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), originalAmount);
                convertedAmount = convertedAmount.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                pendingRequests.get(i).setAmount(convertedAmount);
                
                log.info("Prinitng giver");
                log.info(pendingRequests.get(i).getGiver());
            }
        }
        
    }
    
    // Function to validate form fields
    public void validateFields(ComponentSystemEvent event){
        // Verify giver exists
        log.info("Verifying before send");
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        // Object for the user calling this function
        User userRequestor = (User)sessionMap.get("User");
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        
        
        UIInput uiGiver = (UIInput) components.findComponent("giver");
        String giverStr = uiGiver.getLocalValue() == null ? "" : uiGiver.getLocalValue().toString();
        String giverId = uiGiver.getClientId();
                
        User userGiver = userEJB.findUserById(giverStr);
        // Check if giver exists
        if (userGiver == null) {
            log.info("Giver is invalid");
            
            FacesMessage msg = new FacesMessage("Giver does not exist");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(giverId, msg);
            facesContext.renderResponse();
        }
        
        // Check if giver is self
        if (new String(giverStr).equals(userRequestor.getUsername())){
            log.info("Giver is invalid");
            
            FacesMessage msg = new FacesMessage("Cannot request from self");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            facesContext.addMessage(giverId, msg);
            facesContext.renderResponse();
        }
        
        UIInput uiAmount = (UIInput) components.findComponent("amount");
        String amountStr = uiAmount.getLocalValue() == null ? "" : uiAmount.getLocalValue().toString();
    
    }
    
    // Function to request for money
    public String requestMoney(){
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        // Object for the user calling this function
        User userRequestor = (User)sessionMap.get("User");
        
        User userGiver = userEJB.findUserById(giver);
        
        BigDecimal amt = new BigDecimal(amount);
        log.info(String.valueOf(amt));
        /* Convert from user's currency to GB Pound for consistent book keeping */
        BigDecimal roundedAmount = JAX_RS_Client.convertCurrency(userRequestor.getCurrency(), "GB Pound", amt);
        
        log.info(String.valueOf(roundedAmount));
        MoneyRequest request = new MoneyRequest(
            userRequestor.getUsername(), userGiver.getUsername(), roundedAmount);
        
        log.info(String.valueOf(request.getAmount()));
        
        moneyRequestEJB.requestMoney(request);
        
        return "request_money?faces-redirect=true";
        
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

    public List<MoneyRequest> getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(List<MoneyRequest> pendingRequests) {
        this.pendingRequests = pendingRequests;
    }
    
    
    
}
