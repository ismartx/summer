package org.smartx.summer.config;

import org.smartx.summer.filter.LoggingFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by binglin on 2016/10/27.
 *
 * 代替web.xml文件
 *
 * @author binglin
 */
public class SpringMvcWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("contextConfigLocation", "classpath:spring-application.xml");

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);

        DispatcherServlet dispatcherServlet = new DispatcherServlet(webContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");


        servletContext.addListener(new ContextLoaderListener());
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("CharacterEncodingFilter", CharacterEncodingFilter.class);
        characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
        characterEncodingFilter.setInitParameter("encoding", "UTF-8");
        characterEncodingFilter.setInitParameter("forceEncoding", "true");

        FilterRegistration.Dynamic loggingFilter = servletContext.addFilter("loggingFilter", LoggingFilter.class);
        loggingFilter.addMappingForUrlPatterns(null, false, "/*");
        loggingFilter.setInitParameter("excludeUrl", "/files/*;/swagger*;/webjars/*;/favicon.ico");

        FilterRegistration.Dynamic jwtTokenAuthFilter = servletContext.addFilter("jwtTokenAuthFilter", DelegatingFilterProxy.class);
        jwtTokenAuthFilter.addMappingForUrlPatterns(null, false, "/api/*");
        //excludeUrl 到 application.xml 中配置
    }
}
