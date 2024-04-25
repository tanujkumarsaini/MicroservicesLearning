package com.tks.hotel.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/staffs")
public class StaffController {
    @GetMapping
    public ResponseEntity<List<String>> getAllStaffs(){
        List s1=new ArrayList();
        s1.add("Arnav");
        s1.add("Bhupendra");
        s1.add("Mohit");
        s1.add("Sohit");
        return new ResponseEntity<>(s1, HttpStatus.OK);
    }
}
