package com.tks.user.service.controllers;

import com.tks.user.service.entities.User;
import com.tks.user.service.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User user1=userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    //single user get
    int retryCount=1;
    @GetMapping("/{userId}")
    //@CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallback")
     //@Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallback")
    //@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId){
        System.out.println("Retry count:"+retryCount);
        retryCount++;

        User user = userService.getUser(userId);
        return  ResponseEntity.ok(user);
    }

    //created fallback method for circuit breaker

    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
        User user=User.builder()
                .email("dummy@gmail.com")
                .name("dummy")
                .about("This is user is created dummy because some service is down")
                .userId("4646")
                .build();
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    //all user get
    @GetMapping
    public ResponseEntity<List<User>> getAllUser(){
        List<User> allUser=userService.getAllUser();
        return ResponseEntity.ok(allUser);
    }

}
