package mock;

import java.util.*;
import java.util.stream.*;
class Book3 {
    String bookId;
    String title;
    String author;
    String genre;
    boolean isAvailable;

    public Book3() {
    }

    public Book3(String bookId, String title, String author, String genre, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isAvailable = isAvailable;
    }
}

class Member {
    String memberId;
    String name;
    String email;

    public Member() {
    }

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }
}

class BorrowRecord {
    String recordId;
    Member member;
    Book3 book;
    String borrowDate;   // "YYYY-MM-DD"
    String returnDate;   // null if not returned
    RecordStatus status;
    double fine;         // calculated on return

    public BorrowRecord() {}

    public BorrowRecord(String recordId, Member member, Book3 book,
                        String borrowDate, String returnDate,
                        RecordStatus status, double fine) {
        this.recordId = recordId;
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.fine = fine;
    }

    @Override
    public String toString() {
        return "BorrowRecord{" +
                "recordId='" + recordId + '\'' +
                ", member=" + member.name +
                ", book=" + book.title +
                ", status=" + status +
                ", fine=" + fine +
                '}';
    }
}

enum RecordStatus { BORROWED, RETURNED, OVERDUE }

interface ILibrarySystem {

    // Add book; print "[title] added" or "[title] already exists"
    void addBook(Book3 book);

    // Register member; print "[name] registered" or "[name] already exists"
    void registerMember(Member member);

    /**
     * Borrow a book:
     * - Book3 and Member must exist
     * - Book3 must be available
     * - A member cannot borrow more than 3 books at a time (BORROWED status)
     * - Assign recordId ("REC1", "REC2", ...), status = BORROWED, fine = 0
     * - Mark book as unavailable
     * - Return recordId on success, null on failure
     */
    String borrowBook(String memberId, String bookId, String borrowDate);

    /**
     * Return a book:
     * - Record must exist and be in BORROWED/OVERDUE status
     * - Calculate fine: if returnDate > borrowDate by more than 14 days → ₹10/extra day
     * - Mark book as available again
     * - Set status = RETURNED, set returnDate
     * - Print "Returned. Fine: ₹[amount]"
     */
    void returnBook(String recordId, String returnDate);

    /**
     * Mark overdue:
     * - Find all BORROWED records where borrowDate is more than 14 days before today
     * - today parameter is passed as String "YYYY-MM-DD"
     * - Set their status to OVERDUE
     * - Return count of records marked overdue
     */
    int markOverdueRecords(String today);

    // Return total fine collected from all RETURNED records for a given memberId
    double getTotalFineByMember(String memberId);

    // Return list of all books currently borrowed by a member (BORROWED or OVERDUE)
    List<Book3> getBooksBorrowedByMember(String memberId);

    // Return the member who has borrowed the most books overall (any status); null if none
    Member getMostActiveMember();
}

public class _12_LibrarySystem implements ILibrarySystem {
    Map<String,Book3> book3s = new HashMap<>();
    Map<String,Member> members = new HashMap<>();
    Map<String,BorrowRecord> borrowRecords = new HashMap<>();

    String OFFSET = "BRR";
    int i=1;

    @Override
    public void addBook(Book3 book) {
        if(book3s.putIfAbsent(book.bookId,book)==null){
            System.out.println(book.title+" added");
        }
        else{
            System.out.println(book.title +" already exists");
        }
    }

    @Override
    public void registerMember(Member member) {
        if(members.putIfAbsent(member.memberId,member)==null){
            System.out.println(member.name+" added");
        }
        else{
            System.out.println(member.name +" already exists");
        }
    }

    @Override
    public String borrowBook(String memberId, String bookId, String borrowDate) {
        Member member = members.get(memberId);
        Book3 book = book3s.get(bookId);

        if (member == null) { System.out.println("Member not found: " + memberId); return null; }
        if (book == null || !book.isAvailable) { System.out.println("Book unavailable: " + bookId); return null; }

        long activeBorrows = borrowRecords.values().stream()
                .filter(r -> r.member.memberId.equals(memberId) && r.status == RecordStatus.BORROWED)
                .count();
        if (activeBorrows >= 3) { System.out.println("Borrow limit reached for: " + memberId); return null; }

        String recId = OFFSET + i++;
        BorrowRecord br = new BorrowRecord(recId, member, book, borrowDate, null, RecordStatus.BORROWED, 0);
        borrowRecords.put(recId, br);
        book.isAvailable = false;
        System.out.println("Borrowed: " + recId);
        return recId;
    }
    @Override
    public void returnBook(String recordId, String returnDate) {
        BorrowRecord br = borrowRecords.get(recordId);
        if (br == null) { System.out.println("Record not found: " + recordId); return; }
        if (br.status == RecordStatus.RETURNED) { System.out.println("Already returned: " + recordId); return; }

        br.returnDate = returnDate;
        br.status = RecordStatus.RETURNED;
        br.book.isAvailable = true;

        // Fine calculation
        long days = daysBetween(br.borrowDate, returnDate);
        br.fine = days > 14 ? (days - 14) * 10.0 : 0;
        System.out.println("Returned. Fine: ₹" + br.fine);
    }

    // Helper
    private long daysBetween(String from, String to) {
        String[] f = from.split("-"), t = to.split("-");
        java.time.LocalDate d1 = java.time.LocalDate.of(Integer.parseInt(f[0]), Integer.parseInt(f[1]), Integer.parseInt(f[2]));
        java.time.LocalDate d2 = java.time.LocalDate.of(Integer.parseInt(t[0]), Integer.parseInt(t[1]), Integer.parseInt(t[2]));
        return java.time.temporal.ChronoUnit.DAYS.between(d1, d2);
    }

