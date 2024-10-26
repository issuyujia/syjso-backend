package com.syj.syjso.controller;

import com.syj.syjso.common.BaseResponse;
import com.syj.syjso.common.ResultUtils;
import com.syj.syjso.manager.SearchFacade;
import com.syj.syjso.model.dto.search.SearchRequest;
import com.syj.syjso.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子接口
 */
@RestController
@RequestMapping("/search")
@Slf4j
/**
 * 全局聚合搜索接口
 */
public class SearchController {

    @Resource
    private SearchFacade searchFacade;

    /**
     * 聚合搜索接口，查询全部信息
     *
     * @param searchRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }

}
