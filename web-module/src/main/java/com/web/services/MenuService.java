package com.web.services;

import com.common.repository.SiteDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MenuService {
    @Autowired
    private SiteDataRepository siteDataRepo;

    public String getMenuJson() {
        return siteDataRepo.findAll().get(0).getJson();
    }
}
