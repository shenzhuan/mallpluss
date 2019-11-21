package com.mei.zhuang.vo.query;


import lombok.Data;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author arvato team
 * @since 2019-04-15
 */
@Data
public class QueryMemberParma {

    private static final long serialVersionUID = 1L;


    private Integer buyCount;

    private String mobile;


    private String sex;


    //程序渠道
    private String comeFrom;


}
