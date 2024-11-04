package com.example.RestoranoIS.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    @GetMapping("orders")
    public String showOrders() {
        return "Orders/orders";
    }

    @GetMapping("/view-order")
    public String viewOrder() {
        return "Orders/view-order";
    }

    @GetMapping("/edit-order")
    public String editOrder() {
        return "Orders/edit-order";
    }

    @PostMapping("/edit-order")
    public String processEditOrder() {
        return "Orders/edit-order";
    }

    @GetMapping("/delete-order")
    public String deleteOrder() {
        return "Orders/delete-order";
    }
    @PostMapping("/delete-order")
    public String processDeleteOrder() {
        return "redirect:/orders";
    }

    @GetMapping("/generate-report")
    public String showGenerateReportPage() {
        return "Orders/generate-report";
    }

    @GetMapping("/report")
    public String showReportPage() {
        return "Orders/report";
    }
}
