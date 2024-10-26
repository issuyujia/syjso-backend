package com.syj.syjso.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.annotation.AuthCheck;
import com.syj.syjso.common.BaseResponse;
import com.syj.syjso.common.DeleteRequest;
import com.syj.syjso.common.ErrorCode;
import com.syj.syjso.common.ResultUtils;
import com.syj.syjso.constant.UserConstant;
import com.syj.syjso.exception.BusinessException;
import com.syj.syjso.exception.ThrowUtils;
import com.syj.syjso.model.dto.picture.PictureQueryRequest;
import com.syj.syjso.model.dto.post.PostAddRequest;
import com.syj.syjso.model.dto.post.PostEditRequest;
import com.syj.syjso.model.dto.post.PostQueryRequest;
import com.syj.syjso.model.dto.post.PostUpdateRequest;
import com.syj.syjso.model.entity.Picture;
import com.syj.syjso.model.entity.Post;
import com.syj.syjso.model.entity.User;
import com.syj.syjso.model.vo.PostVO;
import com.syj.syjso.service.PictureService;
import com.syj.syjso.service.PostService;
import com.syj.syjso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 帖子接口
 *
 * @author <a href="https://github.com/lisyj">程序员鱼皮</a>
 * @from <a href="https://syj.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/picture")
@Slf4j
/**
 * 聚合搜索图片接口
 */
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                           HttpServletRequest request) {
        long current = pictureQueryRequest.getCurrent();
        long size = pictureQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, size);
        return ResultUtils.success(picturePage);
    }


}
