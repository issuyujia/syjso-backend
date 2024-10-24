package com.syj.syjso.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.syj.syjso.model.entity.Post;
import com.syj.syjso.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.syj.syjso.model.entity.Picture;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author syj
 * @date 2024/10/23 20:57
 */
@SpringBootTest
public class CrawlerTest {
    @Resource
    PostService postService;


    @Test
    void testFetchPicture() throws IOException {
        String current = "1";
        String url = "https://cn.bing.com/images/search?q=%E5%B0%8F+%E9%BB%91%E5%AD%90&qpvt=%E5%B0%8F%E9%BB%91%E5%AD%90&form=IGRE&cw=1177&ch=787&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            // 取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String, Object> map = JSONUtil.toBean(m, Map.class);
            String murl = (String) map.get("murl");
//            System.out.println(murl);
            // 取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    /**
     * 爬取文章内容
     */
    @Test
    void testFetchPassage() {
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
        JSONArray records = (JSONArray) data.get("records");
        ArrayList<Post> posts = new ArrayList<>();
        for (Object record : records) {
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            posts.add(post);
        }
        System.out.println(posts);
        //数据入库
        boolean flag = postService.saveBatch(posts);
        if (flag) {
            System.out.println("数据插入成功");
        } else {
            System.out.println("数据插入失败");
        }
        Assertions.assertTrue(flag);
    }
}
