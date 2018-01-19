package com.rkl.common_library.net;

import com.rkl.common_library.constant.APIServiceConfig;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okio.Buffer;

/**
 * Created by ArmGlobe on 2016/9/22.
 * 创建https的证书
 */
public class HttpsFactroy {

    protected static SSLSocketFactory getSSLSocketFactory() {

        try {
            // 使用传输层安全协议TLS(transport layer secure)
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //使用SRCA.cer 文件的形式
            //FileInputStream certInputStream = new FileInputStream(new File("srca.cer"));
            //也可以通过RFC 字符串的形式使用证书
            InputStream certInputStream = new Buffer().writeUtf8(APIServiceConfig.API_SERVICE_HTTPS_CERTIFICATE).inputStream();
            // 初始化keyStore，用来导入证书
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            //参数null 表示使用系统默认keystore，也可使用其他keystore（需事先将srca.cer 证书导入keystore 里）
            keyStore.load(null);
            //通过流创建一个证书
            Certificate certificate = CertificateFactory.getInstance("X.509")
                    .generateCertificate(certInputStream);
            // 把srca.cer 这个证书导入到KeyStore 里，别名叫做srca
            keyStore.setCertificateEntry("srca", certificate);
            // 设置使用keyStore 去进行证书校验
            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            //用我们设定好的TrustManager 去做ssl 通信协议校验，即证书校验
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}