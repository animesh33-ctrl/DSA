package mock;

import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

enum Rating {
    ONE_STAR(1), TWO_STAR(2), THREE_STAR(3), FOUR_STAR(4), FIVE_STAR(5);

    private final int value;

    Rating(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

class Review {
    String reviewId;
    String productId;
    String userId;
    Rating rating;
    String comment;
    long timestamp;
    int helpfulCount;

    public Review(String reviewId, String productId, String userId,
                  Rating rating, String comment) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = System.currentTimeMillis();
        this.helpfulCount = 0;
    }
    public Review(String reviewId, String productId, String userId,
                  Rating rating, String comment, long timestamp) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = timestamp;
        this.helpfulCount = 0;
    }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId='" + reviewId + '\'' +
                ", productId='" + productId + '\'' +
                ", userId='" + userId + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", timestamp=" + timestamp +
                ", helpfulCount=" + helpfulCount +
                '}';
    }

    public String toCSV() {
        return reviewId + "," +
                productId + "," +
                userId + "," +
                rating.getValue() + "," +
                comment.replace(",", ";") + "," +
                timestamp;
    }

    private static Rating getRatingFromValue(int value) throws InvalidReviewException {
        return switch (value) {
            case 1 -> Rating.ONE_STAR;
            case 2 -> Rating.TWO_STAR;
            case 3 -> Rating.THREE_STAR;
            case 4 -> Rating.FOUR_STAR;
            case 5 -> Rating.FIVE_STAR;
            default -> throw new InvalidReviewException("Invalid rating value: " + value);
        };
    }

    public static Review fromCSV(String line) throws InvalidReviewException {

        if (line == null || line.trim().isEmpty()) {
            throw new InvalidReviewException("CSV line is empty");
        }

        String[] split = line.split(",");

        if (split.length != 6) {
            throw new InvalidReviewException(
                    "Invalid CSV format. Expected 6 fields but got " + split.length
            );
        }

        try {
            String reviewId = split[0].trim();
            String productId = split[1].trim();
            String userId = split[2].trim();

            int ratingValue = Integer.parseInt(split[3].trim());
            Rating rating = getRatingFromValue(ratingValue);

            String comment = split[4].trim().replace(";", ",");
            long timestamp = Long.parseLong(split[5].trim());

            return new Review(
                    reviewId, productId, userId,
                    rating, comment,
                    timestamp
            );

        } catch (Exception e) {
            throw new InvalidReviewException("Error parsing CSV: " + e.getMessage());
        }
    }
}

class InvalidReviewException extends Exception {
    public InvalidReviewException(String message) {
        super(message);
    }
}

interface IReviewStore {
    void storeReviews(Queue<Review> reviews) throws IOException;
    Queue<Review> getAllReviews();
    void saveToFile(String filename) throws IOException;
    void loadFromFile(String filename) throws IOException, InvalidReviewException;
}

interface IReviewSystem {
    void submitReview(String productId, String userId, Rating rating, String comment);
    int getTotalReviews();
    int getPendingReviews();
    void flush() throws IOException;

    // Stream-based analyticsx`
    Map<String, Long> getReviewCountByProduct();
    Map<Rating, Long> getRatingDistribution();
    double getAverageRating();

    // Lambda filters
    List<Review> getReviewsByProduct(String productId);
    List<Review> getReviewsByUser(String userId);
    List<Review> getReviewsByRating(Rating rating);
    List<Review> getPositiveReviews(); // 4 and 5 stars
    List<Review> getNegativeReviews(); // 1 and 2 stars

    // Advanced analytics
    Map<String, Double> getAverageRatingByProduct();
    List<String> getTopRatedProducts(int n);
    List<String> getWorstRatedProducts(int n);
    List<String> getMostReviewedProducts(int n);

    // File operations
    void exportReviews(String filename) throws IOException;
    void importReviews(String filename) throws IOException, InvalidReviewException;
    void generateReport(String filename) throws IOException, ReportException;
}

class ReportException extends Exception {
    public ReportException(String message) {
        super(message);
    }
}


class ReviewStore implements IReviewStore{
    Queue<Review> reviewQueue = new LinkedList<>();

    @Override
    public void storeReviews(Queue<Review> reviews) throws IOException {
        while(!reviews.isEmpty()){
            reviewQueue.offer(reviews.poll());
        }
        System.out.println("Reviews successfully added");
    }

    @Override
    public Queue<Review> getAllReviews() {
        return new LinkedList<>(reviewQueue);
    }

    @Override
    public void saveToFile(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(Review r : reviewQueue) {
                writer.write(r.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void loadFromFile(String filename) throws IOException, InvalidReviewException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String line = reader.readLine();
            while(line != null){
                reviewQueue.offer(Review.fromCSV(line));
                line = reader.readLine();
            }
        }
    }
}

class ReviewSystem implements IReviewSystem{
    Queue<Review> reviews = new LinkedList<>();
    IReviewStore reviewStore;
    int batchSize;
    String offset = "REVIEW";
    int num = 1;

