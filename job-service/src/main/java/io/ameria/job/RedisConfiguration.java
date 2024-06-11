package io.ameria.job;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.mappers.JobMapper;
import org.jobrunr.storage.StorageProvider;
import org.jobrunr.storage.nosql.redis.LettuceRedisStorageProvider;
import static org.jobrunr.utils.resilience.RateLimiter.Builder.rateLimit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
@Slf4j
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private Integer port;
    @Value("${spring.data.redis.password}")
    private String password;
    @Value("${spring.data.redis.username}")
    private String username;

    @Value(value = "${spring.profiles.active}")
    private String profile;

//    @Bean
//    public LettuceConnectionFactory lettuceConnectionFactory() {
//
//        if (!profile.equalsIgnoreCase("LOCAL")) {
//            var redisConfiguration = new RedisStandaloneConfiguration();
//            redisConfiguration.setHostName(host);
//            redisConfiguration.setPort(port);
//            redisConfiguration.setPassword(password);
//            redisConfiguration.setUsername(username);
//            return new LettuceConnectionFactory(redisConfiguration);
//        }
//
//        if (profile.equalsIgnoreCase("LOCAL")) {
//            RedisStandaloneConfiguration redisStandaloneConfiguration
//                    = new RedisStandaloneConfiguration("redis", 6379);
//            return new LettuceConnectionFactory(redisStandaloneConfiguration);
//        }
//
//        return null;
//
//    }
//    @Bean
//    public RedisTemplate<String, String> redisTemplate() {
//        var template = new RedisTemplate<String, String>();
//        template.setConnectionFactory(lettuceConnectionFactory());
//        template.setHashValueSerializer(new StringRedisSerializer());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        return template;
//    }
    @Bean
    public StorageProvider storageProvider(JobMapper jobMapper) {
        final LettuceRedisStorageProvider lettuceRedisStorageProvider = new LettuceRedisStorageProvider(getRedisClient(), rateLimit().withoutLimits());
        lettuceRedisStorageProvider.setJobMapper(jobMapper);
        return lettuceRedisStorageProvider;
    }

    private RedisClient getRedisClient() {
        
        return RedisClient.create(RedisURI.create("redis", 6379));

//        RedisURI redisURI = RedisURI.Builder.redis("redis", 6379)
//                .withAuthentication(username, password)
//                .build();
//        return RedisClient.create(redisURI);

//        if (profile.equalsIgnoreCase("LOCAL")) {
//            return RedisClient.create(RedisURI.create("redis", 6379));
//        }
        ///return null;
    }

}
