package com.zhangflg.shop.action;

import com.zhangflg.shop.bean.Article;
import com.zhangflg.shop.bean.ArticleType;
import com.zhangflg.shop.service.ShopService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class ListServlet extends HttpServlet {
    private ShopService shopService;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Override
    public void init() throws ServletException {
        super.init();
        // 获取spring的容器。然后从容器中得到业务层对象
        ServletContext servletContext = this.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        shopService = (ShopService) context.getBean("shopService");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.request = req;
        this.response = resp;
        //查询所有一级类型数据
        List<ArticleType> firstArticleTypes = shopService.loadFirstArticleType();
        //查询所有的商品信息
        List<Article> articles = shopService.searchArticles();

        req.setAttribute("firstArticleTypes", firstArticleTypes);
        req.setAttribute("articles", articles);
    }

}
