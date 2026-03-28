package org.example.springhibernate2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint to create a user with a profile
    @PostMapping("/createUser")
    public String createUser(@RequestBody UserRequest userRequest) {
        //create user and profile
        User user = new User(userRequest.getUsername(), userRequest.getEmail());
        Profile profile = new Profile(userRequest.getProfileBio(), user);
        user.setProfile(profile);

        //save
        userService.saveUserWithProfile(user);
        return "User and profile created successfully!";
    }

    // Endpoint to get a user by ID
    @GetMapping("/getUser/{id}")
    public User getUser(@PathVariable int id) {
        return userService.findUserById(id);
    }

    @PostMapping("/createPost")
    public String createPost(@RequestBody PostRequest postRequest) {
        // Find the user by ID
        User user = userService.findUserById(postRequest.getUserId());
        if (user == null) {
            return "User not found";
        }
        Post post = new Post(postRequest.getContent(), user);
        userService.savePost(post);

        return "Post created successfully!";
    }

    @GetMapping("/getPosts/{userId}")
    public List<Post> getPosts(@PathVariable int userId) {
        List<Post> posts = userService.findPostsByUserId(userId);

        if (posts == null || posts.isEmpty()) {
            throw new RuntimeException("No posts found for user with ID: " + userId);
        }

        return posts;
    }

}
