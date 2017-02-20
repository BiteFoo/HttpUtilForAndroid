package com.hss01248.http.https;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class HttpsUtil {

   private static List<String> certificateFiles;

    public static void addCrtificateFile(String filePath){
        if(certificateFiles==null){
            certificateFiles=new ArrayList<String>();
        }
        certificateFiles.add(filePath);
    }

    public static void setHttps(OkHttpClient.Builder builder){
        if(certificateFiles!= null && certificateFiles.size()>0){
            builder.sslSocketFactory(getSSLSocketFactory());
        }
    }



    /**
     *


     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory() {

        List<String> certificateFiles = HttpsUtil.certificateFiles;

        CertificateFactory certificateFactory;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificateFiles.size(); i++) {
                File file = new File(certificateFiles.get(i));
                if(!file.exists()){
                    throw new RuntimeException("证书文件不存在"+certificateFiles.get(i));
                }
                InputStream certificate = new FileInputStream(file);
                keyStore.setCertificateEntry(0+"", certificateFactory.generateCertificate(certificate));

                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * 指定支持的host
     * set HostnameVerifier
     * 你的host数据 列如 String hosts[]`= {“https//:aaaa,com”, “https//:bbb.com”}
     * {@link HostnameVerifier}
     */
    public static HostnameVerifier getHostnameVerifier(final List<String> hostUrls) {

        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {

            public boolean verify(String hostname, SSLSession session) {
                /*boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;*/
                return true;
            }
        };

        return TRUSTED_VERIFIER;
    }


    /**
     * 让客户端通过所有证书的验证.
     * 注意:容易导致中间人攻击,轻易不要使用
     * @param httpBuilder
     */
    @SuppressWarnings("deprecation")
	public static void setAllCerPass(OkHttpClient.Builder httpBuilder){
        X509TrustManager xtm = new X509TrustManager() {
           // @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

           // @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

           // @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] x509Certificates = new X509Certificate[]{};
                return x509Certificates;
                // return null;
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new TrustManager[]{xtm}, new SecureRandom());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }


        HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            //@Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        httpBuilder.sslSocketFactory(sslContext.getSocketFactory())
                .hostnameVerifier(DO_NOT_VERIFY);
    }



    public static SSLContext getSslContextForCertificateFile(InputStream caInput, String fileName) {
        try {
            KeyStore keyStore = getKeyStore(caInput, fileName);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext;
        } catch (Exception e) {
            String msg = "Error during creating SslContext for certificate from assets";
            //Log.e("SslUtilsAndroid", msg, e);
            throw new RuntimeException(msg);
        }
    }

    private static KeyStore getKeyStore(InputStream caInput, String fileName) {
        KeyStore keyStore = null;
        try {
           // AssetManager assetManager = context.getAssets();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //InputStream caInput = assetManager.open(fileName);
            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
              //  Log.d("SslUtilsAndroid", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);
        } catch (Exception e) {
          //  Log.e("SslUtilsAndroid","Error during getting keystore", e);
        }
        return keyStore;
    }



}
