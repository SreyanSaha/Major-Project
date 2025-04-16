package com.help.service;

import com.help.model.Admin;
import com.help.model.UserAuthData;
import com.help.repository.AdminRepository;
import com.help.repository.UserAuthDataRepository;
import com.help.validation.AdminValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final UserAuthDataRepository userAuthDataRepository;
    private final AdminValidation adminValidation;

    @Autowired
    public AdminService(AdminRepository adminRepository, UserAuthDataRepository userAuthDataRepository, AdminValidation adminValidation) {
        this.adminRepository = adminRepository;
        this.userAuthDataRepository = userAuthDataRepository;
        this.adminValidation = adminValidation;
    }

    public void saveAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin getAdminProfile(String username) {
        return adminRepository.findByUsername(username);
    }
}
