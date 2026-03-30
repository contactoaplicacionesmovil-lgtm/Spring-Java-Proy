package org.example.finalspringproject.service;

import org.example.finalspringproject.model.Customer;
import org.example.finalspringproject.model.Inventory;
import org.example.finalspringproject.model.Order;
import org.example.finalspringproject.model.OrderDetail;
import org.example.finalspringproject.model.Product;
import org.example.finalspringproject.model.Store;
import org.example.finalspringproject.repository.CustomerRepository;
import org.example.finalspringproject.repository.InventoryRepository;
import org.example.finalspringproject.repository.OrderDetailRepository;
import org.example.finalspringproject.repository.OrderRepository;
import org.example.finalspringproject.repository.ProductRepository;
import org.example.finalspringproject.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            StoreRepository storeRepository,
            OrderDetailRepository orderDetailRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.storeRepository = storeRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public Order createOrder(Order order) {
        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one order detail");
        }

        Customer customer = getOrCreateCustomerByEmail(order.getCustomer());
        Store store = getStoreFromPayload(order);

        order.setCustomer(customer);
        order.setStore(store);
        order.setDate(LocalDateTime.now());
        order.setTotalPrice(0.0);

        Order savedOrder = orderRepository.save(order);
        List<OrderDetail> savedDetails = processPurchaseDetails(savedOrder, order.getOrderDetails(), store);
        savedOrder.setOrderDetails(savedDetails);
        savedOrder.setTotalPrice(calculateTotalPrice(savedDetails));

        return orderRepository.save(savedOrder);
    }

    public OrderDetail saveOrderDetail(Long orderId, OrderDetail orderDetail) {
        Order order = getOrderById(orderId);
        validateQuantity(orderDetail.getQuantity());
        Product product = getProductFromPayload(orderDetail);

        Inventory inventory = inventoryRepository
                .findByProductIdAndStoreId(product.getId(), order.getStore().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Inventory not found for product id " + product.getId() + " in store id " + order.getStore().getId()));

        if (inventory.getStockLevel() < orderDetail.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock for product id " + product.getId() + ". Available: "
                            + inventory.getStockLevel() + ", requested: " + orderDetail.getQuantity());
        }

        inventory.setStockLevel(inventory.getStockLevel() - orderDetail.getQuantity());
        inventoryRepository.save(inventory);

        orderDetail.setOrder(order);
        orderDetail.setProduct(product);
        orderDetail.setPrice(product.getPrice());
        orderDetail.setDate(LocalDateTime.now());

        OrderDetail savedDetail = orderDetailRepository.save(orderDetail);

        List<OrderDetail> details = order.getOrderDetails();
        if (details == null) {
            details = new ArrayList<>();
            order.setOrderDetails(details);
        }
        details.add(savedDetail);
        order.setTotalPrice(calculateTotalPrice(details));
        orderRepository.save(order);

        return savedDetail;
    }

    private Customer getOrCreateCustomerByEmail(Customer payloadCustomer) {
        if (payloadCustomer == null) {
            throw new IllegalArgumentException("Customer data is required");
        }
        if (payloadCustomer.getEmail() == null || payloadCustomer.getEmail().isBlank()) {
            throw new IllegalArgumentException("Customer email is required");
        }

        return customerRepository.findByEmail(payloadCustomer.getEmail())
                .orElseGet(() -> customerRepository.save(buildCustomerForCreate(payloadCustomer)));
    }

    private Customer buildCustomerForCreate(Customer payloadCustomer) {
        if (payloadCustomer.getName() == null || payloadCustomer.getName().isBlank()) {
            throw new IllegalArgumentException("Customer name is required to create a new customer");
        }
        if (payloadCustomer.getPhone() == null || payloadCustomer.getPhone().isBlank()) {
            throw new IllegalArgumentException("Customer phone is required to create a new customer");
        }

        Customer customer = new Customer();
        customer.setName(payloadCustomer.getName());
        customer.setEmail(payloadCustomer.getEmail());
        customer.setPhone(payloadCustomer.getPhone());
        return customer;
    }

    private Store getStoreFromPayload(Order order) {
        if (order.getStore() == null || order.getStore().getId() <= 0) {
            throw new IllegalArgumentException("Store id is required");
        }

        Long storeId = order.getStore().getId();
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Store not found with id: " + storeId));
    }

    private Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    private Product getProductFromPayload(OrderDetail orderDetail) {
        if (orderDetail.getProduct() == null || orderDetail.getProduct().getId() <= 0) {
            throw new IllegalArgumentException("Product id is required");
        }

        Long productId = orderDetail.getProduct().getId();
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }

    private List<OrderDetail> processPurchaseDetails(Order order, List<OrderDetail> payloadDetails, Store store) {
        List<OrderDetail> detailsToSave = new ArrayList<>();

        for (OrderDetail payloadDetail : payloadDetails) {
            validateQuantity(payloadDetail.getQuantity());
            Product product = getProductFromPayload(payloadDetail);
            Inventory inventory = inventoryRepository
                    .findByProductIdAndStoreId(product.getId(), store.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Inventory not found for product id " + product.getId() + " in store id " + store.getId()));

            if (inventory.getStockLevel() < payloadDetail.getQuantity()) {
                throw new IllegalArgumentException(
                        "Insufficient stock for product id " + product.getId() + ". Available: "
                                + inventory.getStockLevel() + ", requested: " + payloadDetail.getQuantity());
            }

            inventory.setStockLevel(inventory.getStockLevel() - payloadDetail.getQuantity());
            inventoryRepository.save(inventory);

            OrderDetail detailToSave = new OrderDetail();
            detailToSave.setOrder(order);
            detailToSave.setProduct(product);
            detailToSave.setQuantity(payloadDetail.getQuantity());
            detailToSave.setPrice(product.getPrice());
            detailToSave.setDate(LocalDateTime.now());
            detailsToSave.add(detailToSave);
        }

        return orderDetailRepository.saveAll(detailsToSave);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    private double calculateTotalPrice(List<OrderDetail> details) {
        double total = 0.0;
        for (OrderDetail detail : details) {
            total += detail.getPrice() * detail.getQuantity();
        }
        return total;
    }
}
