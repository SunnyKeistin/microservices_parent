package com.ibm.springcloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

//登录权限认证过滤器
//@Component
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //default false,if true 使过滤器生效
        return true;
    }

    @Override
    public Object run() {
        //如果请求参数中token != user 不转发给服务，直接返回客户端
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletResponse response = requestContext.getResponse();
        String token = requestContext.getRequest().getParameter("token");
        if(!"user".equals(token)){
            requestContext.setSendZuulResponse(false);
            response.setStatus(401);
            return null;
        }
        return null;
    }
}
