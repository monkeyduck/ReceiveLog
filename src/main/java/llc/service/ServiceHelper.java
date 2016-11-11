package llc.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by llc on 16/8/18.
 */
public class ServiceHelper {
    private static UserService userService;

    static {
        ApplicationContext ctx= new ClassPathXmlApplicationContext("applicationContext.xml");
        userService = (UserService) ctx.getBean("UserService");
    }

    public static UserService getUserService(){
        return userService;
    }

}
