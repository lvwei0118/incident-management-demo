package com.incident.management.Redis;


import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;

/**
 * @Description: 在缓存中维护Bloom Filter
 * @Author: lvwei
 * @Date: 2024/12/20
 * @Version: V1.0
 */
public class BloomFilterUtil {
    // 布隆过滤器的预计容量
    private static final int expectedInsertions = 1000000;
    // 布隆过滤器误判率
    private static final double fpp = 0.001;
    private static BloomFilter<String> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), expectedInsertions, fpp);
    /**
     * 向Bloom Filter中添加元素
     */
    public static void add(String key){
        bloomFilter.put(key);
    }
    /**
     * 判断元素是否存在于Bloom Filter中
     */
    public static boolean mightContain(String key){
        return bloomFilter.mightContain(key);
    }
}
