package de.l7r7.proto.bundle.magic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class CrazyFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(CrazyFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("############################################");
        log.info("FILTER");
        log.info("############################################");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
