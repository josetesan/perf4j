/**
 * Provides the concrete {@link org.perf4j.log4j.servlet.GraphingServlet} class that can be installed in a web.xml
 * file to expose graphs generated by a {@link org.perf4j.log4j.GraphingStatisticsAppender}. The following example
 * web.xml shows how the servlet could be configured:
 *
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *
 * &lt;web-app version="2.4"
 *          xmlns="http://java.sun.com/xml/ns/j2ee"
 *          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *          xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"&gt;
 *
 *   &lt;servlet&gt;
 *     &lt;servlet-name&gt;perf4j&lt;/servlet-name&gt;
 *     &lt;servlet-class&gt;org.perf4j.servlet.Log4JGraphingServlet&lt;/servlet-class&gt;
 *     &lt;!--
 *       The values for the graphNames init param must match the names of the GraphingStatisticsAppenders
 *       as configured in the log4j.xml file.
 *     --&gt;
 *     &lt;init-param&gt;
 *       &lt;param-name&gt;graphNames&lt;/param-name&gt;
 *       &lt;param-value&gt;PageTimes,PageTPS&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 *   &lt;/servlet&gt;
 *
 *   &lt;servlet-mapping&gt;
 *     &lt;servlet-name&gt;perf4j&lt;/servlet-name&gt;
 *     &lt;url-pattern&gt;/perf4j&lt;/url-pattern&gt;
 *   &lt;/servlet-mapping&gt;
 * &lt;/web-app&gt;
 * </pre>
 */
package org.perf4j.log4j.servlet;