package com.webapps2022.jsf;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.webapps2022.entity.User;

import com.webapps2022.entity.MoneyTransfer;
import com.webapps2022.ejb.MoneyTransferEJB;
import com.webapps2022.ejb.UserEJB;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class TransactionsViewByAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());

    
    @Inject
    private MoneyTransferEJB transactionsEJB;
    @Inject
    private UserEJB userEJB;
    
    private String sentTo;
    private String amountSent;
    private List<MoneyTransfer> outboundTransfers;
    
    private String receivedFrom;
    private String amountReceived;
    private List<MoneyTransfer> inboundTransfers;
    
    private String user;
    
    public void onPageLoad(){
        log.info("logging start");
        Map<String, String> mapping = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        user = mapping.get("username");
        log.info(user);
        
        User userProfile = userEJB.findUserById(user);
        log.info(userProfile.getUsername());
        
        List<List<MoneyTransfer>> transactionsList = transactionsEJB.getUserTransactions(user);
        
        outboundTransfers = transactionsList.get(0);
        inboundTransfers = transactionsList.get(1);
        
        // Convert transactions from the native GB Pounds used for all transactions in the DB
        // GB Pounds is used for consistency in the DB
        // But can be converted back to the user's currency for display
        if (outboundTransfers != null){
            for (int i = 0; i < outboundTransfers.size(); i++){
                // Mark the transactions as checked, since they are now rendered on the screen
                BigDecimal originalAmount = outboundTransfers.get(i).getAmount();

                BigDecimal convertedAmount = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), originalAmount);
                convertedAmount = convertedAmount.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                outboundTransfers.get(i).setAmount(convertedAmount);
            }
        }
        if (inboundTransfers != null){
            for (int i = 0; i < inboundTransfers.size(); i++){
                // Mark the transactions as checked, since they are now rendered on the screen
                BigDecimal originalAmount = inboundTransfers.get(i).getAmount();

                BigDecimal convertedAmount = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), originalAmount);
                convertedAmount = convertedAmount.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                inboundTransfers.get(i).setAmount(convertedAmount);
            }
        }
        
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getAmountSent() {
        return amountSent;
    }

    public void setAmountSent(String amountSent) {
        this.amountSent = amountSent;
    }

    public List<MoneyTransfer> getOutboundTransfers() {
        return outboundTransfers;
    }

    public void setOutboundTransfers(List<MoneyTransfer> outboundTransfers) {
        this.outboundTransfers = outboundTransfers;
    }

    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public String getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(String amountReceived) {
        this.amountReceived = amountReceived;
    }

    public List<MoneyTransfer> getInboundTransfers() {
        return inboundTransfers;
    }

    public void setInboundTransfers(List<MoneyTransfer> inboundTransfers) {
        this.inboundTransfers = inboundTransfers;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
    
}