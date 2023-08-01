package com.webapps2022.jsf;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.webapps2022.entity.User;

import com.webapps2022.entity.MoneyRequest;
import com.webapps2022.ejb.MoneyRequestEJB;
import com.webapps2022.ejb.MoneyTransferEJB;
import com.webapps2022.ejb.SendMoneyEJB;
import com.webapps2022.entity.MoneyTransfer;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class ReceivedRequestsView implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger log = Logger.getLogger(LoginView.class.getName());
 
    @Inject
    private MoneyRequestEJB requestsEJB;
    @Inject
    private SendMoneyEJB sendEJB;
    @Inject
    private MoneyTransferEJB transactionsEJB;
    
    private List<MoneyRequest> moneyRequests;
    
    
    // Refresh the notifications on page load
    public void onPageLoad(){
         //String transactions = requestsEJB.getUserTransactions(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userProfile = (User)sessionMap.get("User");
        String user = userProfile.getUsername();
        
        // List of unread transfers
        moneyRequests = requestsEJB.getPendingRequestsSentToMe(user);
        
        // Convert transaction amount from the native GB Pounds used for all transactions in the DB
        // GB Pounds is used for consistency in the DB
        // But can be converted back to the user's currency for display
        if (moneyRequests != null){
            for (int i = 0; i < moneyRequests.size(); i++){
                BigDecimal originalAmount = moneyRequests.get(i).getAmount();

                BigDecimal convertedAmount = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), originalAmount);
                convertedAmount = convertedAmount.setScale(0, BigDecimal.ROUND_HALF_EVEN);
                moneyRequests.get(i).setAmount(convertedAmount);
            }
        }
        
    }

    public String accept(){
        // Get passed parameters
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String,String> params = externalContext.getRequestParameterMap();
        Map<String, Object> sessionMap = externalContext.getSessionMap();
        User userProfile = (User)sessionMap.get("User");
        String user = userProfile.getUsername();
       
        // Assign parameters
        String requestId = params.get("requestId");
        String requestor = params.get("requestor");
        BigDecimal amount = new BigDecimal(params.get("amount")) ;
        
        // Get account balance
        BigDecimal balance = transactionsEJB.getBalance(userProfile.getUsername());
        // Convert balance from GB Pound used internally for the DB to user's currency
        BigDecimal convertedBalance = JAX_RS_Client.convertCurrency("GB Pound", userProfile.getCurrency(), balance);
        convertedBalance = convertedBalance.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        
        if (convertedBalance.compareTo(amount) == 1){
            // Convert from user's currency to GB Poun
            BigDecimal convertedAmount = JAX_RS_Client.convertCurrency(userProfile.getCurrency(), "GB Pound", amount);
            
            // Send the money
            MoneyTransfer transaction = new MoneyTransfer(
                userProfile.getUsername(), requestor, convertedAmount);
            log.info(String.valueOf(transaction.getAmount()));
            sendEJB.sendMoney(transaction);

            // Mark as confirmed
            requestsEJB.markRequestAsConfirmed(Long.valueOf(requestId));
        }
        
        // Send money
        //transferEJB.
        return "received_requests?faces-redirect=true";
    }
    
    public String reject(){
        // Get passed parameters
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        Map<String,String> params = externalContext.getRequestParameterMap();
        // Assign parameters
        Long requestId = Long.valueOf(params.get("requestId"));
        
        
        requestsEJB.markRequestAsRejected(requestId);
        return "received_requests?faces-redirect=true";
    }
    
    public List<MoneyRequest> getMoneyRequests() {
        return moneyRequests;
    }

    public void setMoneyRequests(List<MoneyRequest> moneyRequests) {
        this.moneyRequests = moneyRequests;
    }

    
    
 }