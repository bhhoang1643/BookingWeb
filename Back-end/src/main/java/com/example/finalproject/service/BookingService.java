package com.example.finalproject.service;

import com.example.finalproject.dto.BookingDTO;
import com.example.finalproject.dto.HairstylistDTO;
import com.example.finalproject.dto.ServiceDTO;
import com.example.finalproject.entity.*;
import com.example.finalproject.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;
    private final AgentRepository agentRepository;
    private final HairstylistRepository hairstylistRepository;

    public BookingService(BookingRepository bookingRepository, CustomerRepository customerRepository, ShopRepository shopRepository, AgentRepository agentRepository, HairstylistRepository hairstylistRepository) {
        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.shopRepository = shopRepository;
        this.agentRepository = agentRepository;
        this.hairstylistRepository = hairstylistRepository;
    }

    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO getBookingById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Booking does not exist!"));
        return convertToDTO(booking);
    }

    public List<BookingDTO> getBookingsByCustomerId(Integer customerId) {
        return bookingRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByShopId(Integer shopId) {
        return bookingRepository.findByShopId(shopId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByAgentId(Integer agentId) {
        return bookingRepository.findByAgent_Id(agentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Customer customer = customerRepository.findById(bookingDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("❌ Customer does not exist!"));

        Shop shop = shopRepository.findById(bookingDTO.getShopId())
                .orElseThrow(() -> new RuntimeException("❌ Shopdoes not exist!"));

        Agent agent = agentRepository.findById(shop.getAgent().getId())
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist from Shop!"));

        Booking booking = new Booking(customer, shop, agent, bookingDTO.getDatetime(),
                bookingDTO.getPaymentStatus(), bookingDTO.getTotalPrice());

        return convertToDTO(bookingRepository.save(booking));
    }

    public BookingDTO updateBooking(Integer id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("❌ Booking does not exist!"));

        if (bookingDTO.getDatetime() != null) booking.setDatetime(bookingDTO.getDatetime());
        if (bookingDTO.getPaymentStatus() != null) booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        if (bookingDTO.getTotalPrice() != null) booking.setTotalPrice(bookingDTO.getTotalPrice());

        return convertToDTO(bookingRepository.save(booking));
    }

    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("❌ Booking does not exist!");
        }
        bookingRepository.deleteById(id);
    }

    public Optional<Booking> getBookingByIdRaw(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Map<String, Object> calculateRevenueByStylist(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        List<Booking> bookings = bookingRepository.findByAgent_Id(agent.getId());

        Map<String, BigDecimal> revenueMap = new HashMap<>();

        for (Booking booking : bookings) {
            if (booking.getBookingDetails().isEmpty()) continue;
            String stylist = booking.getBookingDetails().get(0).getHairstylist().getName();
            revenueMap.put(stylist, revenueMap.getOrDefault(stylist, BigDecimal.ZERO).add(booking.getTotalPrice()));
        }
        return Map.of("labels", revenueMap.keySet(), "data", revenueMap.values());
    }

    public Map<String, Object> calculateRevenueByService(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        List<Booking> bookings = bookingRepository.findByAgent_Id(agent.getId());

        Map<String, BigDecimal> revenueMap = new HashMap<>();

        for (Booking booking : bookings) {
            booking.getBookingDetails().forEach(detail -> {
                String serviceName = detail.getService().getName();
                revenueMap.put(serviceName, revenueMap.getOrDefault(serviceName, BigDecimal.ZERO).add(detail.getService().getPrice()));
            });
        }
        return Map.of("labels", revenueMap.keySet(), "data", revenueMap.values());
    }

    public Map<String, Object> calculateRevenueByShop(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        List<Booking> bookings = bookingRepository.findByAgent_Id(agent.getId());

        Map<String, BigDecimal> revenueMap = new HashMap<>();

        for (Booking booking : bookings) {
            String location = booking.getShop().getLocation();
            revenueMap.put(location, revenueMap.getOrDefault(location, BigDecimal.ZERO).add(booking.getTotalPrice()));
        }
        return Map.of("labels", revenueMap.keySet(), "data", revenueMap.values());
    }

    public Map<String, Object> calculateRevenueByDay(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        List<Booking> bookings = bookingRepository.findByAgent_Id(agent.getId());

        Map<String, BigDecimal> revenueMap = new TreeMap<>();

        for (Booking booking : bookings) {
            String date = booking.getDatetime().toLocalDate().toString();
            revenueMap.put(date, revenueMap.getOrDefault(date, BigDecimal.ZERO).add(booking.getTotalPrice()));
        }
        return Map.of("labels", revenueMap.keySet(), "data", revenueMap.values());
    }

    public Map<String, Object> calculateRevenueByMonth(Integer accountId) {
        Agent agent = agentRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("❌ Agent does not exist!"));

        List<Booking> bookings = bookingRepository.findByAgent_Id(agent.getId());

        Map<String, BigDecimal> revenueMap = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Booking booking : bookings) {
            String month = booking.getDatetime().format(formatter);
            revenueMap.put(month, revenueMap.getOrDefault(month, BigDecimal.ZERO).add(booking.getTotalPrice()));
        }
        return Map.of("labels", revenueMap.keySet(), "data", revenueMap.values());
    }
    public List<HairstylistDTO> getTopBookedStylists(int limit) {
        List<Object[]> results = bookingRepository.findTopHairstylistsByBookingCount();

        return results.stream()
                .limit(limit)
                .map(obj -> {
                    Integer stylistId = (Integer) obj[0];
                    Long bookingCount = (Long) obj[1];

                    Hairstylist stylist = hairstylistRepository.findById(stylistId)
                            .orElseThrow(() -> new RuntimeException("Stylist not found"));

                    return new HairstylistDTO(
                            stylist.getId(),
                            stylist.getShop().getId(),
                            stylist.getName(),
                            stylist.getExperience(),
                            stylist.getSpecialty(),
                            stylist.getImage(),
                            stylist.getShop().getLocation(),
                            stylist.getShop().getAgent().getAgentName()
                    );
                })
                .collect(Collectors.toList());
    }


    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO(
                booking.getBookingId(),
                booking.getCustomer().getId(),
                booking.getShop().getId(),
                booking.getAgent().getId(),
                booking.getDatetime(),
                booking.getPaymentStatus(),
                booking.getTotalPrice()
        );

        dto.setCustomerPhone(booking.getCustomer().getAccount().getPhoneNumber());
        dto.setShopLocation(booking.getShop().getLocation());

        if (booking.getBookingDetails() != null && !booking.getBookingDetails().isEmpty()) {
            dto.setStylistName(booking.getBookingDetails().get(0).getHairstylist().getName());

            List<ServiceDTO> services = booking.getBookingDetails().stream()
                    .map(detail -> {
                        var s = detail.getService();
                        return new ServiceDTO(
                                s.getServiceId(),
                                s.getAgent().getId(),
                                s.getName(),
                                s.getPrice(),
                                s.getStatus(),
                                s.getImage()
                        );
                    })
                    .collect(Collectors.toList());
            dto.setServices(services);
        } else {
            dto.setServices(Collections.emptyList());
        }

        return dto;
    }

}
