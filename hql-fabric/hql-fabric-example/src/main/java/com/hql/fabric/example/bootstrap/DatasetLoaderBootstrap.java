package com.hql.fabric.example.bootstrap;

import com.hql.fabric.example.loader.ExampleDatasetLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This bootstrap is added for invoking {@link ExampleDatasetLoader}
 * to load datasets file from datasets.
 * <p>
 * To avoid same datasets be duplicated loading to db tables rised problems,
 * we add a thread safe hash map to record the loading status.
 */

@Component
public class DatasetLoaderBootstrap implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(DatasetLoaderBootstrap.class);
    private final Map<String, Boolean> loadDatasetStatusMap = new ConcurrentHashMap<>();

    @Autowired
    private ExampleDatasetLoader datasetLoader;

    @Value(value = "${example.dataset.path:example_datasets.yml}")
    public String datasetPath;

    @Transactional
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            LOG.info("Loading dataset from {}", datasetPath);
            datasetLoader.loadDataset(datasetPath);
            loadDatasetStatusMap.put(datasetPath, true);
        } catch (Exception e) {
            LOG.error("Failed to load dataset from {}", datasetPath, e);
            loadDatasetStatusMap.put(datasetPath, false);
        }
    }

    // -- getter && setter --
    public Map<String, Boolean> getLoadDatasetStatusMap() {
        return this.loadDatasetStatusMap;
    }
}
