package io.github.jakarta.configuration;

import io.github.jakarta.view.jsp.JspcRunnable;
import io.github.jakarta.configuration.webpack.WebpackJspTag;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JakartaStartup implements ServletContextListener {

    public static final JakartaProfile PROFILE;

    static {
        final String profile = System.getProperty("profile");
        if ("development".equalsIgnoreCase(profile)) {
            PROFILE = JakartaProfile.DEVELOPMENT;
        } else {
            PROFILE = JakartaProfile.PRODUCTION;
        }
    }

    @Resource
    private ManagedExecutorService mes;

    @Override
    public void contextInitialized(final ServletContextEvent servletContextEvent) {
        this.mes.runAsync(WebpackJspTag.WEBPACK_MANIFEST);

        final Runnable jspcRunnable = new JspcRunnable(
            this.mes, servletContextEvent.getServletContext()
        );
        this.mes.runAsync(jspcRunnable);
    }
}