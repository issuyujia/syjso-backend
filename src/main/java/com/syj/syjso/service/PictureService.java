package com.syj.syjso.service;

/**
 * @author syj
 * @date 2024/10/25 20:58
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.syj.syjso.model.entity.Picture;

/**
 * 图片接口
 */
public interface PictureService {
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
