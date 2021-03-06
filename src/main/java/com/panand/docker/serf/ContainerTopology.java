package com.panand.docker.serf;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.panand.docker.serf.bolts.IndexEvents;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

public class ContainerTopology {
	
	private static Logger logger = Logger.getLogger(ContainerTopology.class.getName());
	
	private static final String CONFIG_FILE = "serf.properties";
	
	public static void main(String args[]) {
		
		/*
		 * load properties 
		 */
		final Properties properties = new Properties();
		try {
			properties.load(ContainerTopology.class.getClassLoader().getResourceAsStream(CONFIG_FILE));
		} catch (final IOException ioException) {
            logger.error(ioException.getMessage());
        }
		
		try {
			String zkHosts = properties.getProperty("com.panand.docker.serf.container.zkHosts");
			int kafkaSpoutCount = Integer.parseInt((properties.getProperty("com.panand.docker.serf.container.spoutCount", "2")));
			String containerTopic = properties.getProperty("com.panand.docker.serf.container.containerTopic");
			String nimbusHost = properties.getProperty("com.panand.docker.serf.container.nimbus.host");
			boolean forceFromStart = Boolean.getBoolean(properties.getProperty("com.panand.docker.serf.container.forceFromStart", "false"));
			
			BrokerHosts brokerHosts = new ZkHosts(zkHosts);
			SpoutConfig kafkaSpoutConfig = new SpoutConfig(brokerHosts, containerTopic, "", ContainerTopology.class.getName());
			
			kafkaSpoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());

			if(forceFromStart) {
				kafkaSpoutConfig.forceFromStart = true; // start reading from the beginning.
			} else {
				kafkaSpoutConfig.useStartOffsetTimeIfOffsetOutOfRange = true; // start reading from current.
			}
			
			KafkaSpout kafkaSpout = new KafkaSpout(kafkaSpoutConfig);
			
			IndexEvents indexEvents = new IndexEvents(properties);
			
			TopologyBuilder builder = new TopologyBuilder();
			builder.setSpout("ContainerSpout", kafkaSpout, kafkaSpoutCount);
			builder.setBolt("IndexEventBolt", indexEvents).shuffleGrouping("ContainerSpout");
			
			Config config = new Config();
			config.setMaxSpoutPending(1000);
			config.setNumWorkers(2);
			config.put(Config.NIMBUS_HOST, nimbusHost);
			
			StormSubmitter.submitTopology("ContainerTopology", config, builder.createTopology());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}