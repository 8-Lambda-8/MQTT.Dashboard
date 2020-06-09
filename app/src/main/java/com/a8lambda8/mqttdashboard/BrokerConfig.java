package com.a8lambda8.mqttdashboard;

import java.io.Serializable;

public class BrokerConfig implements Serializable{
    private String name;
    private String address;
    private int port;
    private String uname;
    private String passwd;
    private String clientID;


    BrokerConfig(){};

    BrokerConfig(String name,String address,int port, String uname,String passwd, String clientID){

        this.name = name;
        this.address = address;
        this.port = port;
        this.uname = uname;
        this.passwd = passwd;
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
