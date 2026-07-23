package com.tl;

import io.micrometer.common.util.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author X
 */
@SpringBootApplication
@EnableCaching
public class TransmissionLineGisApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(TransmissionLineGisApplication.class, args);
//    }

    public static void main(String[] args)throws UnknownHostException
    {
        ConfigurableApplicationContext application = SpringApplication.run(TransmissionLineGisApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        System.out.println("\n----------------------------------------------------------\n\t" +
                "(♥◠‿◠)ﾉﾞ  项目启动成功   ლ(´ڡ`ლ)ﾞ 服务端地址如下： \n\t" +
                "Local: \t\thttp://localhost:" + port + path + "\n\t" +
                "Swagger: \thttp://localhost:" + port + path + "/swagger-ui.html\n\t" +
                "------------------------------------------------------\n\t"+
                "NetWork: \thttp://"+ip+":" + port + path + "\n\t" +
                "\n----------------------------------------------------------\n\t");
    }

}