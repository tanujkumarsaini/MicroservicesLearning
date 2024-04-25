package com.tks.user.service.services.impl;

import com.tks.user.service.entities.Hotel;
import com.tks.user.service.entities.Rating;
import com.tks.user.service.entities.User;
import com.tks.user.service.exceptions.ResourceNotFoundException;
import com.tks.user.service.external.services.HotelService;
import com.tks.user.service.repositories.UserRepository;
import com.tks.user.service.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private HotelService hotelService;

    @Override
    public User saveUser(User user) {

        //generate unique userId
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        User user= userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with given id is not found on server!! : "+userId));
        //fetch rating of the above user from RATING SERVICE
        Rating[] ratingsOfUser=restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(),Rating[].class);
        ResponseEntity<Rating> ra = restTemplate.getForEntity("http://RATING-SERVICE/ratings/users/" + user.getUserId(), Rating.class);
        List<Rating> ratings = Arrays.stream(ratingsOfUser).collect(Collectors.toList());
        List<Rating> ratingList = ratings.stream().map(rating -> {
            //api call to hote  l service to get the hotel
            //localhost:8092/hotels/3d71df26-5475-4a1f-9362-d996bd48c060
          //  ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/" + rating.getHotelId(), Hotel.class);
            //Hotel hotel = forEntity.getBody();
          Hotel hotel=  hotelService.getHotel(rating.getHotelId());
            rating.setHotel(hotel);
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }
}
