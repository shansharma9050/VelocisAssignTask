package com.example.demo.restcontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;  // ✅ import this
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.LoginModel;
import com.example.demo.model.SpBootProModel;
import com.example.demo.repository.LoginRepository;
import com.example.demo.repository.SpBootProRepository;

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
    public SpBootProModel saveData(SpBootProModel spBootProModel) {
        LoginModel loginModel = new LoginModel();

        if (spBootProModel.getId() != null) {
            SpBootProModel spBootProModel1 = spBootProRepository
                    .findById(spBootProModel.getId())
                    .orElse(new SpBootProModel());

            spBootProModel1.setName(spBootProModel.getName());
            spBootProModel1.setEmail(spBootProModel.getEmail());
            spBootProModel1.setUsername(spBootProModel.getUsername());

            return spBootProRepository.save(spBootProModel1);
        }

        // Create Login entry
        loginModel.setRole(spBootProModel.getRole());
        loginModel.setUsername(spBootProModel.getUsername());
        loginModel.setPassword(this.passwordEncoder.encode(spBootProModel.getPassword()));
        loginRepository.save(loginModel);

        // Create user profile
        spBootProModel.setIsdeleted(0L);
        return spBootProRepository.save(spBootProModel);
    }

    @GetMapping("getuser/{id}")
    public ResponseEntity<SpBootProModel> getUser(@PathVariable Long id) {
        Optional<SpBootProModel> optSp = spBootProRepository.getUserDetails(id);
        return optSp.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PostMapping("deleteuser/{id}")
    public SpBootProModel deleteUser(@PathVariable Long id) {
        SpBootProModel optSp = spBootProRepository.findById(id).orElse(new SpBootProModel());
        optSp.setIsdeleted(1L);
        spBootProRepository.save(optSp);

        // Example: If you add some other DB operation here and it fails,
        // the delete will rollback too.
        return optSp;
    }
}
