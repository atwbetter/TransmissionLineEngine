package com.tl.common.constant;

/**
 * 通用常量信息
 *
 * @author ruoyi
 */
public interface Constants {

    /**
     * UTF-8 字符集
     */
    String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    String GBK = "GBK";

    /**
     * www主域
     */
    String WWW = "www.";

    /**
     * http请求
     */
    String HTTP = "http://";

    /**
     * https请求
     */
    String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    String FAIL = "1";

    /**
     * 登录成功
     */
    String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    String LOGOUT = "Logout";

    /**
     * 注册
     */
    String REGISTER = "Register";

    /**
     * 登录失败
     */
    String LOGIN_FAIL = "Error";

    /**
     * 验证码有效期（分钟）
     */
    Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    String TOKEN = "token";

    /**数据物理存储路径**/
    String FILE_PATH = "360url";

    /**数据物理存储路径**/
    String EV_SERVER_URL = "server_url";


    /**数据物理存储路径**/
    String MODEL_FILE_PATH = "model_file_path";


    /** 地球半径 **/
    Integer EARTH_RADIUS = 6371000;

    Double DEGREETOLENGTHP = 111320D;


    /**
     * tif文件路径
     */
    String TIF_PATH = "tif_path";

    /*
     *临时文件路径
     */
    String TEMP_PATH = "temp_path";


    /**
     * 安全距离误差范围
     */
    String SAFE_DISTANCE_RANGE ="safe_distance_range";
}

