package com.potaliadmin.framework.cache;

import com.potaliadmin.impl.framework.ServiceLocatorFactory;
import com.potaliadmin.impl.framework.properties.AppProperties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Shakti Singh on 12/23/14.
 */
public class ESCacheManager {

  private Logger logger = LoggerFactory.getLogger(ESCacheManager.class);

  AppProperties appProperties;

  private static ESCacheManager _mInstance;
  //private Node node;
  private Client client;

  static {
    _mInstance = new ESCacheManager();
  }

  private ESCacheManager() {}

  public static ESCacheManager getInstance() {
    return _mInstance;
  }

  public void startNode() {
    logger.info("============ Starting Up Elastic Search Node ====================");
    String clusterName = getAppProperties().getEsClusterName();
    try {
      //node = NodeBuilder.nodeBuilder().clusterName(clusterName).data(false).client(true).node();
      //client = node.client();
      Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", clusterName).build();
      client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
    } catch (Exception e) {
      e.printStackTrace();
    }

    logger.info("============= Elastic Search Node Started ========================");
  }

  public void stopNode() {
    logger.info("============ Closing Down Elastic Search Node =====================");
    client.close();
    //node.close();
    logger.info("============ Closed Down Elastic Search Node =====================");
  }

  public Client getClient() {
    return client;
  }

  public AppProperties getAppProperties() {
    if (appProperties == null) {
      appProperties = ServiceLocatorFactory.getBean(AppProperties.class);
    }
    return appProperties;
  }
}
