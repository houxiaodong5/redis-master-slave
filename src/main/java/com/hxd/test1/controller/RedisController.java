package com.hxd.test1.controller;

import com.hxd.test1.entity.User;
import com.hxd.test1.util.ByteArrayUtils;
import com.hxd.test1.util.YjsonUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Resource
    @Qualifier("masterJedisPool")
    private JedisPool masterJedisPool;

    @Resource
    @Qualifier("slaveJedisPool")
    private JedisPool slaveJedisPool;

    /**         测试时:提前向从库中写入key-value:hxd-123
     *          slave:可以获取到hxd-123
     * */
    @GetMapping()
    public ResponseEntity<List<Object>> queryAll(){
        Jedis jedis=null;
        try {
            jedis = slaveJedisPool.getResource();
            System.out.println(slaveJedisPool);
            List<Object> list = new ArrayList<>();
            for(int a=0;a<5;a++){
                byte[] key = jedis.randomBinaryKey();
                System.out.println(key);
                if (key!=null)
                list.add(ByteArrayUtils.bytesToObject(jedis.get(key)));

            }
            System.out.println(jedis.get("hxd"));
            list.add(jedis.get("hxd"));

            jedis.close();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }

    }

    /**         测试时:提前向从库中写入key-value:hxd-123
     *          master:获取不到hxd-123
     * */
    @GetMapping("/1")
    public ResponseEntity<List<Object>> query(){
        Jedis jedis=null;
        try {
            jedis = masterJedisPool.getResource();
            List<Object> list = new ArrayList<>();
            //之前数据使用byte[] 存储，不好获取byte[]类型key,只好通过随机去拿
            for(int a=0;a<5;a++){
                byte[] key = jedis.randomBinaryKey();
                System.out.println(key);
                if (key!=null)
                    list.add(ByteArrayUtils.bytesToObject(jedis.get(key)));

            }
            list.add(jedis.get("hxd"));
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        } finally {
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    /**
     *      实现master写数据，slave读数据
     * */
    @PostMapping("/test")
    public ResponseEntity<User> insert(@RequestBody User user){
        Jedis master = null;
        Jedis slave = null;
        User fromJson = null;
        try {
            master = masterJedisPool.getResource();
            master.set("test", YjsonUtil.toJson(user));
            slave = slaveJedisPool.getResource();
            String data = slave.get("test");
            fromJson = YjsonUtil.fromJson(data, User.class);
            System.out.println(fromJson);
            return ResponseEntity.ok(fromJson);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }finally {
            if(master!=null){
                master.close();
            }
            if(slave!=null){
                slave.close();
            }
        }

    }
}
