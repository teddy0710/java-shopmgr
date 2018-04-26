package com.zhangflg.shop.service;

import com.zhangflg.shop.bean.Article;
import com.zhangflg.shop.bean.ArticleType;

import java.util.List;
import java.util.Map;

public interface ShopService {
    List<ArticleType> getArticleTypes();

    Map<String,Object> login(String loginName, String passWord);

    List<ArticleType> loadFirstArticleType();

    List<Article> searchArticles();
}
