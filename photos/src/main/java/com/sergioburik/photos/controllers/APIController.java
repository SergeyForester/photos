package com.sergioburik.photos.controllers;

import com.sergioburik.photos.models.Post;
import com.sergioburik.photos.models.Subscriber;
import com.sergioburik.photos.models.User;
import com.sergioburik.photos.repositories.PostRepository;
import com.sergioburik.photos.repositories.SubscriberRepository;
import com.sergioburik.photos.repositories.UserRepository;
import com.sergioburik.photos.services.PostItemService;
import com.sergioburik.photos.services.PostService;
import com.sergioburik.photos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    PostItemService postItemService;

    @Autowired
    SubscriberRepository subscriberRepository;

    @Value("${upload.path}")
    String uploadPath;


    @GetMapping("/users/{id}")
    public User user(@PathVariable("id") Long id) {
        return userRepository.getOne(id);
    }

    @GetMapping("/users")
    public List<User> users(@RequestParam(name = "username", required = false) Optional<String> username) {

        if (username.isPresent()) {
            return userRepository.findByUsernameContaining(username.get());
        }

        return userRepository.findAll();

    }

    @PostMapping("/subscribers")
    public Subscriber addSubscriber(@RequestParam Long subId, @RequestParam Long userId) throws Exception {
        User sub = userRepository.getOne(subId);
        User user = userRepository.getOne(userId);

        if (subscriberRepository.findByUserAndSubscriber(user, sub).isPresent()) {
            throw new Exception("Subscriber already exists");
        }

        Subscriber subscriber = new Subscriber(user, sub);
        return subscriberRepository.save(subscriber);
    }

    @PostMapping("/posts")
    public Post addPost(@RequestParam String text,
                          @RequestParam MultipartFile image) throws Exception {

        return postService.save(text, image);
    }


}
