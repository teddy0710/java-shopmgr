package com.zhangflg.shop.service.impl;

import com.zhangflg.shop.bean.Article;
import com.zhangflg.shop.bean.ArticleType;
import com.zhangflg.shop.bean.User;
import com.zhangflg.shop.repository.ArticleMapper;
import com.zhangflg.shop.repository.ArticleTypeMapper;
import com.zhangflg.shop.repository.UserMapper;
import com.zhangflg.shop.service.ShopService;
import com.zhangflg.shop.utils.Pager;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
    //得到数据访问层对象
    @Resource
    private ArticleTypeMapper articleTypeMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public List<ArticleType> getArticleTypes() {
        return articleTypeMapper.getArticleTypes();
    }

    @Override
    public Map<String, Object> login(String loginName, String passWord) {
        Map<String, Object> results = new HashMap<>();
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(passWord)) {
            //参数为空
            results.put("code", 1);
            results.put("msg", "参数为空");
        } else {
            //参数不为空
            //判断用户名密码是否正确
            User user = userMapper.login(loginName);
            if (user != null) {
                //用户存在
                if (user.getPassword().equals(passWord)) {
                    //登录成功，并保存到Session会话中
                    results.put("code", 0);
                    results.put("msg", user);
                } else {
                    //账号存在但密码错误
                    results.put("code", 2);
                    results.put("msg", "密码错误了");
                }
            } else {
                //账号不存在
                results.put("code", 3);
                results.put("msg", "用户名不存在");
            }
        }
        return results;
    }

    @Override
    public List<ArticleType> loadFirstArticleType() {
        return articleTypeMapper.getFirstArticleType();
    }

    @Override
    public List<Article> searchArticles(String typeCode, String secondType, String title, Pager pager) {
        int count = articleMapper.count(typeCode, secondType, title);
        pager.setTotalCount(count);
        return articleMapper.searchArticles(typeCode, secondType, title, pager);
    }

    @Override
    public List<ArticleType> loadSecondArticleType(String typeCode) {
        return articleTypeMapper.getSecondArticleType(typeCode + "%", typeCode.length() + 4);
    }

    @Override
    public void deleteById(String id) {
        articleMapper.deleteById(id);
    }

    @Override
    public Article getArticleById(String id) {
        return articleMapper.getArticleById(id);
    }

    @Override
    public void updateArticle(Article article) {
        articleMapper.update(article);
    }

    @Override
    public void saveArticle(Article article) {
        article.setCreateDate(new Date());
        articleMapper.save(article);
    }
}
