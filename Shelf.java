import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Name: Diego Zavala
 * Date: 3/1/2023
 * Description: This is a program that represents a shelf with books and the amount of books in a Library
 */

public class Shelf {
    /////Constant Fields/////
    public static final int SHELF_NUMBER_ = 0;
    public static final int SUBJECT_ = 1;
    /////Fields/////
    private int shelfNumber;
    private String subject;
    private HashMap<Book, Integer> books;

    /////Constructor/////
    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
        this.books = new HashMap<>(); //not in clink code

    }
    /////Getters and Setters/////
    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }
    /////Equals, HashCode, and toString/////
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (getShelfNumber() != shelf.getShelfNumber()) return false;
        return getSubject() != null ? getSubject().equals(shelf.getSubject()) : shelf.getSubject() == null;
    }

    @Override
    public int hashCode() {
        int result = getShelfNumber();
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return shelfNumber + ": " + subject;
    }

    /////Methods/////
    public int getBookCount(Book book){
        if(books.containsKey(book)){
            return books.get(book);
        }
        return -1;
    }

    public Code addBook(Book book){ //not sure if I am adding correctly
        int count = 0;
        if(books.containsKey(book)){
            count = books.get(book);
            count++;
            books.put(book, count);
            System.out.println(book.toString() + " added to shelf " + this.toString()); //this.toString
            return Code.SUCCESS;
        }else if((!books.containsKey(book)) && book.getSubject().equals(subject)){
            count = 1;
            books.put(book, count);
            System.out.println(book.toString() + " added to shelf " + this.toString());
            return Code.SUCCESS;
        }else if((!books.containsKey(book)) && (!book.getSubject().equals(subject))){
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
//        System.out.println("HashMap: " + books); testing
        return Code.SHELF_SUBJECT_MISMATCH_ERROR;
    }

    public Code removeBook(Book book){
        if(!books.containsKey(book)){
//            System.out.println("KeySet: " + books.keySet());
            System.out.println(book.getTitle() + " is not on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }else if(books.containsKey(book) && books.get(book).equals(0)){
            System.out.println("No copies of " + book.getTitle() + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }else{ //if(books.containsKey(book) && books.get(book) > 0){
            int count = books.get(book);
            count--;
            books.replace(book, books.get(book), count);
            System.out.println(book + " successfully removed from the shelf " + subject);
            return Code.SUCCESS;
        }
    }

    public String listBooks(){
        StringBuilder builder = new StringBuilder();
        int count = 0;
        for(Book book: books.keySet()){
            count += this.getBookCount(book);
        }
        builder.append(count + " books on shelf: ");
        builder.append(this);
        for(Book book: books.keySet()){
            builder.append("\n" + book + " " + this.getBookCount(book));
        }

        System.out.println(builder.toString());
        return builder.toString();
    }

}
