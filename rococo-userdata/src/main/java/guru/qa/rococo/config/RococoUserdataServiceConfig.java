package guru.qa.rococo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class RococoUserdataServiceConfig {

  private final String rococoUserdataBaseUri;

  public RococoUserdataServiceConfig(@Value("${rococo-userdata.base-uri}") String rococoUserdataBaseUri) {
    this.rococoUserdataBaseUri = rococoUserdataBaseUri;
  }

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "userdata")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema userdataSchema) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("RococoUserdataPort");
    wsdl11Definition.setLocationUri(rococoUserdataBaseUri + "/ws");
    wsdl11Definition.setTargetNamespace("rococo-userdata");
    wsdl11Definition.setSchema(userdataSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema userdataSchema() {
    return new SimpleXsdSchema(new ClassPathResource("userdata.xsd"));
  }
}
