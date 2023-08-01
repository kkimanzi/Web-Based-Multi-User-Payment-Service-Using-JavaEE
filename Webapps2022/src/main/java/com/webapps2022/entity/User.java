/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps2022.entity;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({
    @NamedQuery(name = "findUserById", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "getAllUsers", query = "SELECT u FROM User u, Group g WHERE u.username = g.username")
})

@Table(name="users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(nullable=false, length=255)
    private String username;
    @Column(name="password", nullable=false, length=64)
    private String password;
    @Column(name="currency", nullable=false, length=64)
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public User(){}
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.currency = "GB Pound"; //default
    }
    
    public User(String username, String password, String currency) {
        this.username = username;
        this.password = password;
        this.currency = currency;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    
}