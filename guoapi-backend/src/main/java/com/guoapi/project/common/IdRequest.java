package com.guoapi.project.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Id传参（只包含 id 字段，方便 Json 参数传递）
 *
 * @author guoapi
 */
@Data
public class IdRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}