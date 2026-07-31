ListenableFuture<SendResult<String, GenericRecord>> future = kafkaTemplate.send(topic, key, record);
future.addCallback(new ListenableFutureCallback<>() {
    @Override
    public void onSuccess(SendResult<String, GenericRecord> result) {
        log.info("Mesaj trimis cu succes: partition={}, offset={}",
            result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
    }

    @Override
    public void onFailure(Throwable ex) {
        log.error("Eroare la trimiterea mesajului pe topicul {}", topic, ex);
    }
});
