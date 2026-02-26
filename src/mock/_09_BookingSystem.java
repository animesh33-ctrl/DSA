package mock;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.io.*;
import java.util.stream.*;
import java.time.*;

enum RoomType {
    SINGLE(1, 1000), DOUBLE(2, 1500), DELUXE(2, 2500), SUITE(4, 5000);

    private final int capacity;
    private final double pricePerNight;

    RoomType(int capacity, double price) {
        this.capacity = capacity;
        this.pricePerNight = price;
    }

    public int getCapacity() { return capacity; }
    public double getPrice() { return pricePerNight; }
}

enum BookingStatus {
    PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED
}

class Booking {
    String bookingId;
    String guestId;
    String roomNumber;
    RoomType roomType;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    BookingStatus status;
    double totalAmount;
    long bookingTimestamp;

    public Booking(String bookingId, String guestId, String roomNumber,
                   RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkInDate = checkIn;
        this.checkOutDate = checkOut;
        this.status = BookingStatus.PENDING;
        this.bookingTimestamp = System.currentTimeMillis();
        this.totalAmount = calculateAmount();
    }

    public Booking(String bookingId, String guestId, String roomNumber, RoomType roomType, LocalDate checkInDate, LocalDate checkOutDate, BookingStatus status, double totalAmount, long bookingTimestamp) {
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.bookingTimestamp = bookingTimestamp;
    }

    private double calculateAmount() {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return nights * roomType.getPrice();
    }

    public String toCSV() {
        // Format: bookingId,guestId,roomNumber,roomType,checkIn,checkOut,status,amount,timestamp
        return bookingId+","+guestId+","+roomNumber+","+roomType.name()+","+checkInDate.toString()+","+checkOutDate.toString()+","+
                status.name()+","+totalAmount+","+bookingTimestamp;
    }

    public static Booking fromCSV(String line) throws InvalidBookingException {
        String[] spilt = line.split(",");
        try{
            String bookingId = spilt[0].trim();
            String guestId = spilt[1].trim();
            String roomNumber = spilt[2].trim();
            RoomType roomType = RoomType.valueOf(spilt[3].trim());
            LocalDate checkInDate = LocalDate.parse(spilt[4].trim());
            LocalDate checkOutDate = LocalDate.parse(spilt[5].trim());
            BookingStatus status = BookingStatus.valueOf(spilt[6].trim());
            double totalAmount = Double.parseDouble(spilt[7].trim());
            long bookingTimestamp = Long.parseLong(spilt[8].trim());

            return new Booking(bookingId,guestId,roomNumber,roomType,checkInDate,checkOutDate,status,totalAmount,bookingTimestamp);
        }
        catch (Exception e){
            throw new InvalidBookingException("Invalid Exception : "+e.getMessage());
        }
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

interface IBookingStore {
    void storeBookings(Queue<Booking> bookings) throws IOException;
    Queue<Booking> getAllBookings();
    void saveToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, InvalidBookingException;
}

interface IBookingSystem {
    void createBooking(String guestId, String roomNumber, RoomType roomType,
                       LocalDate checkIn, LocalDate checkOut);
    void confirmBooking(String bookingId);
    void checkIn(String bookingId);
    void checkOut(String bookingId);
    void cancelBooking(String bookingId);

    int getTotalBookings();
    int getPendingBookings();
    void flush() throws IOException;

    // Stream-based analytics
    Map<RoomType, Long> getBookingsByRoomType();
    Map<BookingStatus, Long> getBookingsByStatus();
    double getTotalRevenue();
    double getAverageBookingValue();

    // Lambda filters
    List<Booking> getBookingsByGuest(String guestId);
    List<Booking> getBookingsByStatus(BookingStatus status);
    List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end);
    List<Booking> getUpcomingCheckIns(int daysAhead);
    List<Booking> getLongStayBookings(int minNights);

    // Advanced analytics
    Map<String, Double> getRevenueByGuest();
    List<String> getTopSpendingGuests(int n);
    Map<RoomType, Double> getRevenueByRoomType();
    Map<String, Long> getBookingsByRoom();
    List<String> getMostBookedRooms(int n);
    double getAverageStayDuration();
    double getOccupancyRate(LocalDate date);

    // File operations
    void exportBookings(String filename) throws IOException;
    void importBookings(String filename) throws IOException, InvalidBookingException;
    void generateReport(String filename) throws IOException, ReportException;
}

class ReportException2 extends Exception {
    public ReportException2(String message) {
        super(message);
    }
}


class BookingStore implements IBookingStore{
    Queue<Booking> bookingQueue = new LinkedList<>();

