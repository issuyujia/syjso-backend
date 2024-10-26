package com.syj.syjso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.common.BaseResponse;
import com.syj.syjso.common.ErrorCode;
import com.syj.syjso.common.ResultUtils;
import com.syj.syjso.exception.BusinessException;
import com.syj.syjso.exception.ThrowUtils;
import com.syj.syjso.model.dto.picture.PictureQueryRequest;
import com.syj.syjso.model.dto.post.PostQueryRequest;
import com.syj.syjso.model.dto.search.SearchRequest;
import com.syj.syjso.model.dto.user.UserQueryRequest;
import com.syj.syjso.model.entity.Picture;
import com.syj.syjso.model.vo.PostVO;
import com.syj.syjso.model.vo.SearchVO;
import com.syj.syjso.model.vo.UserVO;
import com.syj.syjso.service.PictureService;
import com.syj.syjso.service.PostService;
import com.syj.syjso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 帖子接口
 *
 */
@RestController
@RequestMapping("/search")
@Slf4j
/**
 * 全局聚合搜索接口
 */
public class SearchController {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    /**
     * 聚合搜索接口，查询全部信息
     *
     * @param searchRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        //实现并发功能

        CompletableFuture<Page<UserVO>> userCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //用户查询
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(new UserQueryRequest());
            return userVOPage;
        });

        CompletableFuture<Page<Picture>> pictureCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //图片查询
            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
            return picturePage;
        });


        CompletableFuture<Page<PostVO>> postCompletableFuture = CompletableFuture.supplyAsync(() -> {
            //帖子查询
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
            return postVOPage;
        });

        CompletableFuture.allOf(userCompletableFuture, postCompletableFuture, pictureCompletableFuture).join();

        //获取数据
        try {
            Page<UserVO> userVOPage = userCompletableFuture.get();
            Page<PostVO> postVOPage = postCompletableFuture.get();
            Page<Picture> picturePage = pictureCompletableFuture.get();
            SearchVO searchVO = new SearchVO();
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setPictureList(picturePage.getRecords());
            return ResultUtils.success(searchVO);
        } catch (Exception e) {
            log.error("[search.all] 查询异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
        }
//        //图片查询
//        Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
//        //帖子查询
//        PostQueryRequest postQueryRequest = new PostQueryRequest();
//        postQueryRequest.setSearchText(searchText);
//        Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
//        //用户查询
//        UserQueryRequest userQueryRequest = new UserQueryRequest();
//        userQueryRequest.setUserName(searchText);
//        Page<UserVO> userVOPage = userService.listUserVOByPage(new UserQueryRequest());
    }

}
