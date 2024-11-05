package com.syj.syjso.datasource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 视频数据源
 *
 */
@Component
public class VideoDataSource implements DataSource<Object> {

    @Override
    public Page<Object> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request) {
        return null;
    }
}
