package com.soaringloong.jfrm.framework.web.config;// package com.frm.springboot;

//
// import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
// import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
// import org.springframework.boot.web.server.WebServerFactoryCustomizer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// @Configuration
// public class TomcatWebConfig implements WebMvcConfigurer {
//
// /**
// * 解决文件名中含有":\\"等特殊字符时，接口400的问题<p>
// * Tomcat在 7.0.73, 8.0.39, 8.5.7 版本后，，添加了对于http头的验证。就是严格按照 RFC 3986规范进行访问解析，而 RFC
// 3986规范定义了Url中只允许包含英文字母（a-zA-Z）、数字（0-9）、-_.~4个特殊字符
// * 以及所有保留字符(RFC3986中指定了以下字符为保留字符：! * ' ( ) ; : @ & = + $ , / ? # [ ])。若使用外置tomcat
// 需要到server.xml中配置如下:
// * <Connector port="8080" protocol="HTTP/1.1"
// * connectionTimeout="30000"
// * maxThreads="800"
// * minSpareThreads="50"
// * redirectPort="8443"
// * relaxedQueryChars="[,],|,{,},^,&#x5c;,&#x60;,&quot;,&lt;,&gt;"/>
// *
// *
// *
// * <p>
// * java.lang.IllegalArgumentException:
// * Invalid character found in the request target. The valid characters are defined in
// RFC 7230 and RFC 3986.
// * </p>
// * 对于Get请求还可以通过以下方法解决，这种方法不需要配置 relaxedQueryChars，但是较繁琐
// * <p>
// * var name = "张三:";
// * name = encodeURIComponent(name);
// * name = encodeURIComponent(name);//二次编码
// * //alert(name);
// * url = url + "?name="+name;
// * window.location.href = url;
// *
// * @return
// * @GetMapping("/test") public String test(@RequestParam("name") String name) throws
// UnsupportedEncodingException{
// * name = URLDecoder.decode(name, "UTF-8");
// * System.out.println(name);
// * return "index";
// * }
// * </p>
// * 总结：最简单的方法是配置服务器（在 application.properties
// 中添加一行）server.tomcat.relaxed-query-chars=<>[\]^`{|}:
// */
// @Bean
// public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer()
// {
// return new WebServerFactoryCustomizer<TomcatServletWebServerFactory>() {
// @Override
// public void customize(TomcatServletWebServerFactory factory) {
// factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
// connector.setProperty("relaxedPathChars", "<>[\\]^`{|}:");
// connector.setProperty("relaxedQueryChars", "<>[\\]^`{|}:");
// });
// }
// };
// }
//
// }
