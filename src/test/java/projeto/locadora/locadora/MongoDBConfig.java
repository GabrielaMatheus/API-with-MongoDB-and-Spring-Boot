package projeto.locadora.locadora;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDBConfig implements BeforeAllCallback, AfterAllCallback {

    private MongoDBContainer mongoDBContainer;

    @Override
    public void afterAll(ExtensionContext context) throws Exception {}

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));
        mongoDBContainer.start();
        System.setProperty("spring.data.mongodb.uri", mongoDBContainer.getReplicaSetUrl());
    }
}
