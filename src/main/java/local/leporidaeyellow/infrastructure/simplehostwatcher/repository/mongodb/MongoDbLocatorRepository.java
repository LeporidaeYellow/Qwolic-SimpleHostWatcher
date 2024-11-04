package local.leporidaeyellow.infrastructure.simplehostwatcher.repository.mongodb;

import local.leporidaeyellow.infrastructure.simplehostwatcher.model.mongodb.UniformResourceLocatorItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDbLocatorRepository extends MongoRepository<UniformResourceLocatorItem, String> {}
