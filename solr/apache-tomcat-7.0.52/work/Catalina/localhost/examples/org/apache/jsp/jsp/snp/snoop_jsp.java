/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/7.0.52
 * Generated at: 2017-09-03 13:01:19 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.jsp.snp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class snoop_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
        throws java.io.IOException, javax.servlet.ServletException {

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\n");
      out.write("<!--\n");
      out.write(" Licensed to the Apache Software Foundation (ASF) under one or more\n");
      out.write("  contributor license agreements.  See the NOTICE file distributed with\n");
      out.write("  this work for additional information regarding copyright ownership.\n");
      out.write("  The ASF licenses this file to You under the Apache License, Version 2.0\n");
      out.write("  (the \"License\"); you may not use this file except in compliance with\n");
      out.write("  the License.  You may obtain a copy of the License at\n");
      out.write("\n");
      out.write("      http://www.apache.org/licenses/LICENSE-2.0\n");
      out.write("\n");
      out.write("  Unless required by applicable law or agreed to in writing, software\n");
      out.write("  distributed under the License is distributed on an \"AS IS\" BASIS,\n");
      out.write("  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n");
      out.write("  See the License for the specific language governing permissions and\n");
      out.write("  limitations under the License.\n");
      out.write("-->\n");
      out.write("\n");
      out.write("<body bgcolor=\"white\">\n");
      out.write("<h1> Request Information </h1>\n");
      out.write("<font size=\"4\">\n");
      out.write("JSP Request Method: ");
      out.print( util.HTMLFilter.filter(request.getMethod()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Request URI: ");
      out.print( util.HTMLFilter.filter(request.getRequestURI()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Request Protocol: ");
      out.print( util.HTMLFilter.filter(request.getProtocol()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Servlet path: ");
      out.print( util.HTMLFilter.filter(request.getServletPath()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Path info: ");
      out.print( util.HTMLFilter.filter(request.getPathInfo()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Query string: ");
      out.print( util.HTMLFilter.filter(request.getQueryString()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Content length: ");
      out.print( request.getContentLength() );
      out.write("\n");
      out.write("<br>\n");
      out.write("Content type: ");
      out.print( util.HTMLFilter.filter(request.getContentType()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Server name: ");
      out.print( util.HTMLFilter.filter(request.getServerName()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Server port: ");
      out.print( request.getServerPort() );
      out.write("\n");
      out.write("<br>\n");
      out.write("Remote user: ");
      out.print( util.HTMLFilter.filter(request.getRemoteUser()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Remote address: ");
      out.print( util.HTMLFilter.filter(request.getRemoteAddr()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Remote host: ");
      out.print( util.HTMLFilter.filter(request.getRemoteHost()) );
      out.write("\n");
      out.write("<br>\n");
      out.write("Authorization scheme: ");
      out.print( util.HTMLFilter.filter(request.getAuthType()) );
      out.write(" \n");
      out.write("<br>\n");
      out.write("Locale: ");
      out.print( request.getLocale() );
      out.write("\n");
      out.write("<hr>\n");
      out.write("The browser you are using is\n");
      out.print( util.HTMLFilter.filter(request.getHeader("User-Agent")) );
      out.write("\n");
      out.write("<hr>\n");
      out.write("</font>\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
