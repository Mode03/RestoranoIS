package com.example.RestoranoIS.Controllers;

import com.example.RestoranoIS.Models.Client;
import com.example.RestoranoIS.Models.Dish;
import com.example.RestoranoIS.Models.Order;
import com.example.RestoranoIS.Models.User;
import com.example.RestoranoIS.Services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.RestoranoIS.Services.OrderService;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("orders")
    public String showOrders(Model model, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Integer userId = loggedInUser.getId();

        boolean isAdmin = userService.isAdministrator(userId);
        model.addAttribute("isAdmin", isAdmin);

        // Patikriname, ar naudotojas yra administratorius
        if (userService.isAdministrator(userId)) {
            // Jei administratorius, rodomi visi užsakymai
            List<Order> orders = orderService.getAllOrders();
            model.addAttribute("orders", orders);
        } else if (userService.isClient(userId)) {
            // Jei klientas, rodomi tik jo užsakymai
            List<Order> orders = orderService.getOrdersByClientId(userId);
            model.addAttribute("orders", orders);
        } else {
            // Jei naudotojas nėra nei administratorius, nei klientas
            return "redirect:/access-denied";
        }

        return "Orders/orders";
    }

    @GetMapping("/create-order")
    public String showCreateOrderForm(Model model, HttpSession session) throws JsonProcessingException {
        // Patikriname, ar vartotojas prisijungęs
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Peradresuojame, jei vartotojas neprisijungęs
        }

        // Patikriname, ar vartotojas yra administratorius
        boolean isAdmin = userService.isAdministrator(loggedInUser.getId());
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            // Jei administratorius, perduodame visus klientus pasirinkimui
            List<Client> clients = userService.getAllClients();
            model.addAttribute("clients", clients);
        } else {
            // Jei klientas, pridedame tik jo informaciją
            Client client = userService.getClientByUserId(loggedInUser.getId());
            if (client == null) {
                client = new Client(loggedInUser.getId(), loggedInUser.getVardas());
            }
            model.addAttribute("client", client);
        }

        ObjectMapper mapper = new ObjectMapper();
        List<Dish> dishes = orderService.getAllDishes();
        model.addAttribute("dishesJson", mapper.writeValueAsString(dishes));

        // Perduodame duomenis į modelį ir formą
        model.addAttribute("paymentTypes", orderService.getAllPaymentTypes());
        model.addAttribute("orderStatuses", orderService.getAllOrderStatuses());
        model.addAttribute("orderTypes", orderService.getAllOrderTypes());
        model.addAttribute("dishes", orderService.getAllDishes());

        System.out.println("AAAAAAAA");

        return "Orders/create-order";
    }

    @PostMapping("/create-order")
    public String createOrder(@RequestParam Integer paymentTypeId,
                              @RequestParam Integer orderTypeId,
                              @RequestParam(required = false) Integer clientId, // Pasirinktas kliento ID (tik administratoriui)
                              @RequestParam List<Integer> dishIds, // Patiekalo ID sąrašas
                              @RequestParam List<Integer> quantities, // Patiekalo kiekiai
                              HttpSession session) {
        // Patikriname, ar vartotojas prisijungęs
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Client client;
        if (userService.isAdministrator(loggedInUser.getId()) && clientId != null) {
            // Administratorius pasirinko klientą
            client = userService.getClientByUserId(clientId);
            if (client == null) {
                throw new IllegalArgumentException("Pasirinktas klientas neegzistuoja.");
            }
        } else {
            // Klientas kuria užsakymą pats
            client = userService.getClientByUserId(loggedInUser.getId());
            if (client == null) {
                client = new Client(loggedInUser.getId(), loggedInUser.getVardas());
            }
        }

        // Sukuriame užsakymą
        Order order = orderService.createOrder(paymentTypeId, 1, orderTypeId, client);

        double totalSum = 0;

        // Pridedame pasirinktus patiekalus prie užsakymo
        for (int i = 0; i < dishIds.size(); i++) {
            Integer dishId = dishIds.get(i);
            Integer quantity = quantities.get(i);

            Dish dish = orderService.getDishById(dishId);
            totalSum += dish.getKaina() * quantity;

            // Pridedame patiekalą prie užsakymo
            orderService.addDishToOrder(order, dishId, quantity);
        }

        // Atnaujinkite užsakymą su bendra suma
        order.setBendraSuma(totalSum);
        orderService.saveOrder(order);

        return "redirect:/orders"; // Nukreipiame į užsakymų sąrašą
    }

    @GetMapping("/view-order")
    public String viewOrder(@RequestParam("id") Integer orderId, Model model) {
        Order order = orderService.getOrderById(orderId);
        model.addAttribute("order", order);
        model.addAttribute("orderDishes", orderService.getOrderDishesByOrderId(orderId));
        return "Orders/view-order"; // Sukurti HTML šabloną užsakymo peržiūrai
    }

    @GetMapping("/edit-order")
    public String editOrder(@RequestParam("id") Integer orderId, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Dish> dishes = orderService.getAllDishes();
        model.addAttribute("dishesJson", mapper.writeValueAsString(dishes));


        Order order = orderService.getOrderById(orderId);
        model.addAttribute("dishes", dishes);
        model.addAttribute("order", order);
        model.addAttribute("orderDishes", orderService.getOrderDishesByOrderId(orderId));
        model.addAttribute("paymentTypes", orderService.getAllPaymentTypes());
        model.addAttribute("orderTypes", orderService.getAllOrderTypes());
        return "Orders/edit-order";
    }

    @PostMapping("/edit-order")
    public String processEditOrder(@RequestParam("id") Integer orderId,
                                   @RequestParam Integer paymentTypeId,
                                   @RequestParam Integer orderTypeId,
                                   @RequestParam(required = false) List<Integer> dishIds,
                                   @RequestParam(required = false) List<Integer> quantities,
                                   @RequestParam("date") String date) {
        orderService.updateOrder(orderId, paymentTypeId, orderTypeId, dishIds, quantities);

        Order order = orderService.getOrderById(orderId);
        order.setData(LocalDate.parse(date));
        orderService.saveOrder(order);

        return "redirect:/orders";
    }


    @GetMapping("/delete-order")
    public String deleteOrder(@RequestParam("id") Integer orderId, Model model) {
        // Get the order for confirmation
        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            model.addAttribute("order", order);
        }
        return "Orders/delete-order"; // Confirmation page
    }
    @PostMapping("/delete-order")
    public String processDeleteOrder(@RequestParam("orderId") Integer orderId) {
        // Pašalina užsakymą ir susijusius OrderDish įrašus
        orderService.deleteOrder(orderId);
        return "redirect:/orders"; // Nukreipia atgal į užsakymų sąrašą
    }


    @GetMapping("/generate-report")
    public String showGenerateReportPage(Model model) {
        model.addAttribute("paymentTypes", orderService.getAllPaymentTypes());
        model.addAttribute("statusTypes", orderService.getAllOrderStatuses());
        return "Orders/generate-report";
    }

    @GetMapping("/report")
    public String generateReport(@RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 @RequestParam(required = false) Integer paymentType,
                                 @RequestParam(required = false) Integer statusType,
                                 Model model) {
        List<Order> filteredOrders = orderService.getFilteredOrders(startDate, endDate, paymentType, statusType);

        // Patiekalų statistika ir viso suma
        Map<String, Integer> dishSummary = orderService.getDishSummary(filteredOrders);
        double totalOrderSum = orderService.getTotalOrderSum(filteredOrders);

        // Perduoti duomenis šablonui
        model.addAttribute("filteredOrders", filteredOrders);
        model.addAttribute("dishSummary", dishSummary);
        model.addAttribute("totalOrderSum", totalOrderSum);

        return "Orders/report";
    }
}
