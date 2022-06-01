
package com.biperf.core.ui;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebServicesConfig extends WebMvcConfigurerAdapter
{

  @Override
  public void configureMessageConverters( List<HttpMessageConverter<?>> converters )
  {
    GMappingJackson2HttpMessageConverter jsonConverter = new GMappingJackson2HttpMessageConverter();
    converters.add( jsonConverter );
    super.configureMessageConverters( converters );
  }

}