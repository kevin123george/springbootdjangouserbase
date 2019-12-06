package com.example.authtry.controller;


import com.example.authtry.exception.ResourceNotFoundException;
import com.example.authtry.model.ApplicationUser;
import com.example.authtry.model.Question;
import com.example.authtry.repository.QuestionRepository;
import com.example.authtry.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;


    @Autowired
    private UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public QuestionController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginParams params, HttpSession session){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(params.getUsername(), params.getPassword());
        try {
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("================== "+authentication.isAuthenticated());
            return new ResponseEntity<>(authentication.getPrincipal(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        session.invalidate();
    }


    @GetMapping("/")
    public Page<Question> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }





    @PostMapping("/questions")
    public Question createQuestion(@Valid @RequestBody Question question) {
        return questionRepository.save(question);
    }

    @PostMapping("/register")
    public void registerUser(@RequestBody ApplicationUser user){
        ApplicationUser checkUser = userRepository.findByUsername(user.getUsername());
        if(checkUser == null){
            userRepository.save(user);

        }
        else{
            System.out.println("sdsds");
        }
    }

    @PutMapping("/questions/{questionId}")
    public Question updateQuestion(@PathVariable Long questionId,
                                   @Valid @RequestBody Question questionRequest) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    question.setTitle(questionRequest.getTitle());
                    question.setDescription(questionRequest.getDescription());
                    return questionRepository.save(question);
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }


    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
        return questionRepository.findById(questionId)
                .map(question -> {
                    questionRepository.delete(question);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + questionId));
    }
}
