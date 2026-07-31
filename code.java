private Object convertValue(JsonNode value, Schema fieldSchema) {
    Schema resolvedSchema = resolveSchema(fieldSchema); // <-- schema reală, nu doar tipul
    Schema.Type type = resolvedSchema.getType();

    return switch (type) {
        case STRING -> value.asText();
        case INT -> value.asInt();
        case LONG -> value.asLong();
        case FLOAT -> (float) value.asDouble();
        case DOUBLE -> value.asDouble();
        case BOOLEAN -> value.asBoolean();
        case RECORD -> {
            GenericRecordBuilder nested = new GenericRecordBuilder(resolvedSchema); // folosim resolvedSchema, nu fieldSchema
            resolvedSchema.getFields().forEach(f -> {
                JsonNode nestedVal = value.get(f.name());
                if (nestedVal != null && !nestedVal.isNull()) {
                    nested.set(f, convertValue(nestedVal, f.schema()));
                }
            });
            yield nested.build();
        }
        case ARRAY -> {
            List<Object> list = new ArrayList<>();
            Schema elementSchema = resolvedSchema.getElementType();
            value.forEach(el -> list.add(convertValue(el, elementSchema)));
            yield list;
        }
        default -> value.asText();
    };
}

// Extrage schema reală dintr-un union (ex: ["null", "int"] sau ["null", {record}])
private Schema resolveSchema(Schema schema) {
    if (schema.getType() == Schema.Type.UNION) {
        return schema.getTypes().stream()
                .filter(s -> s.getType() != Schema.Type.NULL)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Union has no non-null type: " + schema));
    }
    return schema;
}