    public ReviewSystem(int batchSize, IReviewStore reviewStore) {
        this.batchSize = batchSize;
        this.reviewStore = reviewStore;
    }

    public ReviewSystem() {}

    @Override
    public void submitReview(String productId, String userId, Rating rating, String comment) {
        reviews.offer(new Review(offset+num++,productId,userId,rating,comment));


        if(reviews.size() >= batchSize){
            try {
                reviewStore.storeReviews(reviews);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getTotalReviews() {
        return reviewStore.getAllReviews().size();
    }

    @Override
    public int getPendingReviews() {
        return reviews.size();
    }

    @Override
    public void flush() throws IOException {
        reviewStore.storeReviews(reviews);
    }

    @Override
    public Map<String, Long> getReviewCountByProduct() {
        return reviewStore.getAllReviews().stream().collect(Collectors.groupingBy(e->e.productId,Collectors.counting()));
    }

    @Override
    public Map<Rating, Long> getRatingDistribution() {
        return reviewStore.getAllReviews().stream().collect(Collectors.groupingBy(obj -> obj.rating, Collectors.counting()));
    }

    @Override
    public double getAverageRating() {
        return reviewStore.getAllReviews().stream().mapToDouble(obj -> obj.rating.getValue()).average().orElse(0.0);
    }

    @Override
    public List<Review> getReviewsByProduct(String productId) {
        return reviewStore.getAllReviews().stream().filter( obj -> obj.productId.equalsIgnoreCase(productId)).collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByUser(String userId) {
        return reviewStore.getAllReviews().stream().filter( obj -> obj.userId.equalsIgnoreCase(userId)).collect(Collectors.toList());
    }

    @Override
    public List<Review> getReviewsByRating(Rating rating) {
        return reviewStore.getAllReviews().stream().filter( obj -> obj.rating.getValue() == rating.getValue()).collect(Collectors.toList());
    }

    @Override
    public List<Review> getPositiveReviews() {
        return reviewStore.getAllReviews().stream().filter( obj -> obj.rating.getValue() >= 4).collect(Collectors.toList());
    }

    @Override
    public List<Review> getNegativeReviews() {
        return reviewStore.getAllReviews().stream().filter( obj -> obj.rating.getValue() <= 2).collect(Collectors.toList());
    }

    @Override
    public Map<String, Double> getAverageRatingByProduct() {
        return reviewStore.getAllReviews().stream().collect(Collectors.groupingBy(obj->obj.productId,Collectors.averagingDouble(obj -> obj.rating.getValue())));
    }


    @Override
    public List<String> getTopRatedProducts(int n) {
        return getAverageRatingByProduct()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getWorstRatedProducts(int n) {
        return getAverageRatingByProduct()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())  // Ascending (lowest first)
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getMostReviewedProducts(int n) {
        return getReviewCountByProduct()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(n)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public void exportReviews(String filename) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            for(Review r : reviewStore.getAllReviews()){
                writer.write(r.toCSV());
                writer.newLine();
            }
        }
    }

    @Override
    public void importReviews(String filename) throws IOException, InvalidReviewException {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){

            String line = reader.readLine();
            while (line != null){
                reviews.offer(Review.fromCSV(line));
                if(reviews.size() >= batchSize){
                    flush();
                }
                line = reader.readLine();
            }
        }


    }

    @Override
    public void generateReport(String filename) throws IOException, ReportException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("=== Product Review Report ===\n\n");

            writer.write("Total Reviews: " + getTotalReviews() + "\n");
            writer.write("Average Rating: " + String.format("%.2f", getAverageRating()) + "\n\n");

            writer.write("--- Rating Distribution ---\n");
            getRatingDistribution().forEach((rating, count) -> {
                try {
                    writer.write(rating + ": " + count + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.write("\n--- Top Rated Products ---\n");
            getTopRatedProducts(5).forEach(productId -> {
                try {
                    Double avgRating = getAverageRatingByProduct().get(productId);
                    writer.write(productId + " - Avg: " + String.format("%.2f", avgRating) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.write("\n--- All Reviews ---\n");
            for (Review r : reviewStore.getAllReviews()) {
                writer.write(r.toCSV() + "\n");
            }

        } catch (IOException e) {
            throw new ReportException("Failed to generate report: " + e.getMessage());
        }
    }
}

public class _07_ProductReview {
    public static void main(String[] args) throws Exception {

        System.out.println("===== INITIALIZING REVIEW SYSTEM =====");

        IReviewStore store = new ReviewStore();
        IReviewSystem system = new ReviewSystem(3, store);

        /* ---------------------------------------------------
           1️⃣ SUBMIT REVIEWS (BATCH TESTING)
        --------------------------------------------------- */
        system.submitReview("P1001", "U1", Rating.FIVE_STAR, "Excellent product");
        system.submitReview("P1001", "U2", Rating.FOUR_STAR, "Very good");
        system.submitReview("P1002", "U1", Rating.TWO_STAR, "Not satisfied");

        System.out.println("Pending Reviews (after 3 submits): " + system.getPendingReviews());
        System.out.println("Total Reviews (stored): " + system.getTotalReviews());

        system.submitReview("P1003", "U3", Rating.THREE_STAR, "Average");
        system.submitReview("P1002", "U4", Rating.ONE_STAR, "Worst quality");
        system.submitReview("P1001", "U5", Rating.FIVE_STAR, "Loved it");

        System.out.println("Pending Reviews (before flush): " + system.getPendingReviews());

        system.flush();

        System.out.println("Pending Reviews (after flush): " + system.getPendingReviews());
        System.out.println("Total Reviews (after flush): " + system.getTotalReviews());

        /* ---------------------------------------------------
           2️⃣ BASIC STREAM ANALYTICS
        --------------------------------------------------- */
        System.out.println("\n===== BASIC ANALYTICS =====");

        System.out.println("Review Count By Product:");
        system.getReviewCountByProduct()
                .forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("\nRating Distribution:");
        system.getRatingDistribution()
                .forEach((k, v) -> System.out.println(k + " -> " + v));

        System.out.println("\nOverall Average Rating:");
        System.out.println(system.getAverageRating());

        /* ---------------------------------------------------
           3️⃣ FILTER OPERATIONS
        --------------------------------------------------- */
        System.out.println("\n===== FILTERS =====");

        System.out.println("Reviews for Product P1001:");
        system.getReviewsByProduct("P1001")
                .forEach(r -> System.out.println(r.toCSV()));

        System.out.println("\nReviews by User U1:");
        system.getReviewsByUser("U1")
                .forEach(r -> System.out.println(r.toCSV()));

        System.out.println("\nReviews with FIVE STAR:");
        system.getReviewsByRating(Rating.FIVE_STAR)
                .forEach(r -> System.out.println(r.toCSV()));

        System.out.println("\nPositive Reviews (4 & 5 stars):");
        system.getPositiveReviews()
                .forEach(r -> System.out.println(r.productId + " -> " + r.rating));

        System.out.println("\nNegative Reviews (1 & 2 stars):");
        system.getNegativeReviews()
                .forEach(r -> System.out.println(r.productId + " -> " + r.rating));

        /* ---------------------------------------------------
           4️⃣ ADVANCED ANALYTICS
        --------------------------------------------------- */
        System.out.println("\n===== ADVANCED ANALYTICS =====");

        System.out.println("Average Rating By Product:");
        system.getAverageRatingByProduct()
                .forEach((k, v) -> System.out.println(k + " -> " + String.format("%.2f", v)));

        System.out.println("\nTop 2 Rated Products:");
        system.getTopRatedProducts(2)
                .forEach(System.out::println);

        System.out.println("\nWorst 2 Rated Products:");
        system.getWorstRatedProducts(2)
                .forEach(System.out::println);

        System.out.println("\nMost Reviewed Products:");
        system.getMostReviewedProducts(2)
                .forEach(System.out::println);

        /* ---------------------------------------------------
           5️⃣ FILE EXPORT TEST
        --------------------------------------------------- */
        System.out.println("\n===== FILE EXPORT =====");
        String exportFile = "reviews_export.csv";
        system.exportReviews(exportFile);
        System.out.println("Reviews exported to " + exportFile);

        /* ---------------------------------------------------
           6️⃣ FILE IMPORT TEST
        --------------------------------------------------- */
        System.out.println("\n===== FILE IMPORT =====");
        IReviewStore newStore = new ReviewStore();
        IReviewSystem importedSystem = new ReviewSystem(2, newStore);

        importedSystem.importReviews(exportFile);
        importedSystem.flush();

        System.out.println("Imported Reviews Count:");
        System.out.println(importedSystem.getTotalReviews());

        System.out.println("Imported Review Count By Product:");
        importedSystem.getReviewCountByProduct()
                .forEach((k, v) -> System.out.println(k + " -> " + v));

        /* ---------------------------------------------------
           7️⃣ CSV PARSING VALIDATION
        --------------------------------------------------- */
        System.out.println("\n===== CSV PARSING TEST =====");

        String csvLine = "R999,P999,U99,5,Excellent item,1700000000000";
        Review parsed = Review.fromCSV(csvLine);
        System.out.println("Parsed Review:");
        System.out.println(parsed.toCSV());

        /* ---------------------------------------------------
           8️⃣ REPORT GENERATION
        --------------------------------------------------- */
        System.out.println("\n===== REPORT GENERATION =====");

        String reportFile = "review_report.txt";
        system.generateReport(reportFile);
        System.out.println("Report generated at: " + reportFile);

        /* ---------------------------------------------------
           9️⃣ FINAL CONSISTENCY CHECK
        --------------------------------------------------- */
        System.out.println("\n===== FINAL CHECK =====");
        System.out.println("Original System Reviews: " + system.getTotalReviews());
        System.out.println("Imported System Reviews: " + importedSystem.getTotalReviews());

        System.out.println("\n===== ALL FUNCTIONS TESTED SUCCESSFULLY ✅ =====");
    }
}