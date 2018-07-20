package com.mongo.db.sampleData;

import com.mongo.db.user.User;
import com.mongo.db.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
@ActiveProfiles("test")
public class SampleDataInitializer {
    @Autowired
    private ReactiveMongoTemplate template;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        template.collectionExists(User.class)
                .flatMap(isExist -> isExist ? template.dropCollection(User.class) : Mono.just(isExist))
                .thenMany(template.insertAll(Arrays.asList(
                        new User().setName("alice").setAge(10),
                        new User().setName("bob").setAge(10)
                )))
                .thenMany(userRepository.saveAll(Arrays.asList(
                        new User().setName("cyndi").setAge(10),
                        new User().setName("diana").setAge(10)
                )))
                .subscribe();
    }
}
