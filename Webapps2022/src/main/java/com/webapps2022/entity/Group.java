/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webapps2022.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_groups")

public class Group implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String USERS_GROUP = "users";
    public static final String ADMINS_GROUP = "admins";
    
    @Id
    @Column(name="username", nullable=false, length=255)
    private String username;
    @Column(name="groupname", nullable=false, length=32)
    private String groupname;
    
    public Group() {}
    public Group(String username, String groupname) {
        this.username = username;
        this.groupname = groupname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    
    
    
}