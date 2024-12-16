package com.example.RestoranoIS.Services;

import com.example.RestoranoIS.Models.*;
import com.example.RestoranoIS.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentTypeRepository paymentTypeRepository;

    @Autowired
    private StatusTypeRepository statusTypeRepository;

    @Autowired
    private OrderTypeRepository orderTypeRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private OrderDishRepository orderDishRepository;

    public Order createOrder(Integer paymentTypeId, Integer statusTypeId, Integer orderTypeId, Client client) {
        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment type ID"));
        StatusType statusType = statusTypeRepository.findById(statusTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status type ID"));
        OrderType orderType = orderTypeRepository.findById(orderTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order type ID"));

        double calculatedTotalSum = 0;

        Order order = new Order();
        order.setData(LocalDate.now());
        order.setPaymentType(paymentType);
        order.setStatusType(statusType);
        order.setOrderType(orderType);
        order.setBendraSuma(calculatedTotalSum);
        order.setClient(client); // Priskiriame klientą

        return orderRepository.save(order);
    }

    public void addDishToOrder(Order order, Integer dishId, Integer quantity) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dish ID"));

        OrderDish orderDish = new OrderDish();
        orderDish.setOrder(order);
        orderDish.setDish(dish);
        orderDish.setKiekis(quantity);

        orderDishRepository.save(orderDish);
    }

    @Transactional
    public void updateOrder(Integer orderId, Integer paymentTypeId, Integer orderTypeId,
                            Integer statusTypeId, List<Integer> dishIds, List<Integer> quantities) {
        Order order = getOrderById(orderId);

        PaymentType paymentType = paymentTypeRepository.findById(paymentTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment type ID"));
        OrderType orderType = orderTypeRepository.findById(orderTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order type ID"));
        StatusType statusType = statusTypeRepository.findById(statusTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid status type ID"));

        order.setPaymentType(paymentType);
        order.setOrderType(orderType);
        order.setStatusType(statusType);

        // Pašalinkite esamus patiekalus iš užsakymo ir pridėkite naujus
        orderDishRepository.deleteByOrderId(orderId);

        double totalSum = 0;

        if (dishIds != null && quantities != null) {
            for (int i = 0; i < dishIds.size(); i++) {
                addDishToOrder(order, dishIds.get(i), quantities.get(i));

                Dish dish = getDishById(dishIds.get(i));
                totalSum += dish.getKaina() * quantities.get(i);
            }
        }

        order.setBendraSuma(totalSum);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(Integer orderId) {
        // Pirma, pašalina visus susijusius OrderDish įrašus
        orderDishRepository.deleteByOrderId(orderId);

        // Tada pašalina pagrindinį Order įrašą
        orderRepository.deleteById(orderId);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<PaymentType> getAllPaymentTypes() {
        return paymentTypeRepository.findAll();
    }

    public List<StatusType> getAllOrderStatuses() {
        return statusTypeRepository.findAll();
    }

    public List<OrderType> getAllOrderTypes() {
        return orderTypeRepository.findAll();
    }

    public void saveOrder(Order order) {
        // Užsakymo išsaugojimas duomenų bazėje
        orderRepository.save(order);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public Order getOrderById(Integer orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Užsakymas nerastas: ID " + orderId));
    }

    public List<OrderDish> getOrderDishesByOrderId(Integer orderId) {
        return orderDishRepository.findByOrderId(orderId);
    }

    public List<Order> getOrdersByClientId(Integer clientId) {
        return orderRepository.findByClient_idNaudotojas(clientId);
    }

    public List<Order> getFilteredOrders(String startDate, String endDate, Integer paymentTypeId, Integer statusTypeId) {
        List<Order> allOrders = getAllOrders(); // Gaukite visus užsakymus

        return allOrders.stream()
                .filter(order -> {
                    // Filtruoti pagal pradžios ir pabaigos datą
                    boolean matchesDate = true;
                    if (startDate != null && !startDate.isEmpty()) {
                        matchesDate = !order.getData().isBefore(LocalDate.parse(startDate));
                    }
                    if (endDate != null && !endDate.isEmpty()) {
                        matchesDate = matchesDate && !order.getData().isAfter(LocalDate.parse(endDate));
                    }

                    // Filtruoti pagal mokėjimo tipą
                    boolean matchesPayment = paymentTypeId == null || order.getPaymentType().getId().equals(paymentTypeId);

                    // Filtruoti pagal būseną
                    boolean matchesStatus = statusTypeId == null || order.getStatusType().getId().equals(statusTypeId);

                    return matchesDate && matchesPayment && matchesStatus;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getDishSummary(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> getOrderDishesByOrderId(order.getId()).stream())
                .collect(Collectors.groupingBy(
                        orderDish -> orderDish.getDish().getPavadinimas(),
                        Collectors.summingInt(OrderDish::getKiekis)
                ));
    }

    public double getTotalOrderSum(List<Order> orders) {
        return orders.stream()
                .mapToDouble(Order::getBendraSuma)
                .sum();
    }

    public Dish getDishById(Integer dishId) {
        return dishRepository.findById(dishId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid dish ID"));
    }

    public boolean canDeleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return false;
        }

        return !order.getStatusType().getName().equalsIgnoreCase("pabaigta");
    }

}