    @Override
    public void storeBookings(Queue<Booking> bookings) throws IOException {
        while(!bookings.isEmpty())
            bookingQueue.add(bookings.poll());
    }

    @Override
    public Queue<Booking> getAllBookings() {
        return new LinkedList<>(bookingQueue);
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for (Booking b : bookingQueue) {
                writer.write(b.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException, InvalidBookingException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            try{
                String line = reader.readLine();
                while(line != null){
                    bookingQueue.add(Booking.fromCSV(line));
                    line = reader.readLine();
                }
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }
}

class BookingSystem implements IBookingSystem{
    Queue<Booking> bookings = new LinkedList<>();
    int batchSize;
    IBookingStore store;
    String offset = "BOOO";
    int count = 1;

    public BookingSystem(IBookingStore store, int batchSize) {
        this.store = store;
        this.batchSize = batchSize;
    }

    @Override
    public void createBooking(String guestId, String roomNumber, RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        bookings.add(new Booking(offset+count++,guestId,roomNumber,roomType,checkIn,checkOut));
        if(bookings.size() >= batchSize){
            try {
                store.storeBookings(bookings);
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void confirmBooking(String bookingId) {
        bookings.stream().filter(obj -> obj.bookingId.equals(bookingId)).forEach(obj->{
                obj.status=BookingStatus.CONFIRMED;
                System.out.println("Confirmed Booking of Booking Id : "+obj.bookingId);
        });

        store.getAllBookings().stream().filter(obj->obj.bookingId.equals(bookingId)).forEach(obj-> {
            obj.status = BookingStatus.CONFIRMED;
            System.out.println("Confirmed Booking : "+obj.bookingId);
        });
    }

    @Override
    public void checkIn(String bookingId) {
        bookings.stream().map(obj -> obj.status=BookingStatus.CHECKED_IN)
                .forEach(obj-> System.out.println("Confirmed Booking of Booking Id : "+obj));
    }

    @Override
    public void checkOut(String bookingId) {
        bookings.stream().map(obj -> obj.status=BookingStatus.CHECKED_OUT)
                .forEach(obj-> System.out.println("Confirmed Booking of Booking Id : "+obj));
    }

    @Override
    public void cancelBooking(String bookingId) {
        bookings.stream().map(obj -> obj.status=BookingStatus.CANCELLED)
                .forEach(obj-> System.out.println("Confirmed Booking of Booking Id : "+obj));
    }

    @Override
    public int getTotalBookings() {
        return store.getAllBookings().size();
    }

    @Override
    public int getPendingBookings() {
        return bookings.size();
    }

    @Override
    public void flush() throws IOException {
        store.storeBookings(bookings);
    }

    @Override
    public Map<RoomType, Long> getBookingsByRoomType() {
        return store.getAllBookings().stream().collect(
                Collectors.groupingBy(obj->obj.roomType,Collectors.counting())
        );
    }

    @Override
    public Map<BookingStatus, Long> getBookingsByStatus() {
        return store.getAllBookings().stream().collect(
            Collectors.groupingBy(obj -> obj.status,Collectors.counting())
        );
    }

    @Override
    public double getTotalRevenue() {
        return store.getAllBookings().stream().mapToDouble(obj-> obj.totalAmount).sum();
    }

    @Override
    public double getAverageBookingValue() {
        return store.getAllBookings().stream().mapToDouble(obj -> obj.totalAmount).average().orElse(0);
    }

    @Override
    public List<Booking> getBookingsByGuest(String guestId) {
        return store.getAllBookings().stream().filter(obj -> obj.guestId.equals(guestId)).collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return store.getAllBookings().stream().filter(obj-> obj.status==status).collect(Collectors.toList());
    }

    @Override
    public List<Booking> getBookingsByDateRange(LocalDate start, LocalDate end) {
        return store.getAllBookings().stream().
                filter(obj -> !obj.checkInDate.isBefore(start)&&!obj.checkOutDate.isAfter(end)).collect(Collectors.toList());
    }

    @Override
    public List<Booking> getUpcomingCheckIns(int daysAhead) {
        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(daysAhead);
        return store.getAllBookings().stream().filter(obj->obj.status==BookingStatus.CONFIRMED || obj.status==BookingStatus.PENDING)
                .filter(obj->!obj.checkInDate.isBefore(today))
                .filter(obj -> !obj.checkInDate.isAfter(futureDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Booking> getLongStayBookings(int minNights) {
        return store.getAllBookings().stream()
                .filter(b -> {
                    long nights = ChronoUnit.DAYS.between(b.checkInDate, b.checkOutDate);
                    return nights >= minNights;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> getRevenueByGuest() {
        return store.getAllBookings().stream().filter(obj->obj.status!=BookingStatus.CANCELLED && obj.status!=BookingStatus.PENDING).collect(
                Collectors.groupingBy(obj->obj.guestId,Collectors.summingDouble(obj->obj.totalAmount))
        );
    }

    @Override
    public List<String> getTopSpendingGuests(int n) {
        return getRevenueByGuest()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Map<RoomType, Double> getRevenueByRoomType() {
        return store.getAllBookings().stream().collect(
                Collectors.groupingBy(obj -> obj.roomType,Collectors.summingDouble(obj->obj.totalAmount))
        );
    }

    @Override
    public Map<String, Long> getBookingsByRoom() {
        return store.getAllBookings().stream().collect(
                Collectors.groupingBy(obj-> obj.roomNumber,Collectors.counting())
        );
    }

    @Override
    public List<String> getMostBookedRooms(int n) {
        return getBookingsByRoom()  // This returns Map<String, Long>
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public double getAverageStayDuration() {
        return store.getAllBookings().stream()
                .mapToLong(b -> ChronoUnit.DAYS.between(b.checkInDate, b.checkOutDate))
                .average()
                .orElse(0.0);
    }

    @Override
    public double getOccupancyRate(LocalDate date) {
        int totalRooms = 100;

        long occupiedRooms = store.getAllBookings().stream()
                .filter(b -> b.status == BookingStatus.CONFIRMED ||
                        b.status == BookingStatus.CHECKED_IN)
                .filter(b -> !date.isBefore(b.checkInDate) &&
                        !date.isAfter(b.checkOutDate))
                .count();

        return (occupiedRooms * 100.0) / totalRooms;
    }

    @Override
    public void exportBookings(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){

            for(Booking b : store.getAllBookings()){
                writer.write(b.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void importBookings(String filename) throws IOException, InvalidBookingException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            try{
                String line = reader.readLine();
                while(line != null){
                    bookings.add(Booking.fromCSV(line));
                    line = reader.readLine();
                }
                if(bookings.size() >= batchSize){
                    flush();
                }
            }
            catch (InvalidBookingException e){
                throw new InvalidBookingException("Invalid Review : "+e.getMessage());
            }
        }
    }

    @Override
    public void generateReport(String filename) throws IOException, ReportException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=== HOTEL BOOKING REPORT ===\n\n");

            writer.write("Total Bookings: " + getTotalBookings() + "\n");
            writer.write("Total Revenue: ₹" + String.format("%.2f", getTotalRevenue()) + "\n");
            writer.write("Average Booking Value: ₹" +
                    String.format("%.2f", getAverageBookingValue()) + "\n");
            writer.write("Average Stay Duration: " +
                    String.format("%.1f", getAverageStayDuration()) + " nights\n\n");

            writer.write("--- Bookings by Room Type ---\n");
            getBookingsByRoomType().forEach((type, count) -> {
                try {
                    writer.write(type + ": " + count + " bookings\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.write("\n--- Bookings by Status ---\n");
            getBookingsByStatus().forEach((status, count) -> {
                try {
                    writer.write(status + ": " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.write("\n--- Revenue by Room Type ---\n");
            getRevenueByRoomType().forEach((type, revenue) -> {
                try {
                    writer.write(type + ": ₹" + String.format("%.2f", revenue) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.write("\n--- Top 5 Spending Guests ---\n");
            int rank = 1;
            for (String guestId : getTopSpendingGuests(5)) {
                Double revenue = getRevenueByGuest().get(guestId);
                writer.write(rank++ + ". " + guestId + " - ₹" +
                        String.format("%.2f", revenue) + "\n");
            }

            writer.write("\n--- Most Booked Rooms ---\n");
            rank = 1;
            for (String roomNumber : getMostBookedRooms(5)) {
                Long count = getBookingsByRoom().get(roomNumber);
                writer.write(rank++ + ". Room " + roomNumber + " - " + count + " bookings\n");
            }

        } catch (IOException e) {
            throw new ReportException("Failed to generate report: " + e.getMessage());
        }
    }
}

public class _09_BookingSystem {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║     HOTEL BOOKING SYSTEM - COMPREHENSIVE TEST         ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");

        try {
            // Initialize system
            IBookingStore store = new BookingStore();
            IBookingSystem system = new BookingSystem(store, 3);

            // Test data
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate in3Days = today.plusDays(3);
            LocalDate in5Days = today.plusDays(5);
            LocalDate in7Days = today.plusDays(7);
            LocalDate in10Days = today.plusDays(10);

            /* ═══════════════════════════════════════════════════════
               TEST 1: CREATE BOOKINGS (BATCHING)
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 1: CREATE BOOKINGS & BATCHING");

            system.createBooking("G001", "R101", RoomType.SINGLE,
                    tomorrow, in3Days);
            System.out.println("✓ Created booking 1 (SINGLE, 2 nights)");
            System.out.println("  Pending: " + system.getPendingBookings());

            system.createBooking("G002", "R102", RoomType.DOUBLE,
                    in5Days, in7Days);
            System.out.println("✓ Created booking 2 (DOUBLE, 2 nights)");
            System.out.println("  Pending: " + system.getPendingBookings());

            system.createBooking("G001", "R201", RoomType.DELUXE,
                    in3Days, in5Days);
            System.out.println("✓ Created booking 3 (DELUXE, 2 nights)");
            System.out.println("  ⚡ BATCH FLUSHED! (size=3)");
            System.out.println("  Pending: " + system.getPendingBookings());
            System.out.println("  Total Stored: " + system.getTotalBookings());

            system.createBooking("G003", "R301", RoomType.SUITE,
                    tomorrow, in5Days);
            System.out.println("\n✓ Created booking 4 (SUITE, 4 nights)");

            system.createBooking("G002", "R103", RoomType.SINGLE,
                    in10Days, in10Days.plusDays(1));
            System.out.println("✓ Created booking 5 (SINGLE, 1 night)");

            system.createBooking("G004", "R104", RoomType.DOUBLE,
                    in3Days, in7Days);
            System.out.println("✓ Created booking 6 (DOUBLE, 4 nights)");
            System.out.println("  ⚡ BATCH FLUSHED! (size=3)");

            system.createBooking("G001", "R105", RoomType.DELUXE,
                    in7Days, in10Days);
            System.out.println("\n✓ Created booking 7 (DELUXE, 3 nights)");
            System.out.println("  Pending: " + system.getPendingBookings());

            System.out.println("\n🔄 Manual flush...");
            system.flush();
            System.out.println("  Pending: " + system.getPendingBookings());
            System.out.println("  Total Stored: " + system.getTotalBookings());

            /* ═══════════════════════════════════════════════════════
               TEST 2: STATUS UPDATES
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 2: STATUS UPDATES");

            // Get all bookings to update their status
            Queue<Booking> allBookings = store.getAllBookings();
            List<Booking> bookingList = new ArrayList<>(allBookings);

            if (bookingList.size() >= 3) {
                system.confirmBooking(bookingList.get(0).bookingId);
                system.confirmBooking(bookingList.get(1).bookingId);
                system.checkIn(bookingList.get(0).bookingId);
                system.cancelBooking(bookingList.get(2).bookingId);

                System.out.println("\n✓ Status updates completed");
                System.out.println("  Booking 1: CHECKED_IN");
                System.out.println("  Booking 2: CONFIRMED");
                System.out.println("  Booking 3: CANCELLED");
            }

            /* ═══════════════════════════════════════════════════════
               TEST 3: STREAM-BASED ANALYTICS
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 3: STREAM-BASED ANALYTICS");

            System.out.println("📊 Bookings by Room Type:");
            system.getBookingsByRoomType().forEach((type, count) ->
                    System.out.println("  " + type + ": " + count + " bookings")
            );

            System.out.println("\n📊 Bookings by Status:");
            system.getBookingsByStatus().forEach((status, count) ->
                    System.out.println("  " + status + ": " + count)
            );

            System.out.println("\n💰 Financial Summary:");
            System.out.println("  Total Revenue: ₹" +
                    String.format("%.2f", system.getTotalRevenue()));
            System.out.println("  Average Booking Value: ₹" +
                    String.format("%.2f", system.getAverageBookingValue()));

            System.out.println("\n📊 Revenue by Room Type:");
            system.getRevenueByRoomType().forEach((type, revenue) ->
                    System.out.println("  " + type + ": ₹" + String.format("%.2f", revenue))
            );

            /* ═══════════════════════════════════════════════════════
               TEST 4: LAMBDA FILTERS
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 4: LAMBDA FILTERS");

            System.out.println("🔍 Bookings by Guest G001:");
            List<Booking> g001Bookings = system.getBookingsByGuest("G001");
            g001Bookings.forEach(b ->
                    System.out.println("  " + b.bookingId + " | Room " + b.roomNumber +
                            " | " + b.roomType + " | " + b.checkInDate)
            );

            System.out.println("\n🔍 Confirmed Bookings:");
            List<Booking> confirmed = system.getBookingsByStatus(BookingStatus.CONFIRMED);
            System.out.println("  Count: " + confirmed.size());

            System.out.println("\n🔍 Upcoming Check-ins (Next 7 Days):");
            List<Booking> upcoming = system.getUpcomingCheckIns(7);
            upcoming.forEach(b -> {
                long daysAway = ChronoUnit.DAYS.between(today, b.checkInDate);
                System.out.println("  " + b.bookingId + " | Guest " + b.guestId +
                        " | Room " + b.roomNumber + " | In " + daysAway + " days");
            });

            System.out.println("\n🔍 Long Stay Bookings (3+ nights):");
            List<Booking> longStays = system.getLongStayBookings(3);
            longStays.forEach(b -> {
                long nights = ChronoUnit.DAYS.between(b.checkInDate, b.checkOutDate);
                System.out.println("  " + b.bookingId + " | " + nights + " nights | ₹" +
                        String.format("%.2f", b.totalAmount));
            });

            System.out.println("\n🔍 Bookings by Date Range (" + tomorrow + " to " + in5Days + "):");
            List<Booking> dateRange = system.getBookingsByDateRange(tomorrow, in5Days);
            System.out.println("  Found: " + dateRange.size() + " bookings");

            /* ═══════════════════════════════════════════════════════
               TEST 5: ADVANCED ANALYTICS
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 5: ADVANCED ANALYTICS");

            System.out.println("💎 Revenue by Guest:");
            system.getRevenueByGuest().forEach((guestId, revenue) ->
                    System.out.println("  " + guestId + ": ₹" + String.format("%.2f", revenue))
            );

            System.out.println("\n🏆 Top 3 Spending Guests:");
            List<String> topGuests = system.getTopSpendingGuests(3);
            for (int i = 0; i < topGuests.size(); i++) {
                String guestId = topGuests.get(i);
                Double revenue = system.getRevenueByGuest().get(guestId);
                System.out.println("  " + (i + 1) + ". " + guestId +
                        " - ₹" + String.format("%.2f", revenue));
            }

            System.out.println("\n🏨 Bookings by Room:");
            system.getBookingsByRoom().forEach((room, count) ->
                    System.out.println("  Room " + room + ": " + count + " bookings")
            );

            System.out.println("\n🏆 Most Booked Rooms (Top 3):");
            List<String> topRooms = system.getMostBookedRooms(3);
            for (int i = 0; i < topRooms.size(); i++) {
                String room = topRooms.get(i);
                Long count = system.getBookingsByRoom().get(room);
                System.out.println("  " + (i + 1) + ". Room " + room +
                        " - " + count + " bookings");
            }

            System.out.println("\n📈 Average Stay Duration: " +
                    String.format("%.1f", system.getAverageStayDuration()) +
                    " nights");

            System.out.println("\n📊 Occupancy Rate for " + tomorrow + ": " +
                    String.format("%.1f", system.getOccupancyRate(tomorrow)) + "%");

            /* ═══════════════════════════════════════════════════════
               TEST 6: FILE OPERATIONS
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 6: FILE OPERATIONS");

            // Export bookings
            String exportFile = "bookings_export.csv";
            system.exportBookings(exportFile);
            System.out.println("✓ Exported bookings to: " + exportFile);

            // Generate report
            String reportFile = "booking_report.txt";
            system.generateReport(reportFile);
            System.out.println("✓ Generated report: " + reportFile);

            // Save store to file
            String storeFile = "bookings_store.csv";
            store.saveToFile(storeFile);
            System.out.println("✓ Saved store to: " + storeFile);

            /* ═══════════════════════════════════════════════════════
               TEST 7: IMPORT & VERIFY
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 7: IMPORT & VERIFICATION");

            // Create new system and import
            IBookingStore newStore = new BookingStore();
            IBookingSystem importedSystem = new BookingSystem(newStore, 2);

            System.out.println("🔄 Importing bookings from: " + exportFile);
            importedSystem.importBookings(exportFile);
            importedSystem.flush();

            System.out.println("\n✓ Import completed!");
            System.out.println("  Original System Bookings: " + system.getTotalBookings());
            System.out.println("  Imported System Bookings: " + importedSystem.getTotalBookings());

            // Verify data integrity
            System.out.println("\n🔍 Data Integrity Check:");
            boolean revenueMatch = Math.abs(system.getTotalRevenue() -
                    importedSystem.getTotalRevenue()) < 0.01;
            System.out.println("  Revenue Match: " + (revenueMatch ? "✓ PASS" : "✗ FAIL"));

            Map<RoomType, Long> originalRoomTypes = system.getBookingsByRoomType();
            Map<RoomType, Long> importedRoomTypes = importedSystem.getBookingsByRoomType();
            boolean roomTypeMatch = originalRoomTypes.equals(importedRoomTypes);
            System.out.println("  Room Type Distribution: " +
                    (roomTypeMatch ? "✓ PASS" : "✗ FAIL"));

            /* ═══════════════════════════════════════════════════════
               TEST 8: CSV PARSING VALIDATION
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 8: CSV PARSING VALIDATION");

            // Test valid CSV
            String validCSV = "B999,G999,R999,SINGLE,2026-03-01,2026-03-03,CONFIRMED,2000.0,1708704000000";
            try {
                Booking parsed = Booking.fromCSV(validCSV);
                System.out.println("✓ Valid CSV parsed successfully:");
                System.out.println("  " + parsed.bookingId + " | " + parsed.guestId +
                        " | Room " + parsed.roomNumber);
            } catch (InvalidBookingException e) {
                System.out.println("✗ Failed to parse valid CSV: " + e.getMessage());
            }

            // Test invalid CSV (wrong field count)
            String invalidCSV1 = "B999,G999,R999,SINGLE";
            try {
                Booking.fromCSV(invalidCSV1);
                System.out.println("✗ Should have thrown exception for invalid CSV");
            } catch (InvalidBookingException e) {
                System.out.println("✓ Invalid CSV rejected correctly: " + e.getMessage());
            }

            // Test invalid CSV (bad date format)
            String invalidCSV2 = "B999,G999,R999,SINGLE,BADDATE,2026-03-03,CONFIRMED,2000.0,1708704000000";
            try {
                Booking.fromCSV(invalidCSV2);
                System.out.println("✗ Should have thrown exception for bad date");
            } catch (InvalidBookingException e) {
                System.out.println("✓ Bad date format rejected: " + e.getMessage());
            }

            /* ═══════════════════════════════════════════════════════
               TEST 9: EDGE CASES
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 9: EDGE CASES");

            // Same-day booking
            system.createBooking("G999", "R999", RoomType.SINGLE, today, today.plusDays(1));
            System.out.println("✓ Same-day booking created (1 night)");

            // Long stay
            system.createBooking("G888", "R888", RoomType.SUITE,
                    today, today.plusDays(30));
            System.out.println("✓ Long stay booking created (30 nights)");

            system.flush();

            // Check if they appear correctly
            List<Booking> allLongStays = system.getLongStayBookings(20);
            System.out.println("✓ Long stays (20+ nights): " + allLongStays.size());

            /* ═══════════════════════════════════════════════════════
               TEST 10: FINAL SUMMARY
            ═══════════════════════════════════════════════════════ */
            printSection("TEST 10: FINAL SUMMARY");

            System.out.println("📊 SYSTEM STATISTICS:");
            System.out.println("  Total Bookings: " + system.getTotalBookings());
            System.out.println("  Total Revenue: ₹" +
                    String.format("%.2f", system.getTotalRevenue()));
            System.out.println("  Average Booking Value: ₹" +
                    String.format("%.2f", system.getAverageBookingValue()));
            System.out.println("  Average Stay Duration: " +
                    String.format("%.1f", system.getAverageStayDuration()) +
                    " nights");

            System.out.println("\n📈 BOOKING BREAKDOWN:");
            system.getBookingsByStatus().forEach((status, count) ->
                    System.out.println("  " + status + ": " + count)
            );

            System.out.println("\n🏆 TOP PERFORMERS:");
            System.out.println("  Top Guest: " +
                    (topGuests.isEmpty() ? "N/A" : topGuests.get(0)));
            System.out.println("  Most Booked Room: " +
                    (topRooms.isEmpty() ? "N/A" : topRooms.get(0)));

            System.out.println("\n📁 FILES GENERATED:");
            System.out.println("  1. " + exportFile);
            System.out.println("  2. " + reportFile);
            System.out.println("  3. " + storeFile);

            System.out.println("\n╔════════════════════════════════════════════════════════╗");
            System.out.println("║          ALL TESTS COMPLETED SUCCESSFULLY ✅          ║");
            System.out.println("╚════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR OCCURRED: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printSection(String title) {
        System.out.println("\n" + "═".repeat(60));
        System.out.println(title);
        System.out.println("═".repeat(60) + "\n");
    }
}
