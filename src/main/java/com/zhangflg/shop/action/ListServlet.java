package com.zhangflg.shop.action;

import com.zhangflg.shop.bean.Article;
import com.zhangflg.shop.bean.ArticleType;
import com.zhangflg.shop.service.ShopService;
import com.zhangflg.shop.utils.Pager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private ShopService shopService;
    private HttpServletRequest req;
    private HttpServletResponse resp;

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
        req.setCharacterEncoding("UTF-8");
        this.req = req;
        this.resp = resp;
        String method = req.getParameter("method");
        try {
            switch (method) {
                case "getAll":
                    getAll();
                    break;
                case "deleteById":
                    deleteById();
                    break;

            }
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteById() throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            shopService.deleteById(id);
            req.setAttribute("tip", "删除成功");
        } catch (Exception e) {
            req.setAttribute("tip", "删除失败");
            e.printStackTrace();
        }
        req.getRequestDispatcher("/list?method=getAll").forward(req, resp);

    }

    private void getAll() throws ServletException, IOException {
        //分页查询
        Pager pager = new Pager();
        String pageIndex = req.getParameter("pageIndex");
        if (!StringUtils.isEmpty(pageIndex)) {
            pager.setPageIndex(Integer.parseInt(pageIndex));
        }
        //接受一级类型编号 查询
        String typeCode = req.getParameter("typeCode");
        //接受er级类型编号 查询
        String secondType = req.getParameter("secondType");

        //接收商品标题
        String title = req.getParameter("title");
        req.setAttribute("secondType", secondType);
        req.setAttribute("title", title);
        //根据一级类型查询二级类型
        if (!StringUtils.isEmpty(typeCode)) {
            List<ArticleType> secondArticleTypes = shopService.loadSecondArticleType(typeCode);
            req.setAttribute("typeCode", typeCode);
            req.setAttribute("secondTypes", secondArticleTypes);
        }

        //查询所有一级类型数据
        List<ArticleType> firstArticleTypes = shopService.loadFirstArticleType();
        //查询所有的商品信息
        List<Article> articles = shopService.searchArticles(typeCode, secondType, title, pager);

        req.setAttribute("firstArticleTypes", firstArticleTypes);
        req.setAttribute("articles", articles);
        req.setAttribute("pager", pager);

        //查询完成，跳转首页

        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);


    }

}
