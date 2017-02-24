package org.asaunin.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.asaunin.socialnetwork.config.Constants.AVATAR_FOLDER;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(AVATAR_FOLDER + "**").addResourceLocations(AVATAR_FOLDER);
	}

}
