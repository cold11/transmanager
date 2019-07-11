package com.cold.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: ohj
 * @Date: 2018/8/9 14:04
 * @Description:
 */
@Slf4j
public class PropertiesUtils {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    // 已经加载过的Properties文件缓存
    private static final Map<String, PropertiesFileBean> LOADED_PROPERTIES_FILE_CACHE = new HashMap<String, PropertiesFileBean>();

    /**
     * 载入properties文件, 多次载入相同（location相同）文件时会刷新缓存内容. 文件路径使用Spring Resource格式,
     * 文件编码使用UTF-8.
     *
     * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer
     */
    private static Properties loadProperties(String location)
            throws IOException {
        Properties props = new Properties();

        log.info("Loading properties file from:[" + location + "]");

        InputStream is = null;
        try {
            Resource resource = resourceLoader.getResource(location);
            is = resource.getInputStream();
            propertiesPersister.load(props, new InputStreamReader(is,
                    DEFAULT_ENCODING));
            LOADED_PROPERTIES_FILE_CACHE.put(location, new PropertiesFileBean(
                    location, resource.lastModified(), props));
        } catch (IOException ex) {
            log.error("Could not load properties from classpath:["
                    + location + "]: " + ex.getMessage());
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return props;
    }

    private static Properties loadProperty(String propertyFilePath) {
        try {
            return loadProperties(propertyFilePath);
        } catch (IOException e) {
            log.error("load properties file [" + propertyFilePath
                    + "]error:" + e.getMessage());
            return null;
        }
    }

    /**
     * 1.从缓存读取Properties，如果缓存中存在并且将要加载的文件的最后更新时间戳与缓存中的时间戳相同，则直接返回缓存中的Properties<br>
     * 2.否则，才真正地去加载配置文件，并且覆盖缓存中原有内容。<br>
     * 3.如果缓存中没有，执行第二步<br>
     *
     * @param propertyFilePath
     *            加载Properties文件相对于classpath的路径
     * @return
     */
    public static Properties getProperties(String propertyFilePath) {
        Resource resource = resourceLoader.getResource(propertyFilePath);
        Long realLastModified = 0L;
        try {
            realLastModified = resource.lastModified();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        if (LOADED_PROPERTIES_FILE_CACHE.containsKey(propertyFilePath)
                && LOADED_PROPERTIES_FILE_CACHE.get(propertyFilePath)
                .getLastModified().longValue() == realLastModified
                .longValue()) {
            log.info("file [" + propertyFilePath
                    + "] has been loaded ,return from mem.");
            return LOADED_PROPERTIES_FILE_CACHE.get(propertyFilePath)
                    .getProperties();
        }
        return loadProperty(propertyFilePath);
    }

    // 缓存的Properties文件对应的Bean
    private static class PropertiesFileBean {
        private String filePath;
        private Long lastModified;
        private Properties properties;

        public PropertiesFileBean(String filePath, Long lastModified,
                                  Properties properties) {
            super();
            this.filePath = filePath;
            this.lastModified = lastModified;
            this.properties = properties;
        }

        public String getFilePath() {
            return filePath;
        }

        public Properties getProperties() {
            return properties;
        }

        public Long getLastModified() {
            return lastModified;
        }
    }
}
