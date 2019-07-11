package com.cold.cache;

import com.cold.entity.TBLanguage;
import com.cold.service.ILanguageService;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @Auther: ohj
 * @Date: 2018/8/8 15:08
 * @Description:
 */
@Service
@Slf4j
public class CacheService {
    @Autowired
    private ILanguageService languageService;
    /**
     * 永久缓存
     */
    @Resource
    private Cache permanentCache;
    /**
     * 初始化永久缓存
     */
    @PostConstruct
    public void initPermanentCache() {
        List<TBLanguage> languages = languageService.getLanguages(null);
        log.info("cache:加载语种{}", JSONUtils.valueToString(languages));
        putPermanentCache("languages", languages);
    }

    /**
     * 添加永久缓存
     * @param key
     * @param value
     */
    public boolean putPermanentCache(final Serializable key, final Object value) {
        try {
            if(null != key && null != value) {
                Element element = new Element(key, value);
                permanentCache.put(element);
                return true;
            }
        } catch (Exception e) {
            log.error("添加缓存失败", e);
            throw new RuntimeException(e);
        }
        return false;
    }

    /**
     * 获取永久缓存
     * @param key
     */
    public Object getPermanentCache(final Serializable key) {
        try {
            if(null != key) {
                Element element = permanentCache.get(key);
                if(null != element) {
                    return element.getObjectValue();
                }
            }
        } catch (Exception e) {
            log.error("获取缓存失败", e);
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 删除永久缓存
     * @param key
     */
    public boolean removePermanentCache(final Serializable key) {
        try {
            if(null != key) {
                permanentCache.remove(key);
                return true;
            }
        } catch (Exception e) {
            log.error("删除缓存失败", e);
            throw new RuntimeException(e);
        }
        return false;
    }
}