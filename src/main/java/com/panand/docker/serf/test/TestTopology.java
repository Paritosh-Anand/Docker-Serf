package com.panand.docker.serf.test;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.testing.TestWordSpout;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

public class TestTopology {

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

	    builder.setSpout("word", new TestWordSpout(), 10);
	    builder.setBolt("exclaim1", new TestBolt(), 3).shuffleGrouping("word");
	    builder.setBolt("exclaim2", new TestBolt(), 2).shuffleGrouping("exclaim1");

	    Config conf = new Config();
	    conf.setDebug(true);

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
