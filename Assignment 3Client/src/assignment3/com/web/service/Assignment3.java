/**
 * Assignment3.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package assignment3.com.web.service;

public interface Assignment3 extends java.rmi.Remote {
    public java.lang.String remove(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String get(java.lang.String id) throws java.rmi.RemoteException;
    public java.lang.String update(java.lang.String id, java.lang.String choice, java.lang.String value) throws java.rmi.RemoteException;
    public java.lang.String create(java.lang.String id, java.lang.String cardNumber, java.lang.String expDate, java.lang.String nameOnCard, java.lang.String unitPrice, java.lang.String quantity) throws java.rmi.RemoteException;
    public java.lang.String getAll() throws java.rmi.RemoteException;
}
