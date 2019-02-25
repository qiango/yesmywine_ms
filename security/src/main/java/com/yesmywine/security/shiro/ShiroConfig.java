package com.yesmywine.security.shiro;

import com.yesmywine.security.redis.ShiroRedisCacheManager;
import com.yesmywine.security.redis.ShiroRedisSessionDAO;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by SJQ on 2017/3/24.
 */
@Configuration
public class ShiroConfig {

//    @Resource
//    private ShiroRedisSessionDAO redisSessionDAO;

//    /**
//     * FilterRegistrationBean
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
//        filterRegistration.setEnabled(true);
//        filterRegistration.addUrlPatterns("/*");
//        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
//        return filterRegistration;
//    }

    /**
     * @sees org.apache.shiro.spring.web.ShiroFilterFactoryBean
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

//        shiroFilterFactoryBean.setSecurityManager(securityManager());
        shiroFilterFactoryBean.setSecurityManager(getDefaultWebSecurityManager());
        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthor");

        // 拦截器.
        Map<String, Filter> filters = new LinkedHashMap();
        // 配置不会被拦截的链接 顺序判断
//        filters.put("anon", new RequestFilter());
        filters.put("authc", new AuthenticationFilter());//将自定义 的FormAuthenticationFilter注入shiroFilter中
        filters.put("anon", urlPermissionsFilter());
//        filters.put("anon", new AnonymousFilter());
        shiroFilterFactoryBean.setFilters(filters);
//        Map<String, Filter> filters_1 = shiroFilterFactoryBean.getFilters();
//        filters_1.put("authc", new AuthenticationFilter());

        //anon匿名 authc登录认证  user用户已登录 logout退出filter
        Map<String, String> chains = new LinkedHashMap();
        chains.put("/user/doLogin", "anon");
        chains.put("/user/login", "anon");
        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        chains.put("/user/logout", "logout");
        chains.put("/activity**", "anon");
        chains.put("/inventory/channels**", "user");
        chains.put("/inventory/warehouses**", "anon");
        chains.put("/user/getPerms**", "user");
        chains.put("/activity/share", "authc");
        chains.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chains);
        return shiroFilterFactoryBean;
    }

//    /**
//     * @see org.apache.shiro.mgt.SecurityManager
//     * @return
//     */
//    @Bean(name="securityManager")
//    public DefaultWebSecurityManager securityManager() {
//        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        manager.setRealm(authRealm());
//        manager.setCacheManager(redisCacheManager());
//        manager.setSessionManager(defaultWebSessionManager());
//        return manager;
//    }
//
//    @Bean
//    @DependsOn(value="lifecycleBeanPostProcessor")
//    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
//        daap.setProxyTargetClass(true);
//        return daap;
//    }
//
    /**
     * @seess DefaultWebSessionManager
     * @return
     */
    @Bean(name="sessionManager")
    public SessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(redisCacheManager());
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setSessionDAO(getRedisSessionDAO());
//        sessionManager.setGlobalSessionTimeout(1800);
//        sessionManager.setCacheManager(redisCacheManager());
        return sessionManager;
    }
    @Bean(name = "ShiroRealmImpl")
    public AuthRealm authRealm() {
        AuthRealm authRealm = new AuthRealm();
//        authRealm.setCacheManager(redisCacheManager());
//        authRealm.setCredentialsMatcher(credentialsMatcher());
        return authRealm;
    }
    //配置自定义的密码比较器
    @Bean(name="credentialsMatcher")
    public CredentialsMatcher credentialsMatcher() {
        return new CredentialsMatcher();
    }

    @Bean
    public URLPermissionsFilter urlPermissionsFilter() {
        return new URLPermissionsFilter();
    }

    @Bean
    public ShiroRedisCacheManager redisCacheManager() {
        return new ShiroRedisCacheManager();
    }

    private static Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();


    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager() {
        DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
        dwsm.setRealm(authRealm());
        dwsm.setCacheManager(redisCacheManager());
        dwsm.setSessionManager(defaultWebSessionManager());
        return dwsm;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(getDefaultWebSecurityManager());
        return new AuthorizationAttributeSourceAdvisor();
    }

    @Bean
    public ShiroRedisSessionDAO getRedisSessionDAO(){
        return new ShiroRedisSessionDAO();
    }

}