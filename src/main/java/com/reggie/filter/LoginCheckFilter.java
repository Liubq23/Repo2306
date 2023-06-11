package com.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ResizableByteArrayOutputStream;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 刘秉奇
 * @version 1.0
 */

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")//拦截所有请求
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //拦截逻辑

        //1、获取本次请求的URI
        String uri = request.getRequestURI();
        //定义 不 需要被拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",//静态资源，页面，图片等
                "/front/**",//页面上的数据会通过动态请求
                //移动端登录用户放行
                "/user/login",
                "/user/sendMsg"
        };

        //2、判断本次请求是否需要处理
        boolean check = check(urls, uri);

        //3、如果不需要处理，则直接放行
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }

        //4、判断登录状态，如果已登录，则直接放行
        //我们当初存的session是employee，所以这里就拿它判断
        if (request.getSession().getAttribute("employee") != null) {

            log.info("{}用户已登录", request.getSession().getAttribute("employee"));
            //设置用户id，在元数据类MyMetaObjectHandler里使用
            long empId = (long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //判断用户是否登录
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }

        //5、如果未登录则返回未登录结果
        //输出流把数据写回去，交给客户端，js里判断res.data.msg === 'NOTLOGIN'，则返回登录页面
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;

     /*   //测试
        log.info("拦截到请求：{}", request.getRequestURI());
        //放行
        filterChain.doFilter(request, response);
*/
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match)
                return true;
        }
        return false;
    }
}
