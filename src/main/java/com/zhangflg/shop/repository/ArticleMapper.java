package com.zhangflg.shop.repository;

import com.zhangflg.shop.bean.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ArticleMapper 数据访问类
 *
 * @author xlei @qq 251425887 @tel 13352818008
 * @version 1.0
 * @Email dlei0009@163.com
 * @date 2018-04-26 11:54:00
 */
@Repository
public interface ArticleMapper {


    List<Article> searchArticles(@Param("typeCode") String typeCode, @Param("secondType") String secondType, @Param("title") String title);
}