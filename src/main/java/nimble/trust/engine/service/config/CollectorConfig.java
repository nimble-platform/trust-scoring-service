package nimble.trust.engine.service.config;

import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;

import nimble.trust.engine.collector.AbstractCollector;
import nimble.trust.engine.collector.Collector;
import nimble.trust.engine.collector.trustdb.InternalCollector;

public enum CollectorConfig {
	
	InternalCollector( new InternalCollector(""));
	
	private  final Collector collector;
	
	CollectorConfig(Collector collector) {
		this.collector = collector;
	}
	
	public Collector getCollector(){
		return collector;
	}
	
	public static Collector getCollectorByType(String name) {
		List<Collector> list = Lists.newArrayList();
		for (Collector collector : list) {
			if (collector.getName().contains(name))
				return collector;
		}
		return null;
	}

	public static List<Collector> read() {
		List<Collector> list = Lists.newArrayList();
		try {
			InputStream is = CollectorConfig.class.getResourceAsStream("/collectors.json");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(is);
			ArrayNode colls = (ArrayNode) node.get("collectors");
			for (JsonNode jsonNode : colls) {
				String className = jsonNode.get("class").textValue();
				String endpoint = jsonNode.get("endpoint").textValue();
				AbstractCollector c =  (AbstractCollector) Class.forName(className).newInstance();
				c.setSourceUri(endpoint);
				list.add(c);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
}
