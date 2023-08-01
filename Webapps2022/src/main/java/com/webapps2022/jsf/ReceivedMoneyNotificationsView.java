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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.webapps2022.jsf.JAX_RS_Client;

@ManagedBean
@SessionScoped
public class ReceivedMoneyNotificationsView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());

    
    @Inject
    private MoneyTransferEJB transactionsEJB;
    
    private String receivedFrom;
    private String amountReceived;
    private List<MoneyTransfer> inboundUnreadTransfers;
    
    // Refresh the notifications on page load
    public void onPageLoad(){
         //String transactions = transactionsEJB.getUserTransactions(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userProfile = (User)sessionMap.get("User");
        String user = userProfile.getUsername();
        
        // List of unread transfers
        inboundUnreadTransfers = transactionsEJB.getUnreadInboundTransfers(user);
        
        // Convert transaction amount from the native GB Pounds used for all transactions in the DB
        // GB Pounds is used for consistency in the DB
        // But can be converted back to the user's currency for display
        if (inboundUnreadTransfers != null){
            for (int i = 0; i < inboundUnreadTransfers.size(); i++){
                // Mark the transactions as checked, since they are now rendered on the screen
                BigDecimal originalAmount = inboundUnreadTransfers.get(i).getAmount();

                BigDecimal convertedAmount = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), originalAmount);
                convertedAmount = convertedAmount.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                inboundUnreadTransfers.get(i).setAmount(convertedAmount);
            }
        }
        
        
        // Mark the unread messages as reas since opened in notifications
        for (int i = 0; i < inboundUnreadTransfers.size(); i++){
            // Mark the transactions as checked, since they are now rendered on the screen
            transactionsEJB.markTransferAsChecked(inboundUnreadTransfers.get(i).getId());
        }
        
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

    public List<MoneyTransfer> getInboundUnreadTransfers() {
        return inboundUnreadTransfers;
    }

    public void setInboundUnreadTransfers(List<MoneyTransfer> inboundUnreadTransfers) {
        this.inboundUnreadTransfers = inboundUnreadTransfers;
    }

    
 }