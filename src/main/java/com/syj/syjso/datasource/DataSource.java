package com.syj.syjso.datasource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
/**
 * @author syj
 * @date 2024/10/26 23:43
 */

/**
 * 统一数据源接口(新接入的数据源必须实现)
 */
public interface DataSource<T> {

    /**
     * 统一搜索接口
     *
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize, HttpServletRequest request);
}
