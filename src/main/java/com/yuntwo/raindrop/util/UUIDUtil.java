package com.yuntwo.raindrop.util;

import org.n3r.idworker.Sid;

import java.util.UUID;


/**
 * 产生随机序列
 */
public class UUIDUtil {
    /**
     * 用来生成一个唯一的、持久的标识符，这样可以用来在数据库或其他数据存储中唯一地标识一个实体或记录
     * 在这个项目中用作用户登录的token
     * @return 去掉了连字符、所有字符都是大写的 UUID 字符串
     */
    public static String getId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }


    public static void main(String[] args) {
        System.out.println(Sid.next());
        System.out.println(getId());
    }
}
