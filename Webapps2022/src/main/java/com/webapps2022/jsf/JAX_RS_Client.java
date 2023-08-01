/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps2022.jsf;

import java.math.BigDecimal;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
public class JAX_RS_Client {
    private static Logger log = Logger.getLogger(LoginView.class.getName());
    
    // Convert from currency, to another currency
    // Calls RESTful web service
    public static BigDecimal convertCurrency(String from, String to, BigDecimal amount){
        // JAX-RS Client
        String strAmount = String.valueOf(amount);
        Client client = ClientBuilder.newClient();
        WebTarget myResource = client.target(
                "http://localhost:8080/Webapps2022/conversion/"+from+"/"+to+"/"+strAmount);
        String response = myResource.request(MediaType.TEXT_PLAIN)
                .get(String.class);
        
        return new BigDecimal(response);
    }
}
