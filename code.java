public static void trimTextValues(JsonNode node) {

        if (node instanceof ObjectNode objectNode) {

            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();

                JsonNode child = field.getValue();

                // daca e text => trim
                if (child instanceof TextNode textNode) {

                    String trimmed = textNode.textValue().trim();

                    objectNode.put(field.getKey(), trimmed);

                } else {
                    // recursiv
                    trimTextValues(child);
                }
            }

        } else if (node instanceof ArrayNode arrayNode) {

            for (int i = 0; i < arrayNode.size(); i++) {

                JsonNode child = arrayNode.get(i);

                if (child instanceof TextNode textNode) {

                    String trimmed = textNode.textValue().trim();

                    arrayNode.set(i, TextNode.valueOf(trimmed));

                } else {
                    trimTextValues(child);
                }
            }
        }
    }
