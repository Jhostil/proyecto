package co.edu.uniquindio.proyecto;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class ResourceWebConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File carpeta = new File("web/src/main/resources/META-INF/resources/uploads/");
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        registry.addResourceHandler("web/src/main/resources/META-INF/resources/uploads/**").addResourceLocations("file:web/src/main/resources/META-INF/resources/uploads/");
    }

}
