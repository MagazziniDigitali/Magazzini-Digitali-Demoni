/**
 * StorageDocumentiDocumentoDigests.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.depositolegale.www.storage;

public class StorageDocumentiDocumentoDigests  implements java.io.Serializable {
    private it.depositolegale.www.storage.StorageDocumentiDocumentoDigestsInstance instance;

    private java.lang.String value;

    public StorageDocumentiDocumentoDigests() {
    }

    public StorageDocumentiDocumentoDigests(
           it.depositolegale.www.storage.StorageDocumentiDocumentoDigestsInstance instance,
           java.lang.String value) {
           this.instance = instance;
           this.value = value;
    }


    /**
     * Gets the instance value for this StorageDocumentiDocumentoDigests.
     * 
     * @return instance
     */
    public it.depositolegale.www.storage.StorageDocumentiDocumentoDigestsInstance getInstance() {
        return instance;
    }


    /**
     * Sets the instance value for this StorageDocumentiDocumentoDigests.
     * 
     * @param instance
     */
    public void setInstance(it.depositolegale.www.storage.StorageDocumentiDocumentoDigestsInstance instance) {
        this.instance = instance;
    }


    /**
     * Gets the value value for this StorageDocumentiDocumentoDigests.
     * 
     * @return value
     */
    public java.lang.String getValue() {
        return value;
    }


    /**
     * Sets the value value for this StorageDocumentiDocumentoDigests.
     * 
     * @param value
     */
    public void setValue(java.lang.String value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StorageDocumentiDocumentoDigests)) return false;
        StorageDocumentiDocumentoDigests other = (StorageDocumentiDocumentoDigests) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.instance==null && other.getInstance()==null) || 
             (this.instance!=null &&
              this.instance.equals(other.getInstance()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
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
        if (getInstance() != null) {
            _hashCode += getInstance().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StorageDocumentiDocumentoDigests.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>>>storage>documenti>documento>digests"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "instance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>>>>storage>documenti>documento>digests>instance"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
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
