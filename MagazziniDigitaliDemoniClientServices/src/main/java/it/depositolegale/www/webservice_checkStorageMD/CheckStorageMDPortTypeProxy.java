package it.depositolegale.www.webservice_checkStorageMD;

public class CheckStorageMDPortTypeProxy implements it.depositolegale.www.webservice_checkStorageMD.CheckStorageMDPortType {
  private String _endpoint = null;
  private it.depositolegale.www.webservice_checkStorageMD.CheckStorageMDPortType checkStorageMDPortType = null;
  
  public CheckStorageMDPortTypeProxy() {
    _initCheckStorageMDPortTypeProxy();
  }
  
  public CheckStorageMDPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initCheckStorageMDPortTypeProxy();
  }
  
  private void _initCheckStorageMDPortTypeProxy() {
    try {
      checkStorageMDPortType = (new it.depositolegale.www.webservice_checkStorageMD.CheckStorageMDServiceLocator()).getCheckStorageMDPort();
      if (checkStorageMDPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)checkStorageMDPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)checkStorageMDPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (checkStorageMDPortType != null)
      ((javax.xml.rpc.Stub)checkStorageMDPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public it.depositolegale.www.webservice_checkStorageMD.CheckStorageMDPortType getCheckStorageMDPortType() {
    if (checkStorageMDPortType == null)
      _initCheckStorageMDPortTypeProxy();
    return checkStorageMDPortType;
  }
  
  public it.depositolegale.www.storage.Storage checkStorageMDOperation(it.depositolegale.www.storage.Documenti documenti) throws java.rmi.RemoteException{
    if (checkStorageMDPortType == null)
      _initCheckStorageMDPortTypeProxy();
    return checkStorageMDPortType.checkStorageMDOperation(documenti);
  }
  
  
}