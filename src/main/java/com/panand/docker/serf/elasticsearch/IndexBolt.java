package com.panand.docker.serf.elasticsearch;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexBolt implements IRichBolt {
	
	private final static Logger logger = LoggerFactory.getLogger(IndexBolt.class);

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public void execute(Tuple kafkaMessage) {

		logger.info("message from kafka -- " + kafkaMessage);
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		// TODO Auto-generated method stub
		
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
