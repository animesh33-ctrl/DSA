package mock;

import java.util.*;
import java.io.*;
import java.util.stream.*;
import java.time.*;
import java.nio.file.*;


enum RideStatus {
    REQUESTED, DRIVER_ASSIGNED, DRIVER_ARRIVED, STARTED, COMPLETED, CANCELLED
}

enum VehicleType {
    BIKE(1, 8.0), MINI(4, 12.0), SEDAN(4, 15.0), SUV(6, 20.0), LUXURY(4, 30.0);

    private final int capacity;
    private final double baseFarePerKm;

    VehicleType(int capacity, double baseFarePerKm) {
        this.capacity = capacity;
        this.baseFarePerKm = baseFarePerKm;
    }

    public int getCapacity() { return capacity; }
    public double getBaseFare() { return baseFarePerKm; }
}

enum PaymentMethod {
    CASH, CARD, WALLET, UPI
}

class Location {
    double latitude;
    double longitude;
    String address;

    public Location(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
        this.address = "";
    }

    public Location(double lat, double lon, String address) {
        this.latitude = lat;
        this.longitude = lon;
        this.address = address;
    }

    public double distanceTo(Location other) {
        double R = 6371;
        double dLat = Math.toRadians(other.latitude - this.latitude);
        double dLon = Math.toRadians(other.longitude - this.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(this.latitude)) *
                        Math.cos(Math.toRadians(other.latitude)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public String toString() {
        return (address != null && !address.isEmpty() ? address + " " : "") +
                "(" + latitude + ", " + longitude + ")";
    }
}

class Driver {
    String driverId;
    String name;
    String phone;
    VehicleType vehicleType;
    String vehicleNumber;
    Location currentLocation;
    boolean isAvailable;
    double rating;
    int totalRides;
    double totalEarnings;

    public Driver(String driverId, String name, String phone, VehicleType vehicleType,
                  String vehicleNumber, Location currentLocation) {
        this.driverId = driverId;
        this.name = name;
        this.phone = phone;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.currentLocation = currentLocation;
        this.isAvailable = true;
        this.rating = 5.0;
        this.totalRides = 0;
        this.totalEarnings = 0.0;
    }

    public Driver(String driverId, String name, VehicleType vehicleType,
                  String vehicleNumber, Location location) {
        this(driverId, name, null, vehicleType, vehicleNumber, location);
    }

    public void updateLocation(double lat, double lon) {
        this.currentLocation.latitude = lat;
        this.currentLocation.longitude = lon;
    }

    @Override
    public String toString() {
        return driverId + " - " + name + " (" + vehicleType + ") Rating: " + String.format("%.1f", rating);
    }
}

class Rider {
    String riderId;
    String name;
    String phone;
    double walletBalance;
    double rating;
    int totalRides;

    public Rider(String riderId, String name, String phone) {
        this.riderId = riderId;
        this.name = name;
        this.phone = phone;
        this.walletBalance = 0.0;
        this.rating = 5.0;
        this.totalRides = 0;
    }

    public boolean addMoney(double amount) {
        if (amount > 0) {
            walletBalance += amount;
            return true;
        }
        return false;
    }

    public boolean deductMoney(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return riderId + " - " + name;
    }
}

class Ride {
    String rideId;
    String riderId;
    String driverId;
    Location pickup;
    Location destination;
    VehicleType vehicleType;
    RideStatus status;
    double estimatedFare;
    double actualFare;
    double distance;
    long requestTime;
    long startTime;
    long endTime;
    PaymentMethod paymentMethod;
    int riderRating;
    int driverRating;

    public Ride(String rideId, String riderId, Location pickup,
                Location destination, VehicleType vehicleType) {
        this.rideId = rideId;
        this.riderId = riderId;
        this.pickup = pickup;
        this.destination = destination;
        this.vehicleType = vehicleType;
        this.status = RideStatus.REQUESTED;
        this.requestTime = System.currentTimeMillis();
        this.distance = pickup.distanceTo(destination);
    }

    public String toCSV() {
        return rideId + "," + riderId + "," + (driverId != null ? driverId : "") + "," +
                pickup.longitude + "," + pickup.latitude + "," +
                destination.longitude + "," + destination.latitude + "," +
                vehicleType.ordinal() + "," + (status != null ? status.ordinal() : 0) + "," +
                estimatedFare + "," + actualFare + "," + distance + "," +
                requestTime + "," + startTime + "," + endTime + "," +
                (paymentMethod != null ? paymentMethod : "") + "," +
                riderRating + "," + driverRating;
    }

    public static Ride fromCSV(String line) throws InvalidRideException {
        try {
            String[] split = line.split(",");
            Ride ride = new Ride(
                    split[0].trim(),
                    split[1].trim(),
                    new Location(Double.parseDouble(split[4].trim()), Double.parseDouble(split[3].trim())),
                    new Location(Double.parseDouble(split[6].trim()), Double.parseDouble(split[5].trim())),
                    VehicleType.values()[Integer.parseInt(split[7].trim())]
            );
            ride.driverId = split[2].trim().isEmpty() ? null : split[2].trim();
            ride.status = RideStatus.values()[Integer.parseInt(split[8].trim())];
            ride.estimatedFare = Double.parseDouble(split[9].trim());
            ride.actualFare = Double.parseDouble(split[10].trim());
            ride.distance = Double.parseDouble(split[11].trim());
            ride.requestTime = Long.parseLong(split[12].trim());
            ride.startTime = Long.parseLong(split[13].trim());
            ride.endTime = Long.parseLong(split[14].trim());
            if (split.length > 15 && !split[15].trim().isEmpty()) {
                ride.paymentMethod = PaymentMethod.valueOf(split[15].trim());
            }
            if (split.length > 16) ride.riderRating = Integer.parseInt(split[16].trim());
            if (split.length > 17) ride.driverRating = Integer.parseInt(split[17].trim());
            return ride;
        } catch (Exception e) {
            throw new InvalidRideException("Invalid ride data: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("Ride[%s] Rider:%s Driver:%s Status:%s Fare:₹%.2f Dist:%.2fkm",
                rideId, riderId, driverId, status, actualFare > 0 ? actualFare : estimatedFare, distance);
    }
}


class InvalidRideException extends Exception {
    public InvalidRideException(String message) { super(message); }
}

class PricingException extends Exception {
    public PricingException(String message) { super(message); }
}

class MatchingException extends Exception {
    public MatchingException(String message) { super(message); }
}


interface IPricingStrategy {
    double calculateFare(Ride ride) throws PricingException;
    double applySurge(double baseFare, Location location, LocalTime time);
}

interface IDriverMatcher {
    Driver findNearestAvailableDriver(Location pickup, VehicleType type, double maxRadius) throws MatchingException;
    List<Driver> findAllAvailableDrivers(Location pickup, VehicleType type, double radius);
}

interface IRideStore {
    void storeRides(Queue<Ride> rides) throws IOException;
    Queue<Ride> getAllRides();
    void persistToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, InvalidRideException;
}

interface IRideService {
    String requestRide(String riderId, Location pickup, Location destination, VehicleType vehicleType) throws MatchingException;
    boolean acceptRide(String rideId, String driverId);
    boolean startRide(String rideId);
    boolean completeRide(String rideId, PaymentMethod paymentMethod) throws PricingException;
    boolean cancelRide(String rideId, String reason);
    void rateDriver(String rideId, int rating);
    void rateRider(String rideId, int rating);
    List<Ride> getRidesByRider(String riderId);
    List<Ride> getRidesByDriver(String driverId);
    List<Ride> getActiveRides();
    List<Ride> getCompletedRides();
    Map<RideStatus, Long> getRidesByStatus();
    Map<VehicleType, Long> getRidesByVehicleType();
    double getAverageRideDistance();
    double getAverageFare();
    double getTotalRevenue();
    List<String> getTopRiders(int n);
    List<String> getTopDrivers(int n);
    List<Driver> getBestRatedDrivers(int n);
    Map<String, Double> getDriverEarnings();
    List<Ride> getRidesInTimeRange(long startTime, long endTime);
    List<Ride> getRidesInLastNHours(int hours);
    Map<Integer, Long> getRidesByHourOfDay();
    void exportRides(String filename) throws IOException;
    void importRides(String filename) throws IOException, InvalidRideException;
    void generateReport(String filename) throws IOException;
    int getPendingRides();
    void flush() throws IOException;
}

interface IDriverService {
    void registerDriver(Driver driver);
    void updateDriverLocation(String driverId, double lat, double lon);
    void setDriverAvailability(String driverId, boolean available);
    Driver getDriver(String driverId);
    List<Driver> getAllDrivers();
    Map<String, Integer> getDriverRideCount();
    Map<String, Double> getDriverRatings();
}

interface IRiderService {
    void registerRider(Rider rider);
    Rider getRider(String riderId);
    boolean addMoneyToWallet(String riderId, double amount);
    double getWalletBalance(String riderId);
    Map<String, Integer> getRiderRideCount();
    List<String> getMostActiveRiders(int n);
}



class RideStore implements IRideStore {
    private Queue<Ride> rides = new LinkedList<>();

    @Override
    public void storeRides(Queue<Ride> rides) throws IOException {
        this.rides = new LinkedList<>(rides);
    }

    @Override
    public Queue<Ride> getAllRides() {
        return new LinkedList<>(rides);
    }

    @Override
    public void persistToFile(String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            for (Ride ride : rides) {
                writer.write(ride.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException, InvalidRideException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        Ride ride = Ride.fromCSV(line);
                        rides.add(ride);
                    } catch (InvalidRideException e) {
                        System.err.println("Skipping invalid ride: " + line);
                    }
                }
            }
        }
    }
}

class DriverMatcher implements IDriverMatcher {
    private List<Driver> availableDrivers = new ArrayList<>();

