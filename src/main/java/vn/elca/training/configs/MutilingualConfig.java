package vn.elca.training.configs;

import java.util.Locale;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class MutilingualConfig extends WebMvcConfigurationSupport {
    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        // CookieLocaleResolver resolver = new CookieLocaleResolver();
        // resolver.setCookieDomain("myAppLocaleCookie");
        // 60 minutes
        // resolver.setCookieMaxAge(60 * 60);
        // return resolver;
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.FRENCH);
        return localeResolver;
    }

    // @Bean(name = "messageSourceI18n")
    // public MessageSource getMessageResource() {
    // ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
    // // Read i18n/messages_xxx.properties file.
    // // For example: i18n/messages_en.properties
    // messageResource.addBasenames("classpath:i18n/messages");
    // // messageResource.addBasenames("clas");
    // messageResource.setDefaultEncoding("UTF-8");
    // return messageResource;
    // }
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
