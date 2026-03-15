package com.example.restapp.service;

import com.example.restapp.model.Book;
import com.example.restapp.model.BorrowingRecord;
import com.example.restapp.model.Member;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    // In-memory storage using ArrayList
    private List<Book> books = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<BorrowingRecord> borrowingRecords = new ArrayList<>();

    private long bookIdCounter = 1;
    private long memberIdCounter = 1;
    private long recordIdCounter = 1;

    // ==================== Book Methods ====================

    // Get all books
    public List<Book> getAllBooks() {
        return books;
    }

    // Get a book by ID
    public Optional<Book> getBookById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    // Get books by title
    public List<Book> getBooksByTitle(String title) {
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList();
    }

    // Add a new book
    public void addBook(Book book) {
        book.setId(bookIdCounter++);
        books.add(book);
    }

    // Update a book
    public void updateBook(Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getId().equals(updatedBook.getId())) {
                books.set(i, updatedBook);
                break;
            }
        }
    }

    // Delete a book by ID
    public void deleteBook(Long id) {
        books.removeIf(book -> book.getId().equals(id));
    }

    // ==================== Member Methods ====================

    // Get all members
    public List<Member> getAllMembers() {
        return members;
    }

    // Get a member by ID
    public Optional<Member> getMemberById(Long id) {
        return members.stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    // Add a new member
    public void addMember(Member member) {
        member.setId(memberIdCounter++);
        members.add(member);
    }

    // Update a member
    public void updateMember(Member updatedMember) {
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getId().equals(updatedMember.getId())) {
                members.set(i, updatedMember);
                break;
            }
        }
    }

    // Delete a member by ID
    public void deleteMember(Long id) {
        members.removeIf(member -> member.getId().equals(id));
    }

    // ==================== BorrowingRecord Methods ====================

    // Get all borrowing records
    public List<BorrowingRecord> getAllBorrowingRecords() {
        return borrowingRecords;
    }

    // Borrow a book (create a new borrowing record)
    public void borrowBook(BorrowingRecord record) {
        // Set borrow date and due date (e.g., due date = borrow date + 14 days)
        record.setId(recordIdCounter++);
        record.setBorrowDate(LocalDate.now());
        record.setDueDate(LocalDate.now().plusDays(14));
        borrowingRecords.add(record);

        // Decrease the available copies of the book
        Book book = record.getBook();
        if (book != null) {
            book.setAvailableCopies(book.getAvailableCopies() - 1);
        }
    }

    // Return a book (update the borrowing record with the return date)
    public void returnBook(Long recordId, LocalDate returnDate) {
        for (BorrowingRecord record : borrowingRecords) {
            if (record.getId().equals(recordId)) {
                record.setReturnDate(returnDate);

                // Increase the available copies of the book
                Book book = record.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                break;
            }
        }
    }
}