    public void addAvailableDriver(Driver driver) {
        availableDrivers.removeIf(d -> d.driverId.equals(driver.driverId));
        availableDrivers.add(driver);
    }

    public void removeDriver(String driverId) {
        availableDrivers.removeIf(d -> d.driverId.equals(driverId));
    }

    @Override
    public Driver findNearestAvailableDriver(Location pickup, VehicleType type, double maxRadius) throws MatchingException {
        return availableDrivers.stream()
                .filter(d -> d.isAvailable && d.vehicleType == type)
                .filter(d -> pickup.distanceTo(d.currentLocation) <= maxRadius)
                .min(Comparator.comparingDouble(d -> pickup.distanceTo(d.currentLocation)))
                .orElseThrow(() -> new MatchingException(
                        "No available " + type + " drivers found within " + maxRadius + " km radius"));
    }

    @Override
    public List<Driver> findAllAvailableDrivers(Location pickup, VehicleType type, double radius) {
        return availableDrivers.stream()
                .filter(d -> d.isAvailable && d.vehicleType == type)
                .filter(d -> pickup.distanceTo(d.currentLocation) <= radius)
                .sorted(Comparator.comparingDouble(d -> pickup.distanceTo(d.currentLocation)))
                .collect(Collectors.toList());
    }
}

class DynamicPricing implements IPricingStrategy {
    private static final double MIN_FARE = 50.0;
    private static final double COST_PER_MINUTE = 2.0;

