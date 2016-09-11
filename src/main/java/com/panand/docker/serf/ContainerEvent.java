package com.panand.docker.serf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import com.panand.docker.serf.elasticsearch.IndexBolt;

public class ContainerEvent {
	
	public static void main(String args[]) throws IOException, AlreadyAliveException, InvalidTopologyException, AuthorizationException {
		
		Properties properties = SerfProperties.getSerfProperties();
		
		String brokerZkStr = properties.getProperty("com.panand.docker.serf.container.zkHosts");
		String topicName = properties.getProperty("com.panand.docker.serf.container.topic", "container");
		String id = properties.getProperty("com.panand.docker.serf.container.id", "");
		int spoutCount = Integer.parseInt(properties.getProperty("com.panand.docker.serf.container.spoutCount", "1"));
		int indexBoltCount = Integer.parseInt(properties.getProperty("com.panand.docker.serf.container.indexBoltCount", "1"));
		
		BrokerHosts hosts = new ZkHosts(brokerZkStr);
		SpoutConfig spoutConfig = new SpoutConfig(hosts, topicName, "/" + topicName, id);
		spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		spoutConfig.useStartOffsetTimeIfOffsetOutOfRange = true;
		spoutConfig.ignoreZkOffsets = true;
		
		KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);
		
		IndexBolt indexBolt = new IndexBolt();
		
		Config conf = new Config();
		conf.setNumWorkers(4);
		conf.setDebug(true);
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("DockerSpout", kafkaSpout, spoutCount);
		builder.setBolt("IndexBolt", indexBolt, indexBoltCount).shuffleGrouping("DockerSpout");
		
		if (args != null && args.length > 0) {
	    	conf.setNumWorkers(3);
		    StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
	    }
	    else {
	    	LocalCluster cluster = new LocalCluster();
	    	cluster.submitTopology("test", conf, builder.createTopology());
	    	Utils.sleep(10000);
	    	cluster.killTopology("test");
		    cluster.shutdown();
	    }

	}
}