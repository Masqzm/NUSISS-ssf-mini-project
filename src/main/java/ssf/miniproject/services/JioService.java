package ssf.miniproject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.miniproject.repo.JioRepo;

@Service
public class JioService {
    @Autowired
    JioRepo jioRepo;    
}
