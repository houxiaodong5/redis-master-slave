package com.hxd.test1.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig extends CachingConfigurerSupport {
    private Logger logger = LoggerFactory.getLogger(JedisConfig.class);

    /**
     * SpringSession  需要注意的就是redis需要2.8以上版本，然后开启事件通知，在redis配置文件里面加上
     * notify-keyspace-events Ex
     * Keyspace notifications功能默认是关闭的（默认地，Keyspace 时间通知功能是禁用的，因为它或多或少会使用一些CPU的资源）。
     * 或是使用如下命令：
     * redis-cli config set notify-keyspace-events Egx
     * 如果你的Redis不是你自己维护的，比如你是使用阿里云的Redis数据库，你不能够更改它的配置，那么可以使用如下方法：在applicationContext.xml中配置
     * <util:constant static-field="org.springframework.session.data.redis.config.ConfigureRedisAction.NO_OP"/>
     * @return
     */

    @Value("${spring.redis.master.host}")
    private String masterHost;

    @Value("${spring.redis.master.port}")
    private int masterPort;

    @Value("${spring.redis.master.timeout}")
    private int masterTimeout;

    @Value("${spring.redis.master.pool.max-active}")
    private int masterMaxActive;

    @Value("${spring.redis.master.pool.max-idle}")
    private int masterMaxIdle;

    @Value("${spring.redis.master.pool.min-idle}")
    private int masterMinIdle;

    @Value("${spring.redis.master.pool.max-wait}")
    private long masterMaxWaitMillis;


    @Value("${spring.redis.slave.host}")
    private String slaveHost;

    @Value("${spring.redis.slave.port}")
    private int slavePort;

    @Value("${spring.redis.slave.timeout}")
    private int slaveTimeout;

    @Value("${spring.redis.slave.pool.max-active}")
    private int slaveMaxActive;

    @Value("${spring.redis.slave.pool.max-idle}")
    private int slaveMaxIdle;

    @Value("${spring.redis.slave.pool.min-idle}")
    private int slaveMinIdle;

    @Value("${spring.redis.slave.pool.max-wait}")
    private long slaveMaxWaitMillis;


    @Qualifier("masterJedisPool")
    @Primary
    @Bean
    public JedisPool redisMasterPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig ();
        jedisPoolConfig.setMaxIdle(masterMaxIdle);
        jedisPoolConfig.setMaxWaitMillis(masterMaxWaitMillis);
        jedisPoolConfig.setMaxTotal(masterMaxActive);
        jedisPoolConfig.setMinIdle(masterMinIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,masterHost,masterPort,masterTimeout,null);

        logger.info("JedisPool注入成功！");
        logger.info("redis地址：" + masterHost + ":" + masterPort);
        return  jedisPool;
    }

    @Qualifier("slaveJedisPool")
    @Bean
    public JedisPool redisSlavePoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig ();
        jedisPoolConfig.setMaxIdle(slaveMaxIdle);
        jedisPoolConfig.setMaxWaitMillis(slaveMaxWaitMillis);
        jedisPoolConfig.setMaxTotal(slaveMaxActive);
        jedisPoolConfig.setMinIdle(slaveMinIdle);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,slaveHost,slavePort,slaveTimeout,null);

        logger.info("JedisPool注入成功！");
        logger.info("redis地址：" + slaveHost + ":" + slavePort);
        return  jedisPool;
    }

}