    @Override
    public int markOverdueRecords(String today) {
        int count = 0;
        for (BorrowRecord br : borrowRecords.values()) {
            if (br.status == RecordStatus.BORROWED && daysBetween(br.borrowDate, today) > 14) {
                br.status = RecordStatus.OVERDUE;
                count++;
            }
        }
        return count;
    }

    @Override
    public double getTotalFineByMember(String memberId) {
        return borrowRecords.values().stream().filter(obj -> obj.member.memberId.equals(memberId)).mapToDouble(obj->obj.fine).sum();
    }

    @Override
    public List<Book3> getBooksBorrowedByMember(String memberId) {
        return borrowRecords.values().stream().filter(obj -> obj.member.memberId.equals(memberId) &&
                        (obj.status == RecordStatus.BORROWED || obj.status == RecordStatus.OVERDUE)).
                map(obj->obj.book).collect(Collectors.toList());
    }

    @Override
    public Member getMostActiveMember() {
        return borrowRecords.values().stream()
                .collect(Collectors.groupingBy(r -> r.member, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}

class LibraryTest {
    public static void main(String[] args) {

        ILibrarySystem system = new _12_LibrarySystem();

        System.out.println("========== ADD BOOKS ==========");
        Book3 b1 = new Book3(); b1.bookId="B001"; b1.title="Clean Code";
        b1.author="Robert Martin"; b1.genre="Tech"; b1.isAvailable=true;

        Book3 b2 = new Book3(); b2.bookId="B002"; b2.title="Atomic Habits";
        b2.author="James Clear"; b2.genre="Self-Help"; b2.isAvailable=true;

        Book3 b3 = new Book3(); b3.bookId="B003"; b3.title="Deep Work";
        b3.author="Cal Newport"; b3.genre="Self-Help"; b3.isAvailable=true;

        Book3 b4 = new Book3(); b4.bookId="B004"; b4.title="DDIA";
        b4.author="Martin Kleppmann"; b4.genre="Tech"; b4.isAvailable=true;

        system.addBook(b1); system.addBook(b2);
        system.addBook(b3); system.addBook(b4);
        system.addBook(b1); // duplicate

        System.out.println("\n========== REGISTER MEMBERS ==========");
        Member m1 = new Member(); m1.memberId="M001"; m1.name="Animesh"; m1.email="a@gmail.com";
        Member m2 = new Member(); m2.memberId="M002"; m2.name="Rahul";   m2.email="r@gmail.com";

        system.registerMember(m1); system.registerMember(m2);
        system.registerMember(m1); // duplicate

        System.out.println("\n========== BORROW BOOKS ==========");
        String r1 = system.borrowBook("M001", "B001", "2025-06-01");
        System.out.println("Record: " + r1); // REC1

        String r2 = system.borrowBook("M001", "B002", "2025-06-01");
        System.out.println("Record: " + r2); // REC2

        String r3 = system.borrowBook("M001", "B003", "2025-06-01");
        System.out.println("Record: " + r3); // REC3

        // M001 already has 3 — should FAIL
        String r4 = system.borrowBook("M001", "B004", "2025-06-01");
        System.out.println("Record (should be null): " + r4);

        // Book3 already borrowed — should FAIL
        String r5 = system.borrowBook("M002", "B001", "2025-06-01");
        System.out.println("Record (should be null): " + r5);

        // Valid — M002 borrows B004
        String r6 = system.borrowBook("M002", "B004", "2025-06-01");
        System.out.println("Record: " + r6); // REC4 (counter continues)

        System.out.println("\n========== RETURN BOOKS ==========");
        // On time return (within 14 days) — fine = 0
        system.returnBook(r1, "2025-06-10");

        // Late return — 20 days → 6 extra days → fine = ₹60
        system.returnBook(r2, "2025-06-21");

        // Return already returned — should fail
        system.returnBook(r1, "2025-06-25");

        // Invalid recordId
        system.returnBook("REC999", "2025-06-10");

        System.out.println("\n========== MARK OVERDUE ==========");
        // r3 borrowed on 2025-06-01, today = 2025-06-20 → 19 days → overdue
        int count = system.markOverdueRecords("2025-06-20");
        System.out.println("Overdue count: " + count); // 1 (r3)

        System.out.println("\n========== TOTAL FINE ==========");
        System.out.println("Fine M001: " + system.getTotalFineByMember("M001")); // 60.0
        System.out.println("Fine M002: " + system.getTotalFineByMember("M002")); // 0.0

        System.out.println("\n========== BOOKS BORROWED BY MEMBER ==========");
        List<Book3> active = system.getBooksBorrowedByMember("M001");
        // Expected: Deep Work only (B001=RETURNED, B002=RETURNED, B003=OVERDUE still not returned)
        for (Book3 b : active) System.out.println(b.title);

        System.out.println("\n========== MOST ACTIVE MEMBER ==========");
        Member active2 = system.getMostActiveMember();
        System.out.println("Most active: " + (active2 != null ? active2.name : "None"));
        // Expected: Animesh (3 total records vs Rahul's 1)

        System.out.println("\n🎉 ALL TESTS COMPLETED 🎉");
    }
}