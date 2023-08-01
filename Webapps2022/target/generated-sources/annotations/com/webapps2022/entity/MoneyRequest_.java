package com.webapps2022.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-04-08T04:13:36")
@StaticMetamodel(MoneyRequest.class)
public class MoneyRequest_ { 

    public static volatile SingularAttribute<MoneyRequest, BigDecimal> amount;
    public static volatile SingularAttribute<MoneyRequest, Long> id;
    public static volatile SingularAttribute<MoneyRequest, String> giver;
    public static volatile SingularAttribute<MoneyRequest, String> requestor;
    public static volatile SingularAttribute<MoneyRequest, String> status;

}