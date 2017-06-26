package com.eme.mas.environment;

/**
 * Created by luokaiwen on 15/7/24.
 * <p>
 * 应用配置
 */
public enum KConfig {

    INSTANCE {

        private final String RELEASE = "RELEASE";               // 线上
        private final String DEBUG_RELEASE = "DEBUG_RELEASE";   // 线上测试
        private final String DEBUG = "DEBUG";                   // 测试
        private final String TAISHAN = "TAISHAN";               // 泰山
        private final String AFANTI = "AFANTI";                 // 阿凡提

        // 当前环境
        private String mCurEvironment = DEBUG_RELEASE;

        @Override
        public String getUrlPrefix() {

            if (RELEASE.equals(mCurEvironment)) {

                return URL_PREFIX_RELEASE;

            } else if (DEBUG_RELEASE.equals(mCurEvironment)) {

                return URL_PREFIX_DEBUG_RELEASE;

            } else if (DEBUG.equals(mCurEvironment)) {

                return URL_PREFIX_DEBUG;

            } else if (TAISHAN.equals(mCurEvironment)) {

                return URL_PREFIX_TAISHAN;

            } else if (AFANTI.equals(mCurEvironment)) {

                return URL_PREFIX_AFANTI;
            }

            return URL_PREFIX_RELEASE;
        }

        @Override
        public boolean isDebug() {

            if (RELEASE.equals(mCurEvironment)) {
                return false;
            }
            return true;
        }

        @Override
        public String getPayHost() {

            if (RELEASE.equals(mCurEvironment)) {

                return PAY_HOST;

            } else if (DEBUG_RELEASE.equals(mCurEvironment)) {

                return PAY_HOST;
            }

            return PAY_HOST_TEST;
        }


        //        private final String AIYUNDONG_HTTP = "http://192.168.1.17:8080";
//        private final String AIYUNDONG_HTTP = "http://192.168.50.218:8080";
        private String AIYUNDONG_HTTP = "http://192.168.50.245:8080";

        private final String SCHEME_HTTP = "http";                          // http协议
        private final String SCHEME_HTTPS = "https";                        // https协议
        private final String PORT_HTTP = "80";                              // http端口80
        private final String PORT_HTTPS = "443";                            // https端口443
        private final String PORT_TEST = "8080";                            // 测试端口号
        private final String HOST_TEST = "apitest.wonderworld.com";         // 测试服务器地址
        private final String HOST_RELEASE = "api.wonderworld.com";          // 线上http协议服务器地址
        private final String HOST_RELEASE_HTTPS = "pay.wonderworld.com";    // 线上https协议服务器地址
        private final String HOST_TAISHAN = "192.168.1.188";                // 泰山测试服务器地址
        private final String HOST_AFANTI = "192.168.1.141";                 // 阿凡提测试服务器地址
        private final String PAY_HOST_TEST = "paytest.wonderworld.com";     // 测试支付回调地址
        private final String PAY_HOST = "pay.wonderworld.com";              // 线上支付回调地址

        //private final String URL_PREFIX_RELEASE = SCHEME_HTTPS + "://" + HOST_RELEASE_HTTPS + ":" + PORT_HTTPS + "/";
        //private final String URL_PREFIX_DEBUG_RELEASE = SCHEME_HTTP + "://" + HOST_RELEASE + ":" + PORT_HTTP + "/";
        //private final String URL_PREFIX_DEBUG = SCHEME_HTTP + "://" + HOST_TEST + ":" + PORT_HTTP + "/";
        private final String URL_PREFIX_TAISHAN = SCHEME_HTTP + "://" + HOST_TAISHAN + ":" + PORT_TEST + "/";
        private final String URL_PREFIX_AFANTI = SCHEME_HTTP + "://" + HOST_AFANTI + ":" + PORT_TEST + "/";

        private final String URL_PREFIX_DEBUG = AIYUNDONG_HTTP + "/";
        private final String URL_PREFIX_DEBUG_RELEASE = AIYUNDONG_HTTP + "/";
        private final String URL_PREFIX_RELEASE = AIYUNDONG_HTTP + "/";


    };

    /**
     * 获取加载图片的host
     */
//    public static String HOST_URL = "http://192.168.1.17:80";
    public static String HOST_URL = "http://192.168.50.245";//图片的主地址

    public static String HOST = "http://192.168.50.218:8080/";//项目主地址
//    public static String HOST = "http://192.168.50.247:8080/";//项目主地址
//    public static String HOST = "http://192.168.50.245:8088/";//项目主地址
//    public static String HOST = "http://192.168.1.17:8080/";//项目主地址

    public static String getHOST() {
        return HOST;
    }

    public static void setHOST(String HOST) {
        KConfig.HOST = HOST;
    }

    public static String PAYHOST = "http://192.168.1.17:8080/";//支付主地址

    public static String getHostUrl() {
        return HOST_URL;
    }

    public static void setHostUrl(String hostUrl) {
        HOST_URL = hostUrl;
    }

    public static String getPAYHOST() {
        return PAYHOST;
    }

    public static void setPAYHOST(String PAYHOST) {
        KConfig.PAYHOST = PAYHOST;
    }


    /**
     * 获取请求地址前缀
     *
     * @return
     */
    public abstract String getUrlPrefix();

    /**
     * 是否是调试模式
     *
     * @return
     */
    public abstract boolean isDebug();

    /**
     * 获取支付host
     *
     * @return
     */
    public abstract String getPayHost();


}
