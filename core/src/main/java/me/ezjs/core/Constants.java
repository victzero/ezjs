package me.ezjs.core;

/**
 * 系统常量.
 * Created by zero-mac on 16/6/29.
 */
public class Constants {


    /**
     * 默认每页数据条数.
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    public final static String DATEFORMAT = "yyyy-MM-dd";

    //通过配置文件更新默认参数.
//    static {
//        try {
//            PropertiesConfiguration configuration = new PropertiesConfiguration("/application.properties");
//            DEFAULT_PAGE_SIZE = configuration.getInt("DEFAULT_PAGE_SIZE");
//        } catch (ConfigurationException e) {
//            e.printStackTrace();
    // Do noThing.
//        }
//    }

}
