package com.zhangflg.shop.action;

import com.zhangflg.shop.bean.User;
import com.zhangflg.shop.service.ShopService;
import com.zhangflg.shop.utils.Constants;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        String method = req.getParameter("method");
        switch (method) {
            case "login":
                login();
                break;
            case "getjsp":
                //跳转到登录界面
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
                break;
        }


    }

    private void login() {
        //获取从登录界面获取的参数
        String loginName = request.getParameter("loginName");
        String passWord = request.getParameter("passWord");
        Map<String, Object> results = shopService.login(loginName, passWord);
        switch ((int) results.get("code")) {
            case 0:
                //登录成功
                User user = (User) results.get("msg");
                request.setAttribute(Constants.USER_SESSION, user);
                //测试：跳转主页面
                //request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);

                //跳转到获取主页面数据的servlet
                try {
                    String url = request.getContextPath() + "/list";
                    System.out.println("LoginServlet》response.sendRedirect(url)：" + url);
                    response.sendRedirect(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
            case 2:
            case 3:
                request.setAttribute(Constants.MSG, results.get("msg").toString());
                break;
        }
    }
}
