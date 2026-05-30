package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;  
import org.springframework.web.bind.annotation.*;

import com.example.model.LoginModel;
import com.example.model.common.model.SpBootProModel;
import com.example.model.repository.SpBootProRepository;
import com.example.repository.LoginRepository;


@RestController
@RequestMapping("api")
public class SpBootProRestController {

    @Autowired
    SpBootProRepository spBootProRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("getDetail")
    public List<SpBootProModel> getDetails() {
        return spBootProRepository.getUserData();
    }

    /**
     * Save both SpBootProModel and LoginModel in a single transaction.
     * If anything fails, both will rollback.
     */
    @Transactional
    @PostMapping("savedata")
    public SpBootProModel saveData(@ModelAttribute SpBootProModel spBootProModel) {

        // UPDATE CASE
        if (spBootProModel.getId() != null) {

            SpBootProModel existingUser = spBootProRepository
                    .findById(spBootProModel.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            

            existingUser.setName(spBootProModel.getName());
            existingUser.setEmail(spBootProModel.getEmail());
            existingUser.setUsername(spBootProModel.getUsername());

            return spBootProRepository.save(existingUser);
        }

        // CREATE CASE

        // Save login credentials
        LoginModel loginModel = new LoginModel();
        loginModel.setRole(spBootProModel.getRole().toUpperCase());
        loginModel.setUsername(spBootProModel.getUsername());
        loginModel.setEmail(spBootProModel.getEmail());
        loginModel.setPassword("0");

        loginRepository.save(loginModel);

        // Save user profile
        spBootProModel.setIsdeleted(0L);
        spBootProModel.setUserid(0L);

        return spBootProRepository.save(spBootProModel);
    }

    @GetMapping("getuser/{id}")
    public ResponseEntity<SpBootProModel> getUser(@PathVariable Long id) {
        Optional<SpBootProModel> optSp = spBootProRepository.getUserDetails(id);
        return optSp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("getuserById")
    public SpBootProModel getUserById(Long id) {

        return spBootProRepository.findById(id).orElse(null);
    }
    
    @PostMapping("checkDuplicate")
    @ResponseBody
    public Map<String,Object> getCheckDuplicate(@RequestParam String email) {

    	Map<String,Object> result=new ConcurrentHashMap();
    	Optional<SpBootProModel> sp=spBootProRepository.findByEmail(email);
    	
    	if(sp.isPresent()) {
    		result.put("exist", true);
    		result.put("username", sp.get().getUsername());
    	}else {
    		result.put("exist", false);
    	}
    	System.out.print(spBootProRepository.findByEmail(email).orElse(null));
        return result;
        
    }

    @Transactional
    @PostMapping("deleteuser/{id}")
    public SpBootProModel deleteUser(@PathVariable Long id) {
        SpBootProModel optSp = spBootProRepository.findById(id).orElse(new SpBootProModel());
        optSp.setIsdeleted(1L);
        spBootProRepository.save(optSp);
        return optSp;
    }
}
