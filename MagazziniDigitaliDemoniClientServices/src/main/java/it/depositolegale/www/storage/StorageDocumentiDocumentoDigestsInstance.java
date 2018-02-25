/**
 * StorageDocumentiDocumentoDigestsInstance.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package it.depositolegale.www.storage;

public class StorageDocumentiDocumentoDigestsInstance implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StorageDocumentiDocumentoDigestsInstance(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _SHA1 = "SHA1";
    public static final java.lang.String _SHA256 = "SHA256";
    public static final java.lang.String _MD5 = "MD5";
    public static final StorageDocumentiDocumentoDigestsInstance SHA1 = new StorageDocumentiDocumentoDigestsInstance(_SHA1);
    public static final StorageDocumentiDocumentoDigestsInstance SHA256 = new StorageDocumentiDocumentoDigestsInstance(_SHA256);
    public static final StorageDocumentiDocumentoDigestsInstance MD5 = new StorageDocumentiDocumentoDigestsInstance(_MD5);
    public java.lang.String getValue() { return _value_;}
    public static StorageDocumentiDocumentoDigestsInstance fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StorageDocumentiDocumentoDigestsInstance enumeration = (StorageDocumentiDocumentoDigestsInstance)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StorageDocumentiDocumentoDigestsInstance fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StorageDocumentiDocumentoDigestsInstance.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.depositolegale.it/storage", ">>>>>storage>documenti>documento>digests>instance"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
