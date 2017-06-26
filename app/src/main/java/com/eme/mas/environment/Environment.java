package com.eme.mas.environment;

/**
 * Created by luokaiwen on 15/4/27.
 * <p/>
 * 项目环境配置
 */
@Deprecated
public class Environment {

    /**
     * 开发模式
     */
    public static boolean DEVELOP = true;

    /**
     * 发布模式
     */
    public static boolean RELEASE = false;

    /**
     * 模式
     */
    public static boolean MODE = DEVELOP;

    /**
     * http协议
     */
    public static final String SCHEME_HTTP = "http";

    /**
     * http协议
     */
    public static final String SCHEME_HTTPS = "https";

    /**
     * 协议,默认为http协议
     */
    public static final String SCHEME = SCHEME_HTTP;

    /**
     * http 端口80
     */
    public static final int PORT_HTTP = 80;

    /**
     * 测试
     */
    //public static final int PORT_HTTP = 8080;

    /**
     * https 端口 443
     */
    public static final int PORT_HTTPS = 443;
    public static final int PORT = PORT_HTTP;

    /**
     * 测试主机
     */
    public static final String HOST = "";
    public static final String PAY_HOST = "paytest.wonderworld.com";



    public static final String URL_PREFIX_HTTPS = SCHEME_HTTPS + "://" + HOST + ":" + PORT_HTTPS;
    public static final String URL_PREFIX_HTTP = SCHEME_HTTP + "://" + HOST + ":" + PORT_HTTP;
    public static final String URL_PREFIX = URL_PREFIX_HTTP;
}
