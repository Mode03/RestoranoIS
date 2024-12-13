package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.*;
import com.example.RestoranoIS.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.hibernate.jdbc.Expectation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class StorageController {
    private final UserService userService;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private StorageRepository storageRepository;
    @Autowired private StorageTypeRepository storageTypeRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProductTypeRepository productTypeRepository;
    @Autowired private StorageProductsRepository storageProductsRepository;
    @Autowired private CityRepository cityRepository;
    private int productAmountWarning = 10;
    @Autowired
    public StorageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/products")
    public String listProducts(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        List<Product> products = productRepository.findAll();
        model.addAttribute("products", products);

        List<Product> lowProducts = new ArrayList<>();
        for(Product product: products){
            if(product.getKiekis() < productAmountWarning){
                lowProducts.add(product);
            }
        }
        model.addAttribute("lowProducts", lowProducts);
        if(!lowProducts.isEmpty()){
            model.addAttribute("showLow", true);
        }
        return "Storage/products";
    }

    @GetMapping("/products/storage")
    public String listStorages(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        List<Storage> storages = storageRepository.findAll();
        model.addAttribute("storages", storages);
        return "Storage/storages";
    }

    @GetMapping("/products/storage/create")
    public String createStorage(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        //if (!isAdmin) {
        //    return "redirect:/access-denied";
        //}
        
        Storage storage = new Storage();
        List<City> cities = cityRepository.findAll();
        List<Employee> employeesAll = employeeRepository.findAll();
        List<StorageType> storageTypes = storageTypeRepository.findAll();
        List<Employee> employees = new ArrayList<>();
        for(Employee empl : employeesAll){
            if(userService.isEmployee(empl.getIdNaudotojas())){
                employees.add(empl);
            }
        }
        model.addAttribute("cities", cities);
        model.addAttribute("storage", storage);
        model.addAttribute("employees", employees);
        model.addAttribute("storageTypes", storageTypes);
        return "Storage/create-storage";
    }

    @PostMapping("/products/storage/create/submit")
    @Transactional
    public String submitCreatedStorage(@ModelAttribute("storage") Storage storage,
                                       Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        //if (!isAdmin) {
        //    return "redirect:/access-denied";
        //}
        
        storageRepository.save(storage);
        return "redirect:/products/storage";
    }

    @GetMapping("/products/create")
    public String createProducts(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        Product product = new Product();
        List<ProductType> productTypes = productTypeRepository.findAll();
        model.addAttribute("product", product);
        model.addAttribute("productTypes", productTypes);
        return "Storage/create-products";
    }

    @PostMapping("/products/create/submit")
    @Transactional
    public String submitCreateProducts(@ModelAttribute("product") Product product, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        product.setKiekis(0);
        productRepository.save(product);
        return "redirect:/products";
    }

    @GetMapping("/products/order/{id}")
    public String orderProducts(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        Optional<Product> optional = productRepository.findById(id);
        if(optional.isEmpty()) throw new IllegalArgumentException("Product not found with ID: " + id);

        Product product = optional.get();
        model.addAttribute("product", product);
        return "Storage/order-products";
    }

    @PostMapping("/products/order/save/{id}")
    public String orderProducts(@RequestParam("uzsakyti") int uzsakyti,
                                @PathVariable int id,
                                Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        Optional<Product> optional = productRepository.findById(id);
        if(optional.isEmpty()) throw new IllegalArgumentException("Product not found with ID: " + id);
        Product product = optional.get();
        ProductType pType = product.getType();
        StorageType sType = new StorageType();
        sType.setName("sausas");
        Set<String> validTypes = Set.of("darzove", "mesa", "juros_gerybe", "pieno");
        if (validTypes.contains(pType.getName())) {
            sType.setName("saltas");
        }

        if(product.getKiekis() == 0){
            List<Storage> storages = storageRepository.findAll();
            Storage selectedStorage = new Storage();
            selectedStorage.setTalpa(-1);
            for(Storage storage: storages){
                int storageId = storage.getSandelioNr();
                if(!storage.getType().getName().equals(sType.getName())){
                    continue;
                }
                StorageType type = storage.getType();

                int spaceTaken = 0;
                List<StorageProducts> storageProducts = storageProductsRepository.getStorageProductsByStorage(storageId);
                for(StorageProducts stProd: storageProducts){
                    spaceTaken += stProd.getProduct().getKiekis();
                }
                if(storage.getTalpa() - spaceTaken > uzsakyti){
                    selectedStorage = storage;
                    break;
                }
            }
            if(selectedStorage.getTalpa() == -1){
                model.addAttribute("error", "Nėra sandelių, su pakankamai laisvos vietos!");
                model.addAttribute("product", product);
                return "Storage/order-products";
            }
            product.setKiekis(product.getKiekis()+uzsakyti);
            productRepository.save(product);
            storageProductsRepository.insertStorageProduct(selectedStorage.getSandelioNr(), product.getId());
        }
        else{
            StorageProducts stProduct = storageProductsRepository.getStorageProductsByProduct(product.getId());
            Storage storage = stProduct.getStorage();
            List<StorageProducts> storageProducts = storageProductsRepository.getStorageProductsByStorage(storage.getSandelioNr());
            int spaceTaken = 0;
            for(StorageProducts stProd: storageProducts){
                spaceTaken += stProd.getProduct().getKiekis();
            }
            if(storage.getTalpa() - spaceTaken > uzsakyti){
                product.setKiekis(product.getKiekis()+uzsakyti);
                productRepository.save(product);
            }
            else{
                model.addAttribute("error", "Produkto sandelyje nėra vietos");
                model.addAttribute("product", product);
                return "Storage/order-products";
            }
        }

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProducts(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
//        if (!isEmployee) {
//            return "redirect:/access-denied";
//        }
        
        storageProductsRepository.deleteByProduct(id);
        productRepository.deleteProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/products/categories")
    public String categoriesProducts(Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        return "Storage/categories-products";
    }

    @GetMapping("/products/categories/{type}")
    public String categoriesProductsList(@PathVariable("type") String input, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();

        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}
        
        List<Product> products = new ArrayList<>();
        List<ProductType> types = productTypeRepository.findAll();
        ProductType selectedType = new ProductType();
        boolean found = false;
        for (ProductType type: types){
            if (type.getName().equals(input)){
                selectedType = type;
                found = true;
                break;
            }
        }
        if(!found) return "Storage/categories-products";
        List<Product> allProducts = productRepository.findAll();
        //TODO: CIA SIAIP GALIMA PAKEISTI SU SQL UZKLAUSA WHERE TYPE.NAME = product.TYPE
        for (Product product : allProducts){
            if(product.getType() == selectedType){
                products.add(product);
            }
        }
        model.addAttribute("products", products);
        return "Storage/products";
    }

    @GetMapping("/products/delete-confirm/{id}")
    public String confirmDelete(@PathVariable int id, Model model, HttpSession session){
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }
        Integer userId = loggedInUser.getId();
        
        boolean isEmployee = userService.isEmployee(userId);
        //if (!isEmployee) {
        //    return "redirect:/access-denied";
        //}

        Optional<Product> optional = productRepository.findById(id);
        if(optional.isEmpty()) throw new IllegalArgumentException("Product not found with ID: " + id);
        Product product = optional.get();
        model.addAttribute("product",product);
        return "Storage/delete-products";
    }

}
