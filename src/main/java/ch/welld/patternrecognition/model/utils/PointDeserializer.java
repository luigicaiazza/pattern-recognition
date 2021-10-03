package ch.welld.patternrecognition.model.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import ch.welld.patternrecognition.model.Point;

public class PointDeserializer extends StdDeserializer<Point> {
	
	private static final long serialVersionUID = 5069212669363162471L;

	public PointDeserializer() {
        this(null);
    }

    public PointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Point deserialize(JsonParser parser, DeserializationContext deserializer) {
        ObjectCodec codec = parser.getCodec();
        JsonNode node;
		try {
			node = codec.readTree(parser);
		} catch (IOException ioe) {
			return null;
		}
        
        JsonNode xNode = node.get("x");
        int x = xNode.asInt();
        JsonNode yNode = node.get("y");
        int y = yNode.asInt();
        return new Point(x, y);
    }

}
