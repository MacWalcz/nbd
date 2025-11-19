package org.nbd.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import java.util.Properties;
import java.io.InputStream;

public class RedisConfig {
    private static JedisPool pool;

    static {
        try (InputStream in = RedisConfig.class.getClassLoader().getResourceAsStream("redis.properties")) {
            Properties props = new Properties();
            props.load(in);
            String host = props.getProperty("redis.host", "localhost");
            int port = Integer.parseInt(props.getProperty("redis.port", "6379"));
            pool = new JedisPool(host, port);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load Redis config", e);
        }
    }

    public static Jedis getConnection() {
        return pool.getResource();
    }

    public static JedisPool getPool() {
        return pool;
    }
}
