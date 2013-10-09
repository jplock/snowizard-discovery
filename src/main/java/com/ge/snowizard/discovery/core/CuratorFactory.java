package com.ge.snowizard.discovery.core;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import com.ge.snowizard.discovery.DiscoveryConfiguration;
import com.ge.snowizard.discovery.health.CuratorHealthCheck;
import com.ge.snowizard.discovery.manage.CuratorManager;
import com.yammer.dropwizard.config.Environment;

public class CuratorFactory {

    private final Environment environment;

    /**
     * Constructor
     *
     * @param environment
     *            {@link Environment}
     */
    public CuratorFactory(final Environment environment) {
        this.environment = environment;
    }

    /**
     * Build a new instance of a {@link CuratorFramework} and register a health
     * check and make sure it's properly managed.
     *
     * @param config
     *            {@link DiscoveryConfiguration}
     * @return {@link CuratorFramework}
     */
    public CuratorFramework build(final DiscoveryConfiguration config) {
        final CuratorFramework framework = CuratorFrameworkFactory
                .builder()
                .connectionTimeoutMs(
                        (int) config.getConnectionTimeout().toMilliseconds())
                .sessionTimeoutMs(
                        (int) config.getSessionTimeout().toMilliseconds())
                .retryPolicy(config.getRetryPolicy())
                .compressionProvider(config.getCompressionProvider())
                .connectString(config.getQuorumSpec())
                .canBeReadOnly(config.isReadOnly())
                .namespace(config.getNamespace()).build();

        environment.manage(new CuratorManager(framework));
        environment.addHealthCheck(new CuratorHealthCheck(framework));
        return framework;
    }
}
