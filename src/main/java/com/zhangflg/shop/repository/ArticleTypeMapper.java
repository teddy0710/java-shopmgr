package com.zhangflg.shop.repository;

import com.zhangflg.shop.bean.ArticleType;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ArticleTypeMapper 数据访问类
 *
 * @author xlei @qq 251425887 @tel 13352818008
 * @version 1.0
 * @Email dlei0009@163.com
 * @date 2018-04-26 11:54:00
 */
public interface ArticleTypeMapper {

    @Select("select * from ec_article_type")
    List<ArticleType> getArticleTypes();
}