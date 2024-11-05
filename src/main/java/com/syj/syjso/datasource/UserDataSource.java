package com.syj.syjso.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.model.dto.user.UserQueryRequest;
import com.syj.syjso.model.vo.UserVO;
import com.syj.syjso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户服务实现
 *
 */
@Service
@Slf4j
public class UserDataSource implements DataSource<UserVO> {

    @Resource
    private UserService userService;

    @Override
    public Page<UserVO> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request) {
        UserQueryRequest userQueryRequest = new UserQueryRequest();
        userQueryRequest.setUserName(searchText);
        userQueryRequest.setCurrent((int) pageNum);
        userQueryRequest.setPageSize((int) pageSize);
        Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
        return userVOPage;
    }
}
