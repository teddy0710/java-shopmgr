package com.zhangflg.shop.repository;

import com.zhangflg.shop.bean.ArticleType;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ArticleTypeMapper 数据访问类
 *
 * @author xlei @qq 251425887 @tel 13352818008
 * @version 1.0
 * @Email dlei0009@163.com
 * @date 2018-04-26 11:54:00
 */
@Repository
public interface ArticleTypeMapper {

    @Select("select * from ec_article_type where code=#{typeCode}")
    ArticleType getTypeByCode(@Param("typeCode") String typeCode);

    @Select("select * from ec_article_type")
    List<ArticleType> getArticleTypes();

    @Select("select * from ec_article_type where length(code)=4")
    List<ArticleType> getFirstArticleType();

    @Select("select * from ec_article_type where code like #{typeCode} and length(code)=#{length}")
    List<ArticleType> getSecondArticleType(@Param("typeCode") String typeCode, @Param("length") int length);
}