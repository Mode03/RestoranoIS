package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.Repositories.CustomerTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    @Autowired
    private CustomerTableRepository customerTableRepository;
}
