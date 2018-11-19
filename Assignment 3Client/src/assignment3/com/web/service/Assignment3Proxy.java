package assignment3.com.web.service;

public class Assignment3Proxy implements assignment3.com.web.service.Assignment3 {
  private String _endpoint = null;
  private assignment3.com.web.service.Assignment3 assignment3 = null;
  
  public Assignment3Proxy() {
    _initAssignment3Proxy();
  }
  
  public Assignment3Proxy(String endpoint) {
    _endpoint = endpoint;
    _initAssignment3Proxy();
  }
  
  private void _initAssignment3Proxy() {
    try {
      assignment3 = (new assignment3.com.web.service.Assignment3ServiceLocator()).getAssignment3();
      if (assignment3 != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)assignment3)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)assignment3)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (assignment3 != null)
      ((javax.xml.rpc.Stub)assignment3)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public assignment3.com.web.service.Assignment3 getAssignment3() {
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3;
  }
  
  public java.lang.String get(java.lang.String id) throws java.rmi.RemoteException{
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3.get(id);
  }
  
  public java.lang.String create(java.lang.String id, java.lang.String cardNumber, java.lang.String expDate, java.lang.String nameOnCard, java.lang.String unitPrice, java.lang.String quantity) throws java.rmi.RemoteException{
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3.create(id, cardNumber, expDate, nameOnCard, unitPrice, quantity);
  }
  
  public java.lang.String remove(java.lang.String id) throws java.rmi.RemoteException{
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3.remove(id);
  }
  
  public java.lang.String getAll() throws java.rmi.RemoteException{
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3.getAll();
  }
  
  public java.lang.String update(java.lang.String id, java.lang.String choice, java.lang.String value) throws java.rmi.RemoteException{
    if (assignment3 == null)
      _initAssignment3Proxy();
    return assignment3.update(id, choice, value);
  }
  
  
}