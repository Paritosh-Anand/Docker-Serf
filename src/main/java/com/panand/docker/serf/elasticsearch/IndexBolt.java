package com.panand.docker.serf.elasticsearch;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexBolt extends BaseRichBolt {
	
	private final static Logger logger = LoggerFactory.getLogger(IndexBolt.class);
	
	OutputCollector _collector;

	public void execute(Tuple tupple) {
		_collector.ack(tupple);
	}

	public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
		_collector = arg2;
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
