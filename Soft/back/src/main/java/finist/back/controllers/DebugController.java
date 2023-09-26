package finist.back.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("api/debug/hello/")
    public ResponseEntity<String> sayHello(@RequestParam(name = "name") String name,
                                           @AuthenticationPrincipal UserDetails userDetails){
        return new ResponseEntity<>("hello, " + name + "\nyour email: " + userDetails.getUsername(), HttpStatus.OK);

    }

    @GetMapping("/free/")
    public ResponseEntity<String> sayFREEDOOOM(){
        return new ResponseEntity<>("FREEEEEEDOOOOOOOM", HttpStatus.OK);

    }
}