    @Override
    public double calculateFare(Ride ride) throws PricingException {
        if (ride == null) throw new PricingException("Ride cannot be null");
        if (ride.distance <= 0) throw new PricingException("Invalid ride distance: " + ride.distance);

        double baseFarePerKm = ride.vehicleType.getBaseFare();
        double distanceFare = ride.distance * baseFarePerKm;
        double timeFare = 0.0;

        if (ride.startTime > 0 && ride.endTime > 0) {
            long durationMs = ride.endTime - ride.startTime;
            double durationMinutes = durationMs / (1000.0 * 60.0);
            timeFare = durationMinutes * COST_PER_MINUTE;
        }

        double baseFare = distanceFare + timeFare;
        LocalTime rideTime = LocalTime.now();
        double finalFare = applySurge(baseFare, ride.pickup, rideTime);
        finalFare = Math.max(finalFare, MIN_FARE);

        return Math.round(finalFare * 100.0) / 100.0;
    }

    @Override
    public double applySurge(double baseFare, Location location, LocalTime time) {
        double surgeMultiplier = 1.0;
        int hour = time.getHour();

        if (hour >= 8 && hour < 10) {
            surgeMultiplier = 1.5;
        } else if (hour >= 18 && hour < 21) {
            surgeMultiplier = 1.5;
        } else if (hour >= 23 || hour < 5) {
            surgeMultiplier = 2.0;
        }

        if (isHighDemandArea(location)) {
            surgeMultiplier = Math.max(surgeMultiplier, 1.3);
        }

        return baseFare * surgeMultiplier;
    }

    private boolean isHighDemandArea(Location location) {
        return location.latitude >= 12.93 && location.latitude <= 12.98 &&
                location.longitude >= 77.59 && location.longitude <= 77.65;
    }
}

class DriverService implements IDriverService {
    private final Map<String, Driver> drivers = new HashMap<>();

