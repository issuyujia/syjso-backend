package com.syj.syjso.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.common.ErrorCode;
import com.syj.syjso.common.ResultUtils;
import com.syj.syjso.exception.BusinessException;
import com.syj.syjso.exception.ThrowUtils;
import com.syj.syjso.model.dto.post.PostQueryRequest;
import com.syj.syjso.model.dto.search.SearchRequest;
import com.syj.syjso.model.dto.user.UserQueryRequest;
import com.syj.syjso.model.entity.Picture;
import com.syj.syjso.model.enums.SearchTypeEnum;
import com.syj.syjso.model.vo.PostVO;
import com.syj.syjso.model.vo.SearchVO;
import com.syj.syjso.model.vo.UserVO;
import com.syj.syjso.service.PictureService;
import com.syj.syjso.service.PostService;
import com.syj.syjso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 搜索门面
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Component
@Slf4j
public class SearchFacade {

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        String type = searchRequest.getType();
        ThrowUtils.throwIf(StringUtils.isBlank(type), ErrorCode.PARAMS_ERROR);
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);
        //如果请求类型为空，则获取全部数据
        if (searchTypeEnum == null) {
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
                return searchVO;
            } catch (Exception e) {
                log.error("[search.all] 查询异常", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "查询异常");
            }
        } else {//根据请求类型获取指定数据
            SearchVO searchVO = new SearchVO();
            switch (searchTypeEnum) {
                case POST:
                    //帖子查询
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case USER:
                    //用户查询
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVOByPage(new UserQueryRequest());
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case PICTURE:
                    //图片查询
                    Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                default:
            }
            return searchVO;
        }
    }
}
