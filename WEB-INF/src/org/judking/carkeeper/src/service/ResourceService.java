package org.judking.carkeeper.src.service;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service("resourceService")
public class ResourceService implements ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    public File get(String path) {
        Resource resource = resourceLoader.getResource(path);
        try {
            return resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

}
