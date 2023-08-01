package com.webapps2022.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-04-08T04:13:36")
@StaticMetamodel(MoneyTransfer.class)
public class MoneyTransfer_ { 

    public static volatile SingularAttribute<MoneyTransfer, BigDecimal> amount;
    public static volatile SingularAttribute<MoneyTransfer, String> sender;
    public static volatile SingularAttribute<MoneyTransfer, String> recipient;
    public static volatile SingularAttribute<MoneyTransfer, Boolean> checked;
    public static volatile SingularAttribute<MoneyTransfer, Long> id;

}