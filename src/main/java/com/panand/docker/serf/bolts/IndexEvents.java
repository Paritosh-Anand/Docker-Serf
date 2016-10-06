package com.panand.docker.serf.bolts;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

/*
 * @author Paritosh Anand <paritosh.anand@makemytrip.com>
 */
public class IndexEvents implements IRichBolt {
	
	private static Logger logger = Logger.getLogger(IndexEvents.class.getName());
	private Properties properties = null;
	
	OutputCollector _collector;
	
	private TransportClient client = null;
	
	public IndexEvents(Properties properties) {
		this.properties = properties;
	}

	public void cleanup() {
		client.close();
	}

	public void execute(Tuple tuple) {

		logger.info("Tupple got from spout - " + tuple.getString(0));
		try {
			IndexResponse indexResponse = client.prepareIndex("docker-container-events-2016-10", "container")
					.setSource(tuple.getString(0)).get();
			
			logger.info("sent message to elastic search - " + indexResponse.getIndex());
			
		} catch (Exception e) {
			logger.error(e);
		}
		_collector.ack(tuple);
	}

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		
		_collector = collector;
		
		String esClusterName = properties.getProperty("com.panand.docker.serf.elasticsearch.cluster.name", "MUM-ACCESS-Cluster");
		String esHost = properties.getProperty("com.panand.docker.serf.elasticsearch.host", "10.96.46.20");
		
		logger.info("preparing elastic search client for es host " + esHost + " and cluster " + esClusterName);
		
		try {

			Settings settings = Settings.settingsBuilder().put("cluster.name", esClusterName).build();
			
			client = TransportClient.builder().settings(settings).build();
			client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(esHost), 9300));
			
		} catch (UnknownHostException e) {
			logger.error(e);
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
