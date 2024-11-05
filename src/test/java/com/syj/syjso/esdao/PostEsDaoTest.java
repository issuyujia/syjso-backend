package com.syj.syjso.esdao;

/**
 * @author syj
 * @date 2024/10/27 20:11
 */

import com.syj.syjso.model.dto.post.PostEsDTO;
import com.syj.syjso.model.dto.post.PostQueryRequest;
import com.syj.syjso.model.entity.Post;
import com.syj.syjso.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 帖子ES 操作测试
 */
@SpringBootTest
public class PostEsDaoTest {
    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostService postService;

    @Test
    void test() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
//        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
//        System.out.println(postPage);
    }

    /**
     * 增
     */
    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(1846158022573850626L);
        postEsDTO.setTitle("张飞大王");
        postEsDTO.setContent("张飞大王所向披靡，一往无前，世界第一!!!!");
        postEsDTO.setTags(Arrays.asList("java", "python"));
        postEsDTO.setUserId(1846092853713907714L);
        postEsDTO.setCreateTime(new Date());
        postEsDTO.setUpdateTime(new Date());
        postEsDTO.setIsDelete(0);
        postEsDao.save(postEsDTO);
        System.out.println(postEsDTO.getId());
    }

    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDTO> PostPage = postEsDao.findAll(PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDTO> records = PostPage.getContent();
        Optional<PostEsDTO> byId = postEsDao.findById(1L);
        System.out.println(byId);
        System.out.println("----------------");
        System.out.println(records);
    }

    @Test
    void testFindByTitle() {
        List<PostEsDTO> list = postEsDao.findByTitle("张飞大王");
        System.out.println(list );
    }
}
