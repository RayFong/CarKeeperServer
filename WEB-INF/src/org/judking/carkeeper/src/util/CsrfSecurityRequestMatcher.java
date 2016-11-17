package org.judking.carkeeper.src.util;

import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by leilf on 2016/11/13.
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private List<String> allowedUrls;

    public List<String> getAllowedUrls() {
        return allowedUrls;
    }

    public void setAllowedUrls(List<String> allowedUrls) {
        this.allowedUrls = allowedUrls;
    }

    @Override
    public boolean matches(HttpServletRequest httpServletRequest) {
        String servletPath = httpServletRequest.getServletPath();
        for (String url : allowedUrls) {
            if (servletPath.contains(url)) {
                return false;
            }
        }

        return !allowedMethods.matcher(httpServletRequest.getMethod()).matches();
    }
}
