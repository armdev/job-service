package io.ameria.job;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author armena
 */
@Configuration
@EnableMongoRepositories(basePackages = "io.ameria.job.repositories",
        mongoTemplateRef = "primaryMongoTemplate")
@Import(value = MongoAutoConfiguration.class)
@EntityScan("io.ameria.job.domain.*")
public class MongoConfiguration extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.primary.uri}")
    private String connection;

    @Value("${spring.data.mongodb.primary.database}")
    private String database;

    @Bean
    @Primary
    public MongoDatabaseFactory primaryFactory() {
        return new SimpleMongoClientDatabaseFactory(((connection)));
    }

    @Primary
    @Bean(name = "primaryMongoTemplate")
    public MongoTemplate primaryMongoTemplate() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(primaryFactory()), new MongoMappingContext());
        converter.setCustomConversions(customConversions());
        converter.afterPropertiesSet();
        return new MongoTemplate(primaryFactory(), converter);
    }

    @Override
    @Primary
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(connection);

        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .readConcern(ReadConcern.LOCAL)
                .applyConnectionString(connectionString)
                .writeConcern(WriteConcern.W1)
                .readPreference(ReadPreference.primary())
                .retryWrites(true)
                .build();
        return MongoClients.create(mongoClientSettings);
    }

//    @Bean(name = "primaryTransactionManager")
//    @Primary
//    public MongoTransactionManager primaryTransactionManager(MongoDatabaseFactory dbFactory) {
//        TransactionOptions transactionOptions = TransactionOptions.builder()
//                .readConcern(ReadConcern.LOCAL).writeConcern(WriteConcern.W1).build();
//        return new MongoTransactionManager(dbFactory, transactionOptions);
//
//    }

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    protected boolean autoIndexCreation() {
        return false;
    }


}
