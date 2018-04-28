package com.zhangflg.shop.action;

import com.zhangflg.shop.bean.Article;
import com.zhangflg.shop.bean.ArticleType;
import com.zhangflg.shop.service.ShopService;
import com.zhangflg.shop.utils.Pager;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@MultipartConfig // 申明这个Servlet是要接收大文件对象的
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
                case "preArticle":
                    preArticle();
                    break;
                case "showUpdate":
                    showUpdate();
                    break;
                case "updateArticle":
                    updateArticle();
                    break;

                case "addArticle":
                    addArticle();
                    break;

            }
        } catch (ServletException | IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private void addArticle() throws ParseException {
// 接收界面提交的参数
        // 获取请求参数 ----普通表单元素
        String code = req.getParameter("code");
        String title = req.getParameter("titleStr");
        String supplier = req.getParameter("supplier");
        String locality = req.getParameter("locality");
        String putawayDate = req.getParameter("putawayDate");
        String price = req.getParameter("price");
        String storage = req.getParameter("storage");
        String description = req.getParameter("description");
        String picUrl = req.getParameter("picUrl"); // 物品旧封面
        // 定义一个商品对象封装界面提交的参数
        Article article = new Article();

        // 接收用户可能上传的封面
        String newUrl = receiveImage();
        picUrl = newUrl != null ? newUrl : picUrl;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        article.setPutawayDate(sdf.parse(putawayDate));
        article.setImage(picUrl);
        ArticleType type = new ArticleType();
        type.setCode(code);
        article.setArticleType(type);
        article.setTitle(title);
        article.setSupplier(supplier);
        article.setLocality(locality);
        article.setPrice(Double.parseDouble(price));
        article.setStorage(Integer.parseInt(storage));
        article.setDescription(description);
        shopService.saveArticle(article);
        req.setAttribute("tip", "添加商品成功");
        try {
            getAll();
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }


    private String receiveImage() {
        try {
            // 如果用户上传了这里代码是不会出现异常 了
            // 如果没有上传这里出现异常
            Part part = req.getPart("image");
            // 保存到项目的路径中去
            String sysPath = req.getSession().getServletContext().getRealPath("/resources/images/article");
            // 定义一个新的图片名称
            String fileName = UUID.randomUUID().toString();
            //  提取图片的类型
            // 上传文件的内容性质
            String contentDispostion = part.getHeader("content-disposition");
            // 获取上传文件的后缀名
            String suffix = contentDispostion.substring(contentDispostion.lastIndexOf("."), contentDispostion.length() - 1);
            fileName += suffix;
            // 把图片保存到路径中去
            part.write(sysPath + "/" + fileName);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateArticle() {
// 接收界面提交的参数
        // 获取请求参数 ----普通表单元素
        String code = req.getParameter("code");
        String title = req.getParameter("titleStr");
        String supplier = req.getParameter("supplier");
        String locality = req.getParameter("locality");
        String price = req.getParameter("price");
        String storage = req.getParameter("storage");
        String description = req.getParameter("description");
        String id = req.getParameter("id"); // 物品编号
        String picUrl = req.getParameter("picUrl"); // 物品旧封面
        // 定义一个商品对象封装界面提交的参数
        Article article = new Article();

        // 接收用户可能上传的封面
        String newUrl = receiveImage();
        picUrl = newUrl != null ? newUrl : picUrl;

        article.setId(Integer.valueOf(id));
        article.setImage(picUrl);
        ArticleType type = new ArticleType();
        type.setCode(code);
        article.setArticleType(type);
        article.setTitle(title);
        article.setSupplier(supplier);
        article.setLocality(locality);
        article.setPrice(Double.parseDouble(price));
        article.setStorage(Integer.parseInt(storage));
        article.setDescription(description);
        shopService.updateArticle(article);
        req.setAttribute("tip", "修改商品成功");
        showUpdate();
    }

    private void showUpdate() {
        String id = req.getParameter("id");
        Article article = shopService.getArticleById(id);
        List<ArticleType> types = shopService.getArticleTypes();
        req.setAttribute("article", article);
        req.setAttribute("types", types);
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/updateArticle.jsp").forward(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void preArticle() {
        String id = req.getParameter("id");
        Article article = shopService.getArticleById(id);
        req.setAttribute("article", article);
        try {
            req.getRequestDispatcher("/WEB-INF/jsp/preArticle.jsp").forward(req, resp);
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
        req.setAttribute("articleTypes", shopService.getArticleTypes());
        req.setAttribute("articles", articles);
        req.setAttribute("pager", pager);

        //查询完成，跳转首页

        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);


    }

}
