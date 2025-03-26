package JavaSE8;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Order {
    String orderId;
    String address;
    List<String> products;

    public Order(String orderId, String address, List<String> products) {
        this.orderId = orderId;
        this.address = address;
        this.products = products;
    }
}

class MergedOrder {
    String address;
    List<String> orderIds;
    List<String> allProducts;

    public MergedOrder(String address) {
        this.address = address;
        this.orderIds = new ArrayList<>();
        this.allProducts = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Address: " + address +
                "\nOrder IDs: " + orderIds +
                "\nProducts: " + allProducts + "\n";
    }
}

public class OrderMergerApp {

    public static List<MergedOrder> mergeOrdersByMap(List<Order> orders) {
        Map<String, MergedOrder> mergedMap = new HashMap<>();

        for (Order order : orders) {
            MergedOrder merged = mergedMap.getOrDefault(order.address, new MergedOrder(order.address));
            merged.orderIds.add(order.orderId);
            merged.allProducts.addAll(order.products);
            mergedMap.put(order.address, merged);
        }

        return new ArrayList<>(mergedMap.values());
    }

    public static List<MergedOrder> mergeOrdersByLoop(List<Order> orders) {
        List<MergedOrder> result = new ArrayList<>();
        boolean[] visited = new boolean[orders.size()];

        for (int i = 0; i < orders.size(); i++) {
            if (visited[i]) continue;

            Order current = orders.get(i);
            MergedOrder merged = new MergedOrder(current.address);
            merged.orderIds.add(current.orderId);
            merged.allProducts.addAll(current.products);
            visited[i] = true;

            for (int j = i + 1; j < orders.size(); j++) {
                Order other = orders.get(j);
                if (!visited[j] && current.address.equals(other.address)) {
                    merged.orderIds.add(other.orderId);
                    merged.allProducts.addAll(other.products);
                    visited[j] = true;
                }
            }

            result.add(merged);
        }

        return result;
    }

    public static void main(String[] args) {
        // CASE 1: Tất cả đơn hàng cùng địa chỉ
        List<Order> sameAddressOrders = List.of(
                new Order("O1", "Same Address", List.of("A")),
                new Order("O2", "Same Address", List.of("B")),
                new Order("O3", "Same Address", List.of("C"))
        );

        // CASE 2: Mỗi đơn một địa chỉ
        List<Order> diffAddressOrders = List.of(
                new Order("O4", "Address A", List.of("X")),
                new Order("O5", "Address B", List.of("Y")),
                new Order("O6", "Address C", List.of("Z"))
        );

        // CASE 3: Hỗn hợp
        List<Order> mixedOrders = List.of(
                new Order("O7", "A", List.of("P1")),
                new Order("O8", "B", List.of("P2")),
                new Order("O9", "A", List.of("P3")),
                new Order("O10", "C", List.of("P4")),
                new Order("O11", "D", List.of("P5")),
                new Order("O12", "B", List.of("P6"))
        );

        System.out.println("CASE 1 - TẤT CẢ CÙNG ĐỊA CHỈ (MAP)");
        mergeOrdersByMap(sameAddressOrders).forEach(System.out::println);

        System.out.println("CASE 2 - MỖI ĐƠN 1 ĐỊA CHỈ (MAP)");
        mergeOrdersByMap(diffAddressOrders).forEach(System.out::println);

        System.out.println("CASE 3 - HỖN HỢP (MAP)");
        mergeOrdersByMap(mixedOrders).forEach(System.out::println);

        System.out.println("CASE 3 - HỖN HỢP (LOOP)");
        mergeOrdersByLoop(mixedOrders).forEach(System.out::println);
    }
}