    @Override
    public void registerDriver(Driver driver) {
        drivers.put(driver.driverId, driver);
        System.out.println("Driver registered: " + driver);
    }

    @Override
    public void updateDriverLocation(String driverId, double lat, double lon) {
        Driver d = drivers.get(driverId);
        if (d != null) d.updateLocation(lat, lon);
    }

    @Override
    public void setDriverAvailability(String driverId, boolean available) {
        Driver d = drivers.get(driverId);
        if (d != null) d.isAvailable = available;
    }

    @Override
    public Driver getDriver(String driverId) {
        return drivers.get(driverId);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return new ArrayList<>(drivers.values());
    }

    @Override
    public Map<String, Integer> getDriverRideCount() {
        Map<String, Integer> counts = new HashMap<>();
        drivers.forEach((id, d) -> counts.put(d.name, d.totalRides));
        return counts;
    }

    @Override
    public Map<String, Double> getDriverRatings() {
        Map<String, Double> ratings = new HashMap<>();
        drivers.forEach((id, d) -> ratings.put(d.name, d.rating));
        return ratings;
    }
}

class RiderService implements IRiderService {
    private final Map<String, Rider> riders = new HashMap<>();

    @Override
    public void registerRider(Rider rider) {
        riders.put(rider.riderId, rider);
        System.out.println("Rider registered: " + rider);
    }

    @Override
    public Rider getRider(String riderId) {
        return riders.get(riderId);
    }

    @Override
    public boolean addMoneyToWallet(String riderId, double amount) {
        Rider r = riders.get(riderId);
        return r != null && r.addMoney(amount);
    }

    @Override
    public double getWalletBalance(String riderId) {
        Rider r = riders.get(riderId);
        return r != null ? r.walletBalance : 0.0;
    }

    @Override
    public Map<String, Integer> getRiderRideCount() {
        Map<String, Integer> counts = new HashMap<>();
        riders.forEach((id, r) -> counts.put(r.name, r.totalRides));
        return counts;
    }

    @Override
    public List<String> getMostActiveRiders(int n) {
        return riders.values().stream()
                .sorted((a, b) -> b.totalRides - a.totalRides)
                .limit(n)
                .map(r -> r.name + " (" + r.totalRides + " rides)")
                .collect(Collectors.toList());
    }
}

class RideService implements IRideService {
    private final IRideStore store;
    private final IDriverMatcher matcher;
    private final IPricingStrategy pricing;
    private final double maxSearchRadius;
    private final Map<String, Ride> activeRides = new HashMap<>();
    private final Map<String, Rider> riders = new HashMap<>();
    private final Map<String, Driver> drivers = new HashMap<>();
    private final List<Ride> completedRides = new ArrayList<>();

    public RideService(IRideStore store, IDriverMatcher matcher,
                       IPricingStrategy pricing, double maxSearchRadius) {
        this.store = store;
        this.matcher = matcher;
        this.pricing = pricing;
        this.maxSearchRadius = maxSearchRadius;
    }

    // Called by RideService to share driver/rider registries
    public void registerRider(Rider rider) {
        riders.put(rider.riderId, rider);
    }

    public void registerDriver(Driver driver) {
        drivers.put(driver.driverId, driver);
        ((DriverMatcher) matcher).addAvailableDriver(driver);
    }

    @Override
    public String requestRide(String riderId, Location pickup, Location destination,
                              VehicleType vehicleType) throws MatchingException {
        if (!riders.containsKey(riderId)) {
            throw new MatchingException("Rider not found: " + riderId);
        }

        String rideId = "RIDE" + System.currentTimeMillis();
        Ride ride = new Ride(rideId, riderId, pickup, destination, vehicleType);

        try {
            ride.estimatedFare = pricing.calculateFare(ride);
        } catch (PricingException e) {
            throw new MatchingException("Failed to calculate fare: " + e.getMessage());
        }

        activeRides.put(rideId, ride);
        System.out.println("Ride created [" + rideId + "] | Estimated Fare: ₹" +
                String.format("%.2f", ride.estimatedFare) + " | Distance: " +
                String.format("%.2f", ride.distance) + " km");
        return rideId;
    }

