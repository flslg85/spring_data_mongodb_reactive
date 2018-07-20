package com.mongo.db.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final ReactiveMongoTemplate template;

    @Autowired
    public UserController(UserRepository userRepository, ReactiveMongoTemplate template) {
        this.userRepository = userRepository;
        this.template = template;
    }

    @GetMapping("/{id}")
    public Mono<User> findUser(@PathVariable("id") String id) {
        return template.findById(id, User.class);
    }

    @PostMapping
    public Mono<User> saveUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/template")
    public Flux<User> findAllUsers(User user,
                                   @RequestParam(required = false) int page,
                                   @RequestParam(required = false) int size) {
        Query query = new Query(
                new Criteria().orOperator(
                        Criteria.where("name").is(user.getName()),
                        Criteria.where("age").is(user.getAge())))
                .with(PageRequest.of(page, size));
        return template.find(query, User.class);
    }
}
