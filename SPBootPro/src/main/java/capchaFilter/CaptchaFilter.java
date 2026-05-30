package capchaFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.controller.LoginController;
import com.example.service.CaptchaService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaService captchaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException, java.io.IOException {

        if (request.getRequestURI().equals("/login") 
                && request.getMethod().equalsIgnoreCase("POST")) {

        	String captcha = request.getParameter("g-recaptcha-response");
        	System.out.println("Captcha Token: " + captcha);

            if (captcha == null || !captchaService.verifyCaptcha(captcha)) {
            	request.getSession().setAttribute(
            	        "error",
            	        "Please verify CAPTCHA");

            	response.sendRedirect("/after-varification-login");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
