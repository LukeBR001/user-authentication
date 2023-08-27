package com.example.userauthentication.models;

public enum Status {

        ACTIVE,
        INACTIVE,
        DELETED;

     public boolean isEnabled() {
         return this == ACTIVE;
     }
}
