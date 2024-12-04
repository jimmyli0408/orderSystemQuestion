import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    interface IOrder {
        String getName();
        void setName(String name);
        void setPrice(int price);
        int getPrice();
    }

    interface IOrderSystem{
        void insertPackage(IOrder order);
        void removePackageFromCart(IOrder order);
        int getTotalCosts();
        Map<String, Integer> discountCategory();
        Map<String, Integer> cartItems();

    }

    class Order implements IOrder {

        private String name;
        private int price;


        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setPrice(int price) {
            this.price = price;
        }


        @Override
        public int getPrice() {
            return this.price;
        }
    }

    class Cart {
        List<IOrder> orders;

        public Cart() {
            this.orders = new ArrayList<>();
        }

        public List<IOrder> getOrders() {
            return this.orders;
        }

    }

    class OrderSystem implements IOrderSystem{

        private Map<String, Integer> discountMap;
        private Map<String, Integer> cartItemsMap;
        Cart cart;

        @Override
        public void insertPackage(IOrder order) {
            if (cart == null) {
                cart = new Cart();
            }
            List<IOrder> orders = cart.getOrders();
            orders.add(order);
            int quant = cartItems().getOrDefault(order.getName(), 0);
            cartItems().put(order.getName(), quant+1);
        }

        @Override
        public void removePackageFromCart(IOrder order) {
            List<IOrder> orders = cart.getOrders();
            for (IOrder curOrder : orders) {
                if (curOrder.getName().equals(order.getName())) {
                    orders.remove(curOrder);
                    break;
                }
            }

            int quant = cartItems().get(order.getName());
            if (quant > 0) {
                cartItems().put(order.getName(), quant-1);
            }
        }

        @Override
        public int getTotalCosts() {

            List<IOrder> orders = cart.getOrders();
            int totalPrice = 0;
            for (IOrder order: orders) {
                int discountRate = getDiscountRate(order.getPrice());
                int discountedPrice = calculateDiscountedPrice(discountRate, order.getPrice());
                totalPrice += discountedPrice;
            }
            return totalPrice;
        }

        private int calculateDiscountedPrice(int discountRate, int initialPrice) {
            double discount = (double) (100 - discountRate)/100;
            int price = (int) Math.round(initialPrice * discount);
            return price;
        }

        private int getDiscountRate(int orderPrice) {

            if (orderPrice <= 10) {
                return discountCategory().get("Cheap");
            }

            if(orderPrice < 20) {
                return discountCategory().get("Moderate");
            }

            return discountCategory().get("Expensive");
        }

        @Override
        public Map<String, Integer> discountCategory() {
            if (discountMap == null) {
                discountMap = new HashMap<>();
            }
            discountMap = new HashMap<>();
            discountMap.put("Cheap", 10);
            discountMap.put("Moderate", 20);
            discountMap.put("Expensive", 30);
            return discountMap;
        }

        @Override
        public Map<String, Integer> cartItems() {
            if (cartItemsMap == null) {
                cartItemsMap = new HashMap<>();
            }
            return cartItemsMap;
        }
    }

}

class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();

        // Create some orders
        Solution.IOrder order1 = solution.new Order();
        Solution.IOrder order2 = solution.new Order();
        Solution.IOrder order3 = solution.new Order();
        Solution.IOrder order4 = solution.new Order();

        // Set names and prices
        order1.setName("Pizza");
        order1.setPrice(98); // Expensive -> 30% discount
        order2.setName("Coke");
        order2.setPrice(9); // Cheap -> 10% discount
        order3.setName("Chicken");
        order3.setPrice(15); // Moderate -> 20% discount
        order4.setName("Burger");
        order4.setPrice(18); // Moderate -> 20% discount

        // Initialize the OrderSystem
        Solution.IOrderSystem orderSystem = solution.new OrderSystem();

        // Insert orders into cart
        System.out.println("Inserting items into cart...");
        orderSystem.insertPackage(order1);
        orderSystem.insertPackage(order2);
        orderSystem.insertPackage(order3);
        orderSystem.insertPackage(order4);

        // Check cart items
        System.out.println("\nCart Items After Insertion:");
        printCartItems(orderSystem);

        // Get total costs
        System.out.println("\nTotal Cost After Inserting Items:");
        int totalCost = orderSystem.getTotalCosts();
        System.out.println("Total Cost: " + totalCost);

        // Remove an item from the cart
        System.out.println("\nRemoving 'Coke' from the cart...");
        orderSystem.removePackageFromCart(order2);

        // Check cart items after removal
        System.out.println("\nCart Items After Removing 'Coke':");
        printCartItems(orderSystem);

        // Get total cost after removing item
        System.out.println("\nTotal Cost After Removing 'Coke':");
        totalCost = orderSystem.getTotalCosts();
        System.out.println("Total Cost: " + totalCost);

        // Add 'Pizza' again to check incrementing quantity
        System.out.println("\nAdding 'Pizza' again to the cart...");
        orderSystem.insertPackage(order1);

        // Check cart items after adding 'Pizza' again
        System.out.println("\nCart Items After Adding 'Pizza' Again:");
        printCartItems(orderSystem);

        // Get final total cost
        System.out.println("\nFinal Total Cost After Adding 'Pizza' Again:");
        totalCost = orderSystem.getTotalCosts();
        System.out.println("Total Cost: " + totalCost);
    }

    private static void printCartItems(Solution.IOrderSystem orderSystem) {
        for (Map.Entry<String, Integer> cartItem : orderSystem.cartItems().entrySet()) {
            System.out.println("Item: " + cartItem.getKey() + ", Quantity: " + cartItem.getValue());
        }
    }
}
