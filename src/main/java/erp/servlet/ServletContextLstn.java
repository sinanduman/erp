package erp.servlet;

import erp.entity.Calisan;
import erp.util.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.List;

@WebListener
public class ServletContextLstn implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ServletContextLstn.class);

    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        @SuppressWarnings("unchecked")
        List<Calisan> calisan = (ArrayList<Calisan>) context.getAttribute("calisan");
        if (calisan == null) {
            logger.info("No calisan mevcut");
            calisan = new ArrayList<>();
            context.setAttribute(Fields.CALISAN, calisan);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
