/**
 * Documenti.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.depositolegale.www.storage;

public class Documenti  implements java.io.Serializable {
    private it.depositolegale.www.storage.DocumentiDocumento[] documento;

    private java.math.BigInteger numDoc;

    private java.math.BigInteger numErr;

    public Documenti() {
    }

    public Documenti(
           it.depositolegale.www.storage.DocumentiDocumento[] documento,
           java.math.BigInteger numDoc,
           java.math.BigInteger numErr) {
           this.documento = documento;
           this.numDoc = numDoc;
           this.numErr = numErr;
    }


    /**
     * Gets the documento value for this Documenti.
     * 
     * @return documento
     */
    public it.depositolegale.www.storage.DocumentiDocumento[] getDocumento() {
        return documento;
    }


    /**
     * Sets the documento value for this Documenti.
     * 
     * @param documento
     */
    public void setDocumento(it.depositolegale.www.storage.DocumentiDocumento[] documento) {
        this.documento = documento;
    }

    public it.depositolegale.www.storage.DocumentiDocumento getDocumento(int i) {
        return this.documento[i];
    }

    public void setDocumento(int i, it.depositolegale.www.storage.DocumentiDocumento _value) {
        this.documento[i] = _value;
    }


    /**
     * Gets the numDoc value for this Documenti.
     * 
     * @return numDoc
     */
    public java.math.BigInteger getNumDoc() {
        return numDoc;
    }


    /**
     * Sets the numDoc value for this Documenti.
     * 
     * @param numDoc
     */
    public void setNumDoc(java.math.BigInteger numDoc) {
        this.numDoc = numDoc;
    }


    /**
     * Gets the numErr value for this Documenti.
     * 
     * @return numErr
     */
    public java.math.BigInteger getNumErr() {
        return numErr;
    }


    /**
     * Sets the numErr value for this Documenti.
     * 
     * @param numErr
     */
    public void setNumErr(java.math.BigInteger numErr) {
        this.numErr = numErr;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Documenti)) return false;
        Documenti other = (Documenti) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.documento==null && other.getDocumento()==null) || 
             (this.documento!=null &&
              java.util.Arrays.equals(this.documento, other.getDocumento()))) &&
            ((this.numDoc==null && other.getNumDoc()==null) || 
             (this.numDoc!=null &&
              this.numDoc.equals(other.getNumDoc()))) &&
            ((this.numErr==null && other.getNumErr()==null) || 
             (this.numErr!=null &&
              this.numErr.equals(other.getNumErr())));
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
        if (getDocumento() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocumento());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocumento(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getNumDoc() != null) {
            _hashCode += getNumDoc().hashCode();
        }
        if (getNumErr() != null) {
            _hashCode += getNumErr().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Documenti.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">documenti"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("documento");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "documento"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>documenti>documento"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numDoc");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "numDoc"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numErr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "numErr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
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
