package edu.dsiedlarz.ParkingMeterAssistant.filter;

import javax.ejb.Singleton;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by private on 11.06.2017.
 */
@WebFilter(filterName = "BrowserFilter", urlPatterns = {"/app/*"})
public class BrowserFilter implements Filter
{

    @Override
    public void init(FilterConfig cfg) throws ServletException
    {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException
    {
        String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");
        System.out.println("userAgent " + userAgent );
            if (userAgent.contains("Chrome"))
            {
                chain.doFilter(req, resp);
                return;
            }
        // Unsupported browser
        ((HttpServletResponse) resp).getWriter().write("Not allowed browser");
    }

    @Override
    public void destroy()
    {
    }
}
