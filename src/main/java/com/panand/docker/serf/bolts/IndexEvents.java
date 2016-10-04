package com.panand.docker.serf.bolts;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class IndexEvents implements IRichBolt {
	
	OutputCollector _collector;

	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	public void execute(Tuple tuple) {
		// simply ack the tuple.
		_collector.ack(tuple);
	}

	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		_collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
