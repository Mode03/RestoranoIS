package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.Models.*;

import com.example.RestoranoIS.Repositories.UserRepository;
import com.example.RestoranoIS.Repositories.ClientRepository;
import com.example.RestoranoIS.Repositories.EmployeeRepository;
import com.example.RestoranoIS.Repositories.AdministratorRepository;
import com.example.RestoranoIS.Repositories.CityRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final AdministratorRepository administratorRepository;
    private final CityRepository cityRepository;

    @Autowired
    public UserService(UserRepository userRepository, ClientRepository clientRepository, EmployeeRepository employeeRepository, AdministratorRepository administratorRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.administratorRepository = administratorRepository;
        this.cityRepository = cityRepository;
    }

    public void registerUserWithRole(User user, String userType, String specialKey, String asmensKodas, String pareigos,
                                     String telefonas, String adresas, String slapyvardis, String miestas, Model model) {

        try {

            // 1. Pirmiausia įrašome vartotoją į pagrindinę lentelę "NAUDOTOJAI"
            User savedUser = userRepository.save(user);

            City selectedCity = cityRepository.findBypavadinimas(miestas);

            // 2. Pagal vaidmenį įrašome į papildomas lenteles
            switch (userType.toLowerCase()) {
                case "klientas":
                    Client client = new Client(savedUser.getId(), slapyvardis);
                    clientRepository.save(client);
                    break;

                case "darbuotojas":
                    Employee employee = new Employee(savedUser.getId(), asmensKodas, pareigos, telefonas, adresas, selectedCity);
                    employeeRepository.save(employee);
                    break;

                case "administratorius":
                    Employee adminAsEmployee = new Employee(savedUser.getId(), asmensKodas, pareigos, telefonas, adresas, selectedCity);
                    Administrator administrator = new Administrator(savedUser.getId(), specialKey);

                    // Pirmiausia išsaugome kaip darbuotoją, tada kaip administratorių
                    employeeRepository.save(adminAsEmployee);
                    administratorRepository.save(administrator);
                    break;

                default:
                    throw new IllegalArgumentException("Neteisingas vartotojo tipas!");
            }
        } catch (Exception e) {
            model.addAttribute("error", "Registracijos metu įvyko klaida: " + e.getMessage());
        }

    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public User findByEmail(String elPastas) {
        return userRepository.findByelPastas(elPastas).orElse(null);
    }

    public Client getClientByUserId(Integer userId) {
        return clientRepository.findById(userId).orElse(null); // Grąžina null, jei nėra
    }

    public boolean isAdministrator(Integer userId) {
        return administratorRepository.existsByIdNaudotojas(userId);
    }

    public boolean isClient(Integer userId) {
        return clientRepository.existsByIdNaudotojas(userId);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

}