    @Override
    public boolean acceptRide(String rideId, String driverId) {
        Ride ride = activeRides.get(rideId);
        Driver driver = drivers.get(driverId);
        if (ride == null || driver == null || ride.status != RideStatus.REQUESTED) return false;

        ride.driverId = driverId;
        ride.status = RideStatus.DRIVER_ASSIGNED;
        driver.isAvailable = false;
        ((DriverMatcher) matcher).removeDriver(driverId);

        System.out.println("Driver " + driver.name + " accepted ride " + rideId);
        return true;
    }

    @Override
    public boolean startRide(String rideId) {
        Ride ride = activeRides.get(rideId);
        if (ride == null || ride.status != RideStatus.DRIVER_ASSIGNED) return false;

        ride.status = RideStatus.STARTED;
        ride.startTime = System.currentTimeMillis();
        System.out.println("Ride " + rideId + " started.");
        return true;
    }

    @Override
    public boolean completeRide(String rideId, PaymentMethod paymentMethod) throws PricingException {
        Ride ride = activeRides.get(rideId);
        if (ride == null || ride.status != RideStatus.STARTED) return false;

        ride.status = RideStatus.COMPLETED;
        ride.endTime = System.currentTimeMillis();
        ride.paymentMethod = paymentMethod;
        ride.actualFare = pricing.calculateFare(ride);

        // Handle wallet payment
        if (paymentMethod == PaymentMethod.WALLET) {
            Rider rider = riders.get(ride.riderId);
            if (rider != null) {
                boolean deducted = rider.deductMoney(ride.actualFare);
                if (!deducted) {
                    System.out.println("Warning: Insufficient wallet balance. Payment pending.");
                }
            }
        }

        // Update driver stats
        Driver driver = drivers.get(ride.driverId);
        if (driver != null) {
            driver.totalRides++;
            driver.totalEarnings += ride.actualFare * 0.8; // 80% to driver
            driver.isAvailable = true;
            ((DriverMatcher) matcher).addAvailableDriver(driver);
        }

        // Update rider stats
        Rider rider = riders.get(ride.riderId);
        if (rider != null) rider.totalRides++;

        completedRides.add(ride);
        activeRides.remove(rideId);

        System.out.println("Ride " + rideId + " completed. Fare: ₹" + String.format("%.2f", ride.actualFare) +
                " | Payment: " + paymentMethod);
        return true;
    }

    @Override
    public boolean cancelRide(String rideId, String reason) {
        Ride ride = activeRides.get(rideId);
        if (ride == null) return false;

        ride.status = RideStatus.CANCELLED;

        // Re-enable driver if assigned
        if (ride.driverId != null) {
            Driver driver = drivers.get(ride.driverId);
            if (driver != null) {
                driver.isAvailable = true;
                ((DriverMatcher) matcher).addAvailableDriver(driver);
            }
        }

        completedRides.add(ride);
        activeRides.remove(rideId);
        System.out.println("Ride " + rideId + " cancelled. Reason: " + reason);
        return true;
    }

    @Override
    public void rateDriver(String rideId, int rating) {
        Ride ride = findRide(rideId);
        if (ride == null || ride.driverId == null) return;

        ride.driverRating = rating;
        Driver driver = drivers.get(ride.driverId);
        if (driver != null) {
            // Running average
            int totalRated = driver.totalRides > 0 ? driver.totalRides : 1;
            driver.rating = ((driver.rating * (totalRated - 1)) + rating) / totalRated;
            System.out.println("Driver " + driver.name + " rated: " + rating + " | Avg: " +
                    String.format("%.2f", driver.rating));
        }
    }

    @Override
    public void rateRider(String rideId, int rating) {
        Ride ride = findRide(rideId);
        if (ride == null) return;

        ride.riderRating = rating;
        Rider rider = riders.get(ride.riderId);
        if (rider != null) {
            int totalRated = rider.totalRides > 0 ? rider.totalRides : 1;
            rider.rating = ((rider.rating * (totalRated - 1)) + rating) / totalRated;
            System.out.println("Rider " + rider.name + " rated: " + rating + " | Avg: " +
                    String.format("%.2f", rider.rating));
        }
    }

    private Ride findRide(String rideId) {
        if (activeRides.containsKey(rideId)) return activeRides.get(rideId);
        return completedRides.stream().filter(r -> r.rideId.equals(rideId)).findFirst().orElse(null);
    }

    @Override
    public List<Ride> getRidesByRider(String riderId) {
        return completedRides.stream().filter(r -> riderId.equals(r.riderId)).collect(Collectors.toList());
    }

