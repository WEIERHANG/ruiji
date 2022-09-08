package com.mk.reggie.config.component;

import com.alibaba.fastjson.JSON;
import com.mk.reggie.common.BaseContext;
import com.mk.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检查用户是否完成登录---自定义过滤器
 * /*所有的请求都拦截
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //专门进行路径比对，路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        //获取本次请求的url
        String requestURI= request.getRequestURI();
        log.info("拦截到请求:{}",requestURI);
        //设置放行路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };
        //判断路径是否在放行路径里面，如果是，则不需要处理
        boolean check= check(urls,requestURI);
        //如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}，不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //1-判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee")!=null)
        {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long empId= (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
//            Long id=Thread.currentThread().getId();
//            log.info("当前线程id为{}",id);
            filterChain.doFilter(request,response);
            return;
        }
        //2-判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user")!=null)
        {
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long userId= (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }


        //如果未登录则返回登录结果,通过输出流方式向客户端界面响应数据
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
//        log.info("拦截到请求:{}",request.getRequestURI());
    }


    /**
     * 检查是否需要放行
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls ,String requestURI){
        for(String url:urls){
            Boolean match=PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}

