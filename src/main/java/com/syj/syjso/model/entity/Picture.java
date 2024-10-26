package com.syj.syjso.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author syj
 * @date 2024/10/24 21:41
 */
@Data
public class Picture implements Serializable {
    private static final long serialVersionUID = 1L;

    private String url;

    private String title;

}
