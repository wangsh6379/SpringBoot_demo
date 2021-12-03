package com.springboot.zookeeper.controller;

import com.springboot.zookeeper.zk.WatcherApi;
import com.springboot.zookeeper.zk.ZkApi;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 通过zk获取节点上的服务地址
 */
@RestController
@RequestMapping("zk")
public class ZkController {
    private static final Logger logger = LoggerFactory.getLogger(ZkController.class);
    private static final String SERVER_PATH = "/product";

    @Autowired
    private ZkApi zkApi;
    @Autowired
    private ZooKeeper zkClient;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("create")
    public String create() {
        String path = "/creat-test";
        boolean status = zkApi.createNode(path, "测试");

        System.out.println("返回状态" + status);
        String data = zkApi.getData(path, new WatcherApi());
        System.out.println("得到的数据是" + data);
        return "ok";
    }

    @Value("${server.port}")
    private String serverPort;

    /**
     * 随机获取一个服务节点
     *
     * @return
     */
    @RequestMapping(value = "/getPath")
    public String paymentzk() {
        List<String> ips = new LinkedList<>();
        try {
            List<String> childs = zkClient.getChildren(SERVER_PATH, true);
            for (String child : childs) {
                byte[] obj = zkClient.getData(SERVER_PATH + "/" + child, false, null);
                String path = new String(obj, "utf-8");
                ips.add(path);
            }
            if (ips.isEmpty()) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //这里我们随机获取一个ip端口使用
        int index = new Random().nextInt(ips.size());
        return ips.get(index);
    }

    //真正请求
    @RequestMapping(value = "/order/id", method = RequestMethod.GET)
    public String get() {
        //从zookeeper中获取调用的ip
        String path = paymentzk();
        if (path == null) {
            return "对不起，产品暂时停止服务！";
        }
        return restTemplate.getForObject("http://" + path + "/product/id", String.class);
    }
}
