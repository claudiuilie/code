@Service
public class AvroMessageProducer {

    private final SchemaRegistryClient schemaRegistryClient;
    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AvroMessageProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.schemaRegistryClient = new CachedSchemaRegistryClient(
                "http://localhost:8081", 100);
    }

    public void sendFromJson(String topic, String key, String jsonPayload) throws Exception {
        // subject-ul standard e "<topic>-value"
        String subject = topic + "-value";
        SchemaMetadata metadata = schemaRegistryClient.getLatestSchemaMetadata(subject);
        Schema avroSchema = new Schema.Parser().parse(metadata.getSchema());

        GenericRecord record = jsonToGenericRecord(jsonPayload, avroSchema);
        kafkaTemplate.send(topic, key, record);
    }

    private GenericRecord jsonToGenericRecord(String json, Schema schema) throws IOException {
        JsonNode node = objectMapper.readTree(json);
        GenericRecordBuilder builder = new GenericRecordBuilder(schema);

        for (Schema.Field field : schema.getFields()) {
            JsonNode value = node.get(field.name());
            if (value != null && !value.isNull()) {
                builder.set(field, convertValue(value, field.schema()));
            }
        }
        return builder.build();
    }

    private Object convertValue(JsonNode value, Schema fieldSchema) {
        Schema.Type type = resolveType(fieldSchema);
        return switch (type) {
            case STRING -> value.asText();
            case INT -> value.asInt();
            case LONG -> value.asLong();
            case FLOAT -> (float) value.asDouble();
            case DOUBLE -> value.asDouble();
            case BOOLEAN -> value.asBoolean();
            case RECORD -> {
                // recursiv pentru obiecte nested
                GenericRecordBuilder nested = new GenericRecordBuilder(fieldSchema);
                fieldSchema.getFields().forEach(f -> {
                    JsonNode nestedVal = value.get(f.name());
                    if (nestedVal != null && !nestedVal.isNull()) {
                        nested.set(f, convertValue(nestedVal, f.schema()));
                    }
                });
                yield nested.build();
            }
            default -> value.asText();
        };
    }

    // gestionează UNION (ex: ["null", "string"]) - comun pentru câmpuri opționale
    private Schema.Type resolveType(Schema schema) {
        if (schema.getType() == Schema.Type.UNION) {
            return schema.getTypes().stream()
                    .filter(s -> s.getType() != Schema.Type.NULL)
                    .findFirst()
                    .map(Schema::getType)
                    .orElse(Schema.Type.STRING);
        }
        return schema.getType();
    }
}
