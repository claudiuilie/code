package com.worldpay.avro;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericFixed;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public final class JsonToGenericRecordConverter {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonToGenericRecordConverter() {
    }

    public static GenericRecord fromJson(String json, Schema schema) throws IOException {
        JsonNode node = MAPPER.readTree(json);
        return fromJson(node, schema);
    }

    public static GenericRecord fromJson(JsonNode node, Schema schema) {

        if (schema.getType() != Schema.Type.RECORD) {
            throw new IllegalArgumentException("Root schema must be RECORD");
        }

        GenericRecord record = new GenericData.Record(schema);

        for (Schema.Field field : schema.getFields()) {

            JsonNode value = node.get(field.name());

            if (value == null || value.isNull()) {
                record.put(field.name(), null);
                continue;
            }

            record.put(field.name(), convert(value, field.schema()));
        }

        return record;
    }

    private static Object convert(JsonNode node, Schema schema) {

        switch (schema.getType()) {

            case STRING:
                return node.asText();

            case INT:
                return node.asInt();

            case LONG:
                return node.asLong();

            case FLOAT:
                return (float) node.asDouble();

            case DOUBLE:
                return node.asDouble();

            case BOOLEAN:
                return node.asBoolean();

            case NULL:
                return null;

            case RECORD:
                return fromJson(node, schema);

            case ARRAY:

                List<Object> list = new ArrayList<>();

                for (JsonNode child : node) {
                    list.add(convert(child, schema.getElementType()));
                }

                return list;

            case MAP:

                Map<String, Object> map = new HashMap<>();

                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

                while (fields.hasNext()) {

                    Map.Entry<String, JsonNode> entry = fields.next();

                    map.put(
                            entry.getKey(),
                            convert(entry.getValue(), schema.getValueType()));
                }

                return map;

            case ENUM:

                return new GenericData.EnumSymbol(schema, node.asText());

            case FIXED:

                byte[] fixedBytes = Base64.getDecoder().decode(node.asText());

                GenericFixed fixed = new GenericData.Fixed(schema);

                fixed.bytes(fixedBytes);

                return fixed;

            case BYTES:

                return ByteBuffer.wrap(Base64.getDecoder().decode(node.asText()));

            case UNION:

                return convertUnion(node, schema);

            default:

                throw new UnsupportedOperationException(
                        "Unsupported type: " + schema.getType());
        }
    }

    private static Object convertUnion(JsonNode node, Schema unionSchema) {

        List<Schema> types = unionSchema.getTypes();

        if (node.isNull()) {
            return null;
        }

        for (Schema candidate : types) {

            if (candidate.getType() == Schema.Type.NULL) {
                continue;
            }

            try {
                return convert(node, candidate);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException(
                "Cannot resolve union for value: " + node);
    }
}



String schemaString =
        cachedSchemaRegistryClient
                .getLatestSchemaMetadata(subject)
                .getSchema();

Schema schema = new Schema.Parser().parse(schemaString);

GenericRecord record =
        JsonToGenericRecordConverter.fromJson(jsonPayload, schema);

boolean valid = GenericData.get().validate(schema, record);

System.out.println(valid);




GenericRecord record =
        JsonToGenericRecordConverter.fromJson(jsonPayload, schema);

if (!GenericData.get().validate(schema, record)) {
    throw new IllegalArgumentException("Payload is not valid.");
}
