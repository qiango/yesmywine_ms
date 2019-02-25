package com.yesmywine.security.jwt;//package com.hzbuvi.security.jwt;
//
//import org.apache.shiro.web.servlet.OncePerRequestFilter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by SJQ on 2017/6/2.
// */
//@Component
//public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
//
////    @Autowired
////    private UserDetailsService userDetailsService;
////
////    @Autowired
////    private JwtTokenUtil jwtTokenUtil;
//
////    @Value("${jwt.header}")
//    private String tokenHeader;
//
////    @Value("${jwt.tokenHead}")
//    private String tokenHead;
//
//
//    protected void doFilterInternal(ServletRequest sltRequest, ServletResponse sltResponse, FilterChain chain) throws ServletException, IOException {
//        HttpServletRequest request = (HttpServletRequest)sltRequest;
//        HttpServletResponse response = (HttpServletResponse)sltResponse;
//        String authHeader = request.getHeader(this.tokenHeader);
////        if (authHeader != null && authHeader.startsWith(tokenHead)) {
////            final String authToken = authHeader.substring(tokenHead.length()); // The part after "Bearer "
////            String username = jwtTokenUtil.getUsernameFromToken(authToken);
////
//////            logger.info("checking authentication " + username);
////
////            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
////
////                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
////
////                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
////                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
////                            userDetails, null, userDetails.getAuthorities());
////                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
////                            request));
////                    logger.info("authenticated user " + username + ", setting security context");
////                    SecurityContextHolder.getContext().setAuthentication(authentication);
////                }
////            }
////        }
////
////        chain.doFilter(request, response);
//    }
//}
