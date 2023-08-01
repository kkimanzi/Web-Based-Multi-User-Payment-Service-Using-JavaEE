package com.webpages2022.restservice;

import com.webapps2022.jsf.LoginView;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

// The rest service is hosted at the context path
@Path("/{currency1}/{currency2}/{amount_of_currency}")
@ApplicationPath("/conversion")

public class ConversionService extends Application{
    public static final BigDecimal gbPound_to_dollar = new BigDecimal(1.31);
    public static final BigDecimal gbPound_to_euro = new BigDecimal(1.20);
    public static final BigDecimal dollar_to_euro = new BigDecimal(0.92);
    
    private static Logger log = Logger.getLogger(LoginView.class.getName());
    
    @GET
    @Produces("text/plain")
    
    public Response getConvertedAmount(
            @PathParam("currency1") String currency1,
            @PathParam("currency2") String currency2,
            @PathParam("amount_of_currency") String original_amount) {
        
        String result;
        BigDecimal tmp;
        switch (currency1){
            case "GB Pound":
                switch (currency2){
                    case "GB Pound":
                        result = original_amount;
                        break;
                    case "US Dollar":
                        tmp =(new BigDecimal(original_amount)).multiply(gbPound_to_dollar);
                        tmp = tmp.setScale(4, BigDecimal.ROUND_HALF_EVEN);
                        result = String.valueOf(tmp);
                        break;
                    case "Euro":
                        tmp = (new BigDecimal(original_amount)).multiply(gbPound_to_euro);
                        tmp = tmp.setScale(4, BigDecimal.ROUND_HALF_EVEN);
                        result = String.valueOf(tmp);result = String.valueOf(tmp);
                        break;
                    default:
                        result =  "bad";
                        break;
                }
                break;
            case "US Dollar":
                switch (currency2){
                    case "GB Pound":
                        tmp = (new BigDecimal(original_amount)).divide(gbPound_to_dollar, 4, RoundingMode.HALF_UP);
                        result = String.valueOf(tmp);
                        break;
                    case "US Dollar":
                        result = original_amount;
                        break;
                    case "Euro":
                        tmp =(new BigDecimal(original_amount)).divide(dollar_to_euro, 4, RoundingMode.HALF_UP);
                       result = String.valueOf(tmp);
                        break;
                    default:
                        result =  "bad";
                        break;   
                }
                break;
            case "Euro":
                switch (currency2){
                    case "GB Pound":
                        log.info("HERE");
                        tmp =(new BigDecimal(original_amount)).divide(gbPound_to_euro, 4, RoundingMode.HALF_UP);
                       result = String.valueOf(tmp);
                        break;
                    case "US Dollar":
                        tmp =(new BigDecimal(original_amount)).divide(dollar_to_euro, 4, RoundingMode.HALF_UP);
                       result = String.valueOf(tmp);
                        break;
                    case "Euro":
                        result = original_amount;
                        break;
                    default:
                        result =  "bad";
                        break;   
                }
                break;
            default:
                result =  "bad";
                break;
        }
        
        
        if (result != "bad"){
            return Response.status(Response.Status.OK)
                    .entity(result)
                    .build();
        } else {
            // Return error 400 - Bad Request
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
    }
}
