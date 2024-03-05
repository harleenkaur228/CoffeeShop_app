package com.example.cafeshopharleen;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/cart")
class CartController {
    @GetMapping("/")
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cartItems = (List<CartItem>)
                session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", calculateTotal(cartItems));
        return "cart";
    }
    @PostMapping("/add")
    public String addToCart(@RequestParam String item, @RequestParam double price, @RequestParam String image, HttpSession session) {
        List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            session.setAttribute("cartItems", cartItems);
        }

        // Check if the item already exists in the cart
        boolean itemExists = false;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getName().equals(item)) {
                cartItem.setQuantity(cartItem.getQuantity() + 1); // Increment quantity
                itemExists = true;
                break;
            }
        }

        // If the item doesn't exist, add it to the cart with quantity 1
        if (!itemExists) {
            cartItems.add(new CartItem(item, price, image));
        }

        return "redirect:/cart/";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam int index, HttpSession
            session) {
        List<CartItem> cartItems = (List<CartItem>)
                session.getAttribute("cartItems");
        if (cartItems != null && index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
        return "redirect:/cart/";
    }
    private double calculateTotal(List<CartItem> cartItems) {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    @GetMapping("/item")
    public String itemCart(HttpSession session, Model model) {
        List<CartItem> cartItems = (List<CartItem>)
                session.getAttribute("cartItems");
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", calculateTotal(cartItems));
        return "cart-item";
    }
}
