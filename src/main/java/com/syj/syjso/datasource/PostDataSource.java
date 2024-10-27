package com.syj.syjso.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.model.dto.post.PostQueryRequest;
import com.syj.syjso.model.entity.Post;
import com.syj.syjso.model.vo.PostVO;
import com.syj.syjso.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子服务实现
 * >
 */
@Service
@Slf4j
public class PostDataSource implements DataSource<PostVO> {

    @Resource
    private PostService postService;

    @Override
    public Page<PostVO> doSearch(String searchText, long pageNum, long pageSize) {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        postQueryRequest.setSearchText(searchText);
        postQueryRequest.setCurrent((int) pageNum);
        postQueryRequest.setPageSize((int) pageSize);
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
//        Page<Post> postPage = postService.searchFromEs(postQueryRequest);
        Page<PostVO> postPage = postService.listPostVOByPage(postQueryRequest, request);
        return postPage;
    }
}




