package come.leyou.config.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: 98050
 * @Time: 2018-11-30 20:59
 * @Feature:
 */
public class MyFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(MyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("过滤器启动");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String url = httpServletRequest.getRequestURI();

        //过滤/actuator/bus-refresh请求
        String suffix = "/bus-refresh";
        if (!url.endsWith(suffix)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        CustometRequestWrapper requestWrapper = new CustometRequestWrapper(httpServletRequest);
        filterChain.doFilter(requestWrapper,servletResponse);

    }

    @Override
    public void destroy() {
        logger.info("过滤器销毁");
    }
}
