/**
 * DocumentiDocumento.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.depositolegale.www.storage;

public class DocumentiDocumento  implements java.io.Serializable {
    private java.lang.String nomeFile;

    private java.lang.String digest;

    private java.lang.String esito;

    private java.util.Calendar dataMod;  // attribute

    public DocumentiDocumento() {
    }

    public DocumentiDocumento(
           java.lang.String nomeFile,
           java.lang.String digest,
           java.lang.String esito,
           java.util.Calendar dataMod) {
           this.nomeFile = nomeFile;
           this.digest = digest;
           this.esito = esito;
           this.dataMod = dataMod;
    }


    /**
     * Gets the nomeFile value for this DocumentiDocumento.
     * 
     * @return nomeFile
     */
    public java.lang.String getNomeFile() {
        return nomeFile;
    }


    /**
     * Sets the nomeFile value for this DocumentiDocumento.
     * 
     * @param nomeFile
     */
    public void setNomeFile(java.lang.String nomeFile) {
        this.nomeFile = nomeFile;
    }


    /**
     * Gets the digest value for this DocumentiDocumento.
     * 
     * @return digest
     */
    public java.lang.String getDigest() {
        return digest;
    }


    /**
     * Sets the digest value for this DocumentiDocumento.
     * 
     * @param digest
     */
    public void setDigest(java.lang.String digest) {
        this.digest = digest;
    }


    /**
     * Gets the esito value for this DocumentiDocumento.
     * 
     * @return esito
     */
    public java.lang.String getEsito() {
        return esito;
    }


    /**
     * Sets the esito value for this DocumentiDocumento.
     * 
     * @param esito
     */
    public void setEsito(java.lang.String esito) {
        this.esito = esito;
    }


    /**
     * Gets the dataMod value for this DocumentiDocumento.
     * 
     * @return dataMod
     */
    public java.util.Calendar getDataMod() {
        return dataMod;
    }


    /**
     * Sets the dataMod value for this DocumentiDocumento.
     * 
     * @param dataMod
     */
    public void setDataMod(java.util.Calendar dataMod) {
        this.dataMod = dataMod;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentiDocumento)) return false;
        DocumentiDocumento other = (DocumentiDocumento) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.nomeFile==null && other.getNomeFile()==null) || 
             (this.nomeFile!=null &&
              this.nomeFile.equals(other.getNomeFile()))) &&
            ((this.digest==null && other.getDigest()==null) || 
             (this.digest!=null &&
              this.digest.equals(other.getDigest()))) &&
            ((this.esito==null && other.getEsito()==null) || 
             (this.esito!=null &&
              this.esito.equals(other.getEsito()))) &&
            ((this.dataMod==null && other.getDataMod()==null) || 
             (this.dataMod!=null &&
              this.dataMod.equals(other.getDataMod())));
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
        if (getNomeFile() != null) {
            _hashCode += getNomeFile().hashCode();
        }
        if (getDigest() != null) {
            _hashCode += getDigest().hashCode();
        }
        if (getEsito() != null) {
            _hashCode += getEsito().hashCode();
        }
        if (getDataMod() != null) {
            _hashCode += getDataMod().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentiDocumento.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>documenti>documento"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("dataMod");
        attrField.setXmlName(new javax.xml.namespace.QName("", "dataMod"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomeFile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "nomeFile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("digest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "digest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("esito");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", "esito"));
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
