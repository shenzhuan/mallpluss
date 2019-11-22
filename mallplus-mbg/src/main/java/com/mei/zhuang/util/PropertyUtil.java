/**
 * Project Name:resources
 * File Name:PropertyUtil.java
 * Package Name:utils
 * Date:2017年8月15日下午3:18:05
 * Copyright (c) 2017, China Link Communications LTD All Rights Reserved.
 */


package com.mei.zhuang.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

/**
 * ClassName: PropertyUtil <br/>
 * Date: 2017年11月20日 下午3:18:05 <br/>
 * Description:
 *
 * @author zhux
 * @version
 * @see
 */
@Slf4j
public class PropertyUtil {

    public static String get(String proKey) {
        return loadProp("common.properties").getProperty(proKey);
    }

    public static Properties loadProp(String file) {
        Properties prop = new Properties();
        InputStream in = null;
        Reader reader = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream(file);
            reader = new InputStreamReader(in, "utf-8");
            prop.load(reader);
            return prop;
        } catch (IOException e) {
            log.error("", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
        return null;
    }

}

