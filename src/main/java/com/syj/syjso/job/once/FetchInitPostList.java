package com.syj.syjso.job.once;

/**
 * @author syj
 * @date 2024/10/23 22:03
 */

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.syj.syjso.model.entity.Post;
import com.syj.syjso.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 获取初始化帖子列表
 */
// todo 取消注释开启任务
//如果取消component的注释，他将会在项目第一次启动的时候调用一次该run方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {
    @Resource
    PostService postService;

    @Override
    public void run(String... args) throws Exception {
        String json = "{\n" +
                "    \"pageSize\": 12,\n" +
                "    \"sortOrder\": \"descend\",\n" +
                "    \"sortField\": \"createTime\",\n" +
                "    \"tags\": [],\n" +
                "    \"current\": 1,\n" +
                "    \"reviewStatus\": 1,\n" +
                "    \"category\": \"文章\",\n" +
                "    \"hiddenContent\": true\n" +
                "}";
        String url = "https://api.codefather.cn/api/post/list/page/vo";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
        System.out.println(result2);
        //获取数据并转化成map
        Map<String, Object> map = JSONUtil.toBean(result2, Map.class);
        Object code = map.get("code");
        System.out.println("code:" + code);
        JSONObject data = (JSONObject) map.get("data");
        System.out.println("data:" + data);
        JSONArray records = (JSONArray)data.get("records");
        ArrayList<Post> posts = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray)tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            posts.add(post);
        }
        System.out.println(posts);
        //数据入库
        boolean flag = postService.saveBatch(posts);
        if(flag){
            System.out.println("数据插入成功");
            log.info("初始化帖子列表成功，条数为"+posts.size());
        }else{
            System.out.println("数据插入失败");
            log.error("初始化帖子列表失败");
        }

    }
}
