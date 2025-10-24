/*package com.ecobazaar.ecobazaar.service;
import com.ecobazaar.ecobazaar.dto.UserReport;
import com.ecobazaar.ecobazaar.model.User;
import com.ecobazaar.ecobazaar.repository.OrderRepository;
import com.ecobazaar.ecobazaar.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserReportService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // Constructor injection (preferred)
    public UserReportService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }



    public UserReport getUserReport(Long userId) {
        // Fetch user data
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch and calculate order stats
        Long totalPurchases = (long) orderRepository.findByUserId(userId).size();
        Double totalSpent = orderRepository.getTotalCarbonByUser(userId);
        Double totalCarbon = orderRepository.getTotalCarbonByUser(userId);

        if (totalSpent == null) totalSpent = 0.0;
        if (totalCarbon == null) totalCarbon = 0.0;

        String badge = getEcoBadge(totalCarbon);

        // Build and return report
        return new UserReport(
                user.getId(),
                user.getName(),
                totalPurchases,
                totalSpent,
                totalCarbon,
                badge
        );
    }

    private String getEcoBadge(Double carbonSaved) {
        if (carbonSaved > 500) return "🌎 Eco Legend";
        else if (carbonSaved > 200) return "🌿 Green Hero";
        else if (carbonSaved > 100) return "🍃 Conscious Shopper";
        else if (carbonSaved > 0) return "🌱 Beginner Eco-Saver";
        else return "🚫 No Impact Yet";
    }
}*/
package com.ecobazaar.ecobazaar.service;
import com.ecobazaar.ecobazaar.dto.UserReport;
import com.ecobazaar.ecobazaar.model.User;
import com.ecobazaar.ecobazaar.repository.OrderRepository;
import com.ecobazaar.ecobazaar.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserReportService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    // Constructor injection (preferred)
    public UserReportService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }


    public UserReport getUserReport(Long userId) {
        // Fetch user data
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch and calculate order stats
        // Finds the number of orders placed by the user
        Long totalPurchases = (long) orderRepository.findByUserId(userId).size(); 
        
        // FIX: Now correctly calls the method that sums the totalPrice (getTotalSpendByUser)
        Double totalSpent = orderRepository.getTotalSpendByUser(userId); 
        
        // This remains correct, calling the method that sums the totalCarbon
        Double totalCarbon = orderRepository.getTotalCarbonByUser(userId); 

        // Handle null values in case the user has no orders or the query returns null
        if (totalSpent == null) totalSpent = 0.0;
        if (totalCarbon == null) totalCarbon = 0.0;

        String badge = getEcoBadge(totalCarbon);

        // Build and return report
        return new UserReport(
                user.getId(),
                user.getName(),
                totalPurchases,
                totalSpent, // Now distinct
                totalCarbon, // Now distinct
                badge
        );
    }

    private String getEcoBadge(Double carbonSaved) {
        if (carbonSaved > 500) return "🌎 Eco Legend";
        else if (carbonSaved > 200) return "🌿 Green Hero";
        else if (carbonSaved > 100) return "🍃 Conscious Shopper";
        else if (carbonSaved > 0) return "🌱 Beginner Eco-Saver";
        else return "🚫 No Impact Yet";
    }
}

