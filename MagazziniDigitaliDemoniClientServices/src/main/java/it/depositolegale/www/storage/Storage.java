/**
 * Storage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.depositolegale.www.storage;

public class Storage  implements java.io.Serializable {
    private it.depositolegale.www.storage.StorageDocumenti documenti;

    private java.lang.String esito;  // attribute

    public Storage() {
    }

    public Storage(
           it.depositolegale.www.storage.StorageDocumenti documenti,
           java.lang.String esito) {
           this.documenti = documenti;
           this.esito = esito;
    }


    /**
     * Gets the documenti value for this Storage.
     * 
     * @return documenti
     */
    public it.depositolegale.www.storage.StorageDocumenti getDocumenti() {
        return documenti;
    }


    /**
     * Sets the documenti value for this Storage.
     * 
     * @param documenti
     */
    public void setDocumenti(it.depositolegale.www.storage.StorageDocumenti documenti) {
        this.documenti = documenti;
    }


    /**
     * Gets the esito value for this Storage.
     * 
     * @return esito
     */
    public java.lang.String getEsito() {
        return esito;
    }


    /**
     * Sets the esito value for this Storage.
     * 
     * @param esito
     */
    public void setEsito(java.lang.String esito) {
        this.esito = esito;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Storage)) return false;
        Storage other = (Storage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.documenti==null && other.getDocumenti()==null) || 
             (this.documenti!=null &&
              this.documenti.equals(other.getDocumenti()))) &&
            ((this.esito==null && other.getEsito()==null) || 
             (this.esito!=null &&
              this.esito.equals(other.getEsito())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDocumenti() != null) {
            _hashCode += getDocumenti().hashCode();
        }
        if (getEsito() != null) {
            _hashCode += getEsito().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Storage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">storage"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("esito");
        attrField.setXmlName(new javax.xml.namespace.QName("", "esito"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documenti");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "documenti"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>storage>documenti"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