    @Override
    public List<Ride> getRidesByDriver(String driverId) {
        return completedRides.stream().filter(r -> driverId.equals(r.driverId)).collect(Collectors.toList());
    }

    @Override
    public List<Ride> getActiveRides() {
        return new ArrayList<>(activeRides.values());
    }

    @Override
    public List<Ride> getCompletedRides() {
        return completedRides.stream()
                .filter(r -> r.status == RideStatus.COMPLETED)
                .collect(Collectors.toList());
    }

    @Override
    public Map<RideStatus, Long> getRidesByStatus() {
        List<Ride> all = getAllRidesList();
        return all.stream().collect(Collectors.groupingBy(r -> r.status, Collectors.counting()));
    }

    @Override
    public Map<VehicleType, Long> getRidesByVehicleType() {
        return getAllRidesList().stream()
                .collect(Collectors.groupingBy(r -> r.vehicleType, Collectors.counting()));
    }

    @Override
    public double getAverageRideDistance() {
        return getCompletedRides().stream()
                .mapToDouble(r -> r.distance)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getAverageFare() {
        return getCompletedRides().stream()
                .mapToDouble(r -> r.actualFare)
                .average()
                .orElse(0.0);
    }

    @Override
    public double getTotalRevenue() {
        return getCompletedRides().stream()
                .mapToDouble(r -> r.actualFare)
                .sum();
    }

    @Override
    public List<String> getTopRiders(int n) {
        Map<String, Long> rideCount = getCompletedRides().stream()
                .collect(Collectors.groupingBy(r -> r.riderId, Collectors.counting()));
        return rideCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(e -> {
                    Rider r = riders.get(e.getKey());
                    return (r != null ? r.name : e.getKey()) + " (" + e.getValue() + " rides)";
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getTopDrivers(int n) {
        Map<String, Long> rideCount = getCompletedRides().stream()
                .filter(r -> r.driverId != null)
                .collect(Collectors.groupingBy(r -> r.driverId, Collectors.counting()));
        return rideCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(e -> {
                    Driver d = drivers.get(e.getKey());
                    return (d != null ? d.name : e.getKey()) + " (" + e.getValue() + " rides)";
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Driver> getBestRatedDrivers(int n) {
        return drivers.values().stream()
                .filter(d -> d.totalRides > 0)
                .sorted((a, b) -> Double.compare(b.rating, a.rating))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> getDriverEarnings() {
        Map<String, Double> earnings = new HashMap<>();
        drivers.forEach((id, d) -> {
            if (d.totalEarnings > 0) {
                earnings.put(d.name, Math.round(d.totalEarnings * 100.0) / 100.0);
            }
        });
        return earnings;
    }

    @Override
    public List<Ride> getRidesInTimeRange(long startTime, long endTime) {
        return getAllRidesList().stream()
                .filter(r -> r.requestTime >= startTime && r.requestTime <= endTime)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ride> getRidesInLastNHours(int hours) {
        long cutoff = System.currentTimeMillis() - (long) hours * 3600 * 1000;
        return getAllRidesList().stream()
                .filter(r -> r.requestTime >= cutoff)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Integer, Long> getRidesByHourOfDay() {
        return getAllRidesList().stream()
                .collect(Collectors.groupingBy(
                        r -> LocalDateTime.ofInstant(
                                java.time.Instant.ofEpochMilli(r.requestTime),
                                java.time.ZoneId.systemDefault()).getHour(),
                        Collectors.counting()
                ));
    }

    @Override
    public void exportRides(String filename) throws IOException {
        List<Ride> all = getAllRidesList();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write("RideId,RiderId,DriverId,PickupLon,PickupLat,DestLon,DestLat," +
                    "VehicleType,Status,EstFare,ActualFare,Distance,RequestTime," +
                    "StartTime,EndTime,PaymentMethod,RiderRating,DriverRating");
            writer.newLine();
            for (Ride ride : all) {
                writer.write(ride.toCSV());
                writer.newLine();
            }
        }
        System.out.println("Exported " + all.size() + " rides to " + filename);
    }

    @Override
    public void importRides(String filename) throws IOException, InvalidRideException {
        store.loadFromFile(filename);
        Queue<Ride> loaded = store.getAllRides();
        for (Ride ride : loaded) {
            completedRides.add(ride);
        }
        System.out.println("Imported rides from " + filename);
    }

    @Override
    public void generateReport(String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename))) {
            writer.write("========================================");
            writer.newLine();
            writer.write("       DAILY RIDE SHARING REPORT        ");
            writer.newLine();
            writer.write("Generated: " + LocalDateTime.now());
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            writer.write("SUMMARY");
            writer.newLine();
            writer.write("-------");
            writer.newLine();
            writer.write("Total Rides Completed: " + getCompletedRides().size());
            writer.newLine();
            writer.write("Total Revenue: ₹" + String.format("%.2f", getTotalRevenue()));
            writer.newLine();
            writer.write("Average Fare: ₹" + String.format("%.2f", getAverageFare()));
            writer.newLine();
            writer.write("Average Distance: " + String.format("%.2f", getAverageRideDistance()) + " km");
            writer.newLine();
            writer.write("Active Rides: " + activeRides.size());
            writer.newLine();
            writer.write("Pending Rides: " + getPendingRides());
            writer.newLine();
            writer.newLine();

            writer.write("RIDES BY STATUS");
            writer.newLine();
            writer.write("---------------");
            writer.newLine();
            getRidesByStatus().forEach((status, count) -> {
                try { writer.write(status + ": " + count); writer.newLine(); }
                catch (IOException ignored) {}
            });
            writer.newLine();

            writer.write("RIDES BY VEHICLE TYPE");
            writer.newLine();
            writer.write("---------------------");
            writer.newLine();
            getRidesByVehicleType().forEach((type, count) -> {
                try { writer.write(type + ": " + count); writer.newLine(); }
                catch (IOException ignored) {}
            });
            writer.newLine();

            writer.write("TOP DRIVERS");
            writer.newLine();
            writer.write("-----------");
            writer.newLine();
            getTopDrivers(5).forEach(d -> {
                try { writer.write(d); writer.newLine(); }
                catch (IOException ignored) {}
            });
            writer.newLine();

            writer.write("DRIVER EARNINGS");
            writer.newLine();
            writer.write("---------------");
            writer.newLine();
            getDriverEarnings().forEach((driver, earnings) -> {
                try { writer.write(driver + ": ₹" + earnings); writer.newLine(); }
                catch (IOException ignored) {}
            });
            writer.newLine();

            writer.write("TOP RIDERS");
            writer.newLine();
            writer.write("----------");
            writer.newLine();
            getTopRiders(5).forEach(r -> {
                try { writer.write(r); writer.newLine(); }
                catch (IOException ignored) {}
            });
        }
        System.out.println("Report generated: " + filename);
    }

    @Override
    public int getPendingRides() {
        return (int) activeRides.values().stream()
                .filter(r -> r.status == RideStatus.REQUESTED)
                .count();
    }

    @Override
    public void flush() throws IOException {
        List<Ride> all = getAllRidesList();
        Queue<Ride> queue = new LinkedList<>(all);
        store.storeRides(queue);
    }

    private List<Ride> getAllRidesList() {
        List<Ride> all = new ArrayList<>(completedRides);
        all.addAll(activeRides.values());
        return all;
    }
}

// ===== MAIN =====

public class _08_RideSharingSystem {
    public static void main(String[] args) throws Exception {
        System.out.println("========================================");
        System.out.println("     RIDE SHARING SYSTEM - DEMO         ");
        System.out.println("========================================\n");

        // Setup
        IRideStore store = new RideStore();
        DriverMatcher driverMatcher = new DriverMatcher();
        IPricingStrategy pricing = new DynamicPricing();
        RideService service = new RideService(store, driverMatcher, pricing, 10.0);
        IDriverService driverService = new DriverService();
        IRiderService riderService = new RiderService();

        // Register Drivers
        System.out.println("--- Registering Drivers ---");
        Driver d1 = new Driver("D001", "Raj", VehicleType.SEDAN, "KA01AB1234",
                new Location(12.9716, 77.5946, "Bangalore"));
        driverService.registerDriver(d1);
        service.registerDriver(d1);

        Driver d2 = new Driver("D002", "Amit", VehicleType.MINI, "KA02CD5678",
                new Location(12.9750, 77.5980, "Indiranagar"));
        driverService.registerDriver(d2);
        service.registerDriver(d2);

        Driver d3 = new Driver("D003", "Suresh", VehicleType.SUV, "KA03EF9012",
                new Location(12.9600, 77.5900, "Koramangala"));
        driverService.registerDriver(d3);
        service.registerDriver(d3);

        // Register Riders
        System.out.println("\n--- Registering Riders ---");
        Rider r1 = new Rider("R001", "Priya", "9876543210");
        r1.addMoney(1000.0);
        riderService.registerRider(r1);
        service.registerRider(r1);

        Rider r2 = new Rider("R002", "Kiran", "9876543211");
        r2.addMoney(500.0);
        riderService.registerRider(r2);
        service.registerRider(r2);

        // ===== RIDE 1: SEDAN =====
        System.out.println("\n--- Ride 1: Priya requests a SEDAN ---");
        Location pickup1 = new Location(12.9716, 77.5946, "MG Road");
        Location dest1 = new Location(12.9352, 77.6245, "HSR Layout");

        String rideId1 = service.requestRide("R001", pickup1, dest1, VehicleType.SEDAN);
        service.acceptRide(rideId1, "D001");
        service.startRide(rideId1);
        Thread.sleep(2000); // Simulate ride duration
        service.completeRide(rideId1, PaymentMethod.WALLET);
        service.rateDriver(rideId1, 5);
        service.rateRider(rideId1, 4);

        // ===== RIDE 2: MINI =====
        System.out.println("\n--- Ride 2: Kiran requests a MINI ---");
        Location pickup2 = new Location(12.9750, 77.5980, "Indiranagar");
        Location dest2 = new Location(12.9900, 77.6200, "Whitefield");

        String rideId2 = service.requestRide("R002", pickup2, dest2, VehicleType.MINI);
        service.acceptRide(rideId2, "D002");
        service.startRide(rideId2);
        Thread.sleep(1000);
        service.completeRide(rideId2, PaymentMethod.UPI);
        service.rateDriver(rideId2, 4);
        service.rateRider(rideId2, 5);

        // ===== RIDE 3: Cancelled =====
        System.out.println("\n--- Ride 3: Priya cancels a ride ---");
        Location pickup3 = new Location(12.9716, 77.5946, "MG Road");
        Location dest3 = new Location(12.9800, 77.6100, "Richmond Road");

        String rideId3 = service.requestRide("R001", pickup3, dest3, VehicleType.SEDAN);
        service.acceptRide(rideId3, "D001");
        service.cancelRide(rideId3, "Rider changed plans");

        // ===== ANALYTICS =====
        System.out.println("\n========================================");
        System.out.println("             ANALYTICS                  ");
        System.out.println("========================================");
        System.out.printf("Total Revenue:     ₹%.2f%n", service.getTotalRevenue());
        System.out.printf("Average Fare:      ₹%.2f%n", service.getAverageFare());
        System.out.printf("Average Distance:  %.2f km%n", service.getAverageRideDistance());
        System.out.println("Pending Rides:     " + service.getPendingRides());

        System.out.println("\nRides by Status:");
        service.getRidesByStatus().forEach((status, count) ->
                System.out.println("  " + status + ": " + count));

        System.out.println("\nRides by Vehicle Type:");
        service.getRidesByVehicleType().forEach((type, count) ->
                System.out.println("  " + type + ": " + count));

        System.out.println("\nTop Riders:");
        service.getTopRiders(3).forEach(r -> System.out.println("  " + r));

        System.out.println("\nTop Drivers:");
        service.getTopDrivers(3).forEach(d -> System.out.println("  " + d));

        System.out.println("\nBest Rated Drivers:");
        service.getBestRatedDrivers(3).forEach(d ->
                System.out.println("  " + d));

        System.out.println("\nDriver Earnings:");
        service.getDriverEarnings().forEach((driver, earnings) ->
                System.out.println("  " + driver + ": ₹" + earnings));

        System.out.println("\nRides in last 24 hours: " + service.getRidesInLastNHours(24).size());

        System.out.println("\nWallet Balances:");
        System.out.printf("  Priya: ₹%.2f%n", riderService.getWalletBalance("R001"));
        System.out.printf("  Kiran: ₹%.2f%n", riderService.getWalletBalance("R002"));

        // Export & Report
        System.out.println("\n--- Exporting Data ---");
        service.exportRides("rides.csv");
        service.generateReport("daily_report.txt");
        service.flush();

        System.out.println("\n========================================");
        System.out.println("         SYSTEM DEMO COMPLETE           ");
        System.out.println("========================================");
    }
}