package com.zjc.insec.insec.until;

import org.apache.http.conn.ssl.TrustStrategy;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by zjc on 2017/12/28.
 */
public class AnyTrustStrategy  implements TrustStrategy{
    @Override
    public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        return true;
    }
}
