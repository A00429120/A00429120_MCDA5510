/**
 * Assignment3ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package assignment3.com.web.service;

public class Assignment3ServiceLocator extends org.apache.axis.client.Service implements assignment3.com.web.service.Assignment3Service {

    public Assignment3ServiceLocator() {
    }


    public Assignment3ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public Assignment3ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for Assignment3
    private java.lang.String Assignment3_address = "http://dev.cs.smu.ca:8055/Assignment_3/services/Assignment3";

    public java.lang.String getAssignment3Address() {
        return Assignment3_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String Assignment3WSDDServiceName = "Assignment3";

    public java.lang.String getAssignment3WSDDServiceName() {
        return Assignment3WSDDServiceName;
    }

    public void setAssignment3WSDDServiceName(java.lang.String name) {
        Assignment3WSDDServiceName = name;
    }

    public assignment3.com.web.service.Assignment3 getAssignment3() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(Assignment3_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAssignment3(endpoint);
    }

    public assignment3.com.web.service.Assignment3 getAssignment3(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            assignment3.com.web.service.Assignment3SoapBindingStub _stub = new assignment3.com.web.service.Assignment3SoapBindingStub(portAddress, this);
            _stub.setPortName(getAssignment3WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAssignment3EndpointAddress(java.lang.String address) {
        Assignment3_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (assignment3.com.web.service.Assignment3.class.isAssignableFrom(serviceEndpointInterface)) {
                assignment3.com.web.service.Assignment3SoapBindingStub _stub = new assignment3.com.web.service.Assignment3SoapBindingStub(new java.net.URL(Assignment3_address), this);
                _stub.setPortName(getAssignment3WSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("Assignment3".equals(inputPortName)) {
            return getAssignment3();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.web.com.assignment3", "Assignment3Service");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.web.com.assignment3", "Assignment3"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("Assignment3".equals(portName)) {
            setAssignment3EndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
