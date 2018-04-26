package com.zhangflg.shop.service.impl;

import com.zhangflg.shop.bean.ArticleType;
import com.zhangflg.shop.repository.ArticleTypeMapper;
import com.zhangflg.shop.service.ShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("shopService")
public class ShopServiceImpl implements ShopService {
    //得到数据访问层对象
    @Resource
    private ArticleTypeMapper articleTypeMapper;

    @Override
    public List<ArticleType> getArticleTypes() {
        return articleTypeMapper.getArticleTypes();
    }
}
