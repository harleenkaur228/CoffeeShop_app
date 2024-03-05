package com.example.cafeshopharleen;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {
    private final UserRepository userRepository;
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }
    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        // Validate input
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("emailError", "Email address is required");
            return "redirect:/signup";
        }

        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("passwordError", "Password is required");
            return "redirect:/signup";
        }

        // Check if the email is already registered
        if (userRepository.findByEmail(user.getEmail()) != null) {
            redirectAttributes.addFlashAttribute("emailError", "Email is already registered");
            return "redirect:/signup";
        }

        // You should use a secure password hashing library in a real-world scenario
        // For simplicity, we're storing plain text password here. Use BCrypt or a similar library.
        user.setPassword(user.getPassword());

        // Save the user to the database
        userRepository.save(user);

        // Redirect to login page after successful signup
        return "redirect:/dashboard";

    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String
            password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/cart/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "index";
        }
    }
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "dashboard";
        } else {
            return "redirect:/";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}

