package com.archnotes.raindy.pcc.web.services;

import com.archnotes.raindy.pcc.unit.api.UnitService;
import com.archnotes.raindy.pcc.web.AppProperties;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangyouce on 2016/12/26.
 */

@Service
public class DispatchService implements UnitService.Iface {

    private final static Logger logger = Logger.getLogger(DispatchService.class);


    private Map<String, LikeService> serviceMap;


    @PreDestroy
    public void destory() {
        System.out.println("DispatchService destory");
        for (Map.Entry<String, LikeService> map: serviceMap.entrySet()) {
            map.getValue().close();
        }
    }

    @Autowired
    public DispatchService(AppProperties appProperties) {
        serviceMap = new HashMap<>();

        logger.info(appProperties);
        serviceMap.put("1", new LikeService(appProperties.rpcThriftPath + "pcc" + "/", appProperties.rpcZookeeperNodes));
//        serviceMap.put("2", new LikeService(appProperties.rpcThriftPath + "node2", appProperties.rpcZookeeperNodes));
//        serviceMap.put("3", new LikeService(appProperties.rpcThriftPath + "node3", appProperties.rpcZookeeperNodes));
    }

    private String[] splitId(String id) {
        String[] arr = id.split("_");
        if (arr.length != 2) {
            return null;
        } else {
            return arr;
        }
    }

    private LikeService getLikeService(String targetId) {
        String[] arr = splitId(targetId);
        if (arr != null) {
            return serviceMap.get(arr[0]);
        }
        return null;
    }

    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getNodeId(String hash) {
        char id = hash.toCharArray()[0];

//        if (id < 67) return "1";
//        if (id < 79) return "2";
//
//        return "3";

        return "1";

    }


    @Override
    public Boolean addUser(String name) {
        String hash = MD5(name);
        String nodeId = getNodeId(hash);
        LikeService likeService = serviceMap.get(nodeId);
        if (likeService != null) {
            return likeService.addUser(name);
        }
        return false;
    }

    @Override
    public String getUser(String userId) {
        LikeService likeService = getLikeService(userId);
        if (likeService != null) {
            return likeService.getUser(userId.split("_")[1]);
        }
        return "";
    }

    @Override
    public Boolean followUser(String sourceUid, String followUid) {
        LikeService likeService = getLikeService(sourceUid);
        if (likeService != null) {
            return likeService.followUser(sourceUid.split("_")[1], followUid);
        }
        return false;
    }

    @Override
    public Boolean like(String targetId, String ownId, String userId) {
        LikeService likeService = getLikeService(targetId);
        if (likeService != null) {
            return likeService.like(targetId.split("_")[1], ownId.split("_")[1], userId);
        }
        return false;
    }

    @Override
    public Set<String> getLikes(String targetId) {
        LikeService likeService = getLikeService(targetId);
        if (likeService != null) {
            return likeService.getLikes(targetId.split("_")[1]);
        }
        return null;
    }

    @Override
    public Long getLikesCount(String targetId) {
        LikeService likeService = getLikeService(targetId);
        if (likeService != null) {
            return likeService.getLikesCount(targetId.split("_")[1]);
        }
        return null;
    }

    @Override
    public Boolean isLike(String targetId, String userId) {
        LikeService likeService = getLikeService(targetId);
        if (likeService != null) {
            return likeService.isLike(targetId.split("_")[1], userId);
        }
        return null;
    }
}
