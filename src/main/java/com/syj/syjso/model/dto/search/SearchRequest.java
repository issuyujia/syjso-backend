package com.syj.syjso.model.dto.search;

/**
 * @author syj
 * @date 2024/10/24 21:50
 */

import com.syj.syjso.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 聚合搜索实体类
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 搜索类型
     */
    private String type;

    private static final long serialVersionUID = 1L;
}
