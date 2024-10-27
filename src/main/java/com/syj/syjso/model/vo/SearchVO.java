package com.syj.syjso.model.vo;

import cn.hutool.json.JSONUtil;
import com.syj.syjso.model.entity.Picture;
import com.syj.syjso.model.entity.Post;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 聚合搜索视图
 */
@Data
public class SearchVO implements Serializable {

    private List<UserVO> userList;

    private List<PostVO> postList;

    private List<Picture> pictureList;

    private List<?> dataList;

    private static final long serialVersionUID = 1L;
}
