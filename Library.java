import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Name: Diego Zavala
 * Date: 3/6/2023
 * Description: This is a program that represents a Library with shelves, readers, and books
 */

public class Library {
    ///////////////Constant Fields///////////////

    public static final int LENDING_LIMIT = 5;

    ///////////////Fields///////////////

    private String name;
    private static int libraryCard;
    private List<Reader> readers;
    private HashMap<String, Shelf> shelves;
    private HashMap<Book, Integer> books;

    ///////////////Constructor///////////////

    public Library(String name) {
        this.name = name;
        shelves = new HashMap<>();
        books = new HashMap<>();
        this.readers = new ArrayList<>();
    }

    ///////////////Methods////////////////

    public Code init(String filename){
        try{
            int num;
            String numOfRecords;
            Code errMessage;
            File file = new File(filename);
            Scanner scan = new Scanner(file);

            //Parsing Books starts
            numOfRecords = scan.nextLine();
            System.out.println("Parsing " + numOfRecords + " books");
            num = convertInt(numOfRecords, Code.BOOK_COUNT_ERROR);
            errMessage = initBooks(num, scan);
            if(num < 0){
                return errMessage;
            }else{
                System.out.println("SUCCESS");
            }
            listBooks();
            System.out.println();

            //Parsing Shelves starts
            numOfRecords = scan.nextLine();
            System.out.println("Parsing " + numOfRecords + " shelves");
            num = convertInt(numOfRecords, Code.SHELF_COUNT_ERROR);
            errMessage = initShelves(num, scan);
            if(num < 0){
                return errMessage;
            }else{
                System.out.println("SUCCESS");
            }
            listShelves(true);
            System.out.println();

            //Parsing Readers starts
            numOfRecords = scan.nextLine();
            System.out.println("Parsing " + numOfRecords + " readers");
            num = convertInt(numOfRecords, Code.READER_COUNT_ERROR);
            errMessage = initReader(num, scan);
            if(num < 0){
                return errMessage;
            }else{
                System.out.println("SUCCESS");
            }
            listReaders();
            System.out.println();

        }catch(FileNotFoundException e){
            return Code.FILE_NOT_FOUND_ERROR;
        }

        return Code.SUCCESS;
    }

    private Code initBooks(int bookCount, Scanner scan){
        String currLine;
        int count = 0;
        HashMap<Integer, String> properties = new HashMap<>(); //holds the properties of the book like isbn, name,...
        if(bookCount < 1){
            return Code.LIBRARY_ERROR;
        }
        while(bookCount != 0){//runs until bookCount is 0
            currLine = scan.nextLine(); //sets the line to a string
            if(currLine.length() != 0){
                count = 0;
                for(String word: currLine.split(",")) { //splits the line into each property
                    properties.put(count, word); //puts each split word into a hashmap
                    count++;
                }
            }
            //parse the page number
            int pageNum = convertInt(properties.get(Book.PAGE_COUNT_), Code.PAGE_COUNT_ERROR); //change CODE parameter
            if(pageNum < 0){
                return Code.PAGE_COUNT_ERROR;
            }
            //parse the date
            LocalDate bookDate = convertDate(properties.get(Book.DUE_DATE_), Code.DATE_CONVERSION_ERROR); //Change the CODE parameter
            if(bookDate.equals(null)){
                return Code.DATE_CONVERSION_ERROR;
            }
            //creates a book with all the properties we got as parameters
            Book book = new Book(properties.get(Book.ISBN_),properties.get(Book.TITLE_), properties.get(Book.SUBJECT_), pageNum, properties.get(Book.AUTHOR_), bookDate);
            System.out.println("parsing book: " + book.getIsbn() + ", " + book.getTitle() + ", "+book.getSubject() + ", "+ book.getPageCount() + ", " + book.getAuthor() + ", " + book.getDueDate());
            addBook(book);//adds the book
            bookCount--;
        }

        return Code.SUCCESS;
    }

    private Code initShelves(int shelfCount, Scanner scan){
        String currLine;
        int shelvesCount = shelfCount;
        int count = 0;
        HashMap<Integer, String> properties = new HashMap<>(); //holds the properties of the book like num and name
        if(shelfCount < 1){
            return Code.SHELF_COUNT_ERROR;
        }
        while(shelfCount != 0){
            currLine = scan.nextLine();
            if(currLine.length() != 0){
                count = 0;
                for(String word: currLine.split(",")) { //splits the line into each property
                    properties.put(count, word); //puts each split word into a hashmap
                    count++;
                }
            }
            //parse the shelf number
            int shelfNum = convertInt(properties.get(Shelf.SHELF_NUMBER_), Code.SHELF_NUMBER_PARSE_ERROR); //change code parameter
            if(shelfNum < 1){
                return Code.SHELF_NUMBER_PARSE_ERROR;
            }
            Shelf shelf = new Shelf(shelfNum, properties.get(Shelf.SUBJECT_));
            System.out.println("Parsing Shelf: " + shelf.toString());
            addShelf(shelf);
            shelfCount--;
        }
        if(shelves.size() == shelvesCount){
            return Code.SUCCESS;
        }
        System.out.println("Number of shelves doesn't match expected");
        return Code.SHELF_NUMBER_PARSE_ERROR;
    }

    private Code initReader(int readerCount, Scanner scan){
        String currLine;
        int count = 0;
        HashMap<Integer, String> properties = new HashMap<>(); //holds the properties of the book like num and name
        if(readerCount <= 0){
            return Code.READER_COUNT_ERROR;
        }
        while(readerCount != 0){
            currLine = scan.nextLine();
            count = 0;
            if(currLine.length() != 0){
                for(String word: currLine.split(",")){
                    properties.put(count, word);
                    count++;
                }
            }
            //num of books to parse
            int bookNum = convertInt(properties.get(Reader.BOOK_COUNT_), Code.BOOK_COUNT_ERROR);//error code

            //card number parsed
            int cardNum = convertInt(properties.get(Reader.CARD_NUMBER_), Code.READER_CARD_NUMBER_ERROR);//Error code might need change
            Reader reader = new Reader(cardNum, properties.get(Reader.NAME_),properties.get(Reader.PHONE_));

            addReader(reader);
            for(int j = 0;j < bookNum; j++){
                Book book = getBookByISBN(properties.get(Reader.BOOK_START_ +(j*2)));
                if(book.equals(null)){
                    System.out.println("ERROR");
                }
                book.setDueDate(convertDate(properties.get(Reader.BOOK_START_+(j*2)+1),Code.DATE_CONVERSION_ERROR));
                shelves.get(book.getSubject()).removeBook(book);
                reader.addBook(book);
                System.out.println("SUCCESS");
            }

            readerCount--;
        }
        return Code.SUCCESS;
    }

    public Code addBook(Book newBook){
        int count = 0;
        if(books.containsKey(newBook)){ //checks if book is present, adds 1 to count
            count = books.get(newBook);
            count++;
            books.put(newBook, count);
            System.out.println(count + " copies of " + newBook.toString() + " in the stacks. ");
        }else if(!books.containsKey(newBook)){// checks if book is not present, sets count to 1
            count = 1;
            books.put(newBook, count);
            System.out.println(newBook.toString() + " added to the stacks. ");
        }
        if(shelves.containsKey(newBook.getSubject())){ //checks if shelf is present in shelves hashmap
            addBookToShelf(newBook, shelves.get(newBook.getSubject()));  //might be wrong

            return Code.SUCCESS;
        }
        System.out.println("No shelf for " + newBook.getSubject() + " books. ");
        return Code.SHELF_EXISTS_ERROR;
    }

    public Code returnBook(Reader reader, Book book){
        Code codeResult;
        if(!reader.hasBook(book)){ //checks if reader doesn't have the book
            System.out.println(reader.getName() + " doesn't have " + book.getTitle() + " checked out. ");
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }if(this.getBookByISBN(book.getIsbn()).equals(null)){
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        System.out.println(reader.getName() + " is returning " + book);
        codeResult = reader.removeBook(book);
        if(codeResult.equals(Code.SUCCESS)){ //check if the result is success
            return returnBook(book);
        }else{
            System.out.println("Could not return " + book);
            return codeResult;
        }
    }

    public Code returnBook(Book book){
        if(!shelves.containsKey(book.getSubject())){ //checks if the shelves dont contain the subject
            System.out.println("No shelf for " + book);
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.get(book.getSubject()).addBook(book); // adds the book to the respective shelf
        return Code.SUCCESS; //this is just so the error goes away
    }

    private Code addBookToShelf(Book book, Shelf shelf){
        Code codeResult;
        codeResult = returnBook(book);
        if(codeResult.equals(Code.SUCCESS)){ //checks if the returnBook method outputs success
            return Code.SUCCESS;
        }
        if(!shelf.equals(book.getSubject())){ //checks if the shelf subject is not the same as the book's
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
        codeResult = shelf.addBook(book); //sets the variable to the output of addBook
        if(codeResult.equals(book + " added to shelf ")){
            return Code.SUCCESS;
        }
        System.out.println("Could not add " + book + " to shelf");
        return codeResult;
    }

    public int listBooks(){ //find way to loop through all shelves and/or books
        StringBuilder builder = new StringBuilder();
        int count = 0;
        int totalCount = 0;
//        System.out.println(books);
        for(Book book1: books.keySet()){ //this needs to loop 4 times for the example
            count = books.get(book1);
            totalCount += count;
            builder.append("\n" + count + " copies of " + book1);
            count = 0;
        }

        System.out.println(builder);
        System.out.println("\n");
        return totalCount;
    }

    public Code checkOutBook(Reader reader, Book book){
        if(!readers.contains(reader)){
            System.out.printf(reader.getName() + " doesn't have an account here. ");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }
        if(reader.getBookCount() > LENDING_LIMIT){
            System.out.println(reader.getName() + " has reached the lending limit, " + LENDING_LIMIT);
            return Code.BOOK_LIMIT_REACHED_ERROR;
        }
        if(!books.containsKey(book)){
            System.out.println("ERROR: could not find " + book);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        if(!shelves.containsKey(book.getSubject())){
            System.out.println("No shelf for " + book.getSubject() + " books!");
            return Code.SHELF_EXISTS_ERROR;
        }
        if(shelves.get(book.getSubject()).getBookCount(book) < 1){
            System.out.println("ERROR: no copies of " + book + " remain.");
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }
        Code codeResult = reader.addBook(book);
        if(!codeResult.equals(Code.SUCCESS)){
            System.out.println("Couldn't checkout " + book);
            return codeResult;
        }
        codeResult = shelves.get(book.getSubject()).removeBook(book);
        if(codeResult.equals(Code.SUCCESS)){
            System.out.println(book + " checked out successfully.");
        }
        return codeResult;
    }

    public Book getBookByISBN(String isbn){
        for(Book book: books.keySet()){ //checks the hashmap of books for every book
            if(book.getIsbn().equals(isbn)){ //checks the isbn of every book in the hashmap
                return book;
            }
        }
        System.out.println("ERROR: Could not find a book with isbn: " + isbn);
        return null;
    }

    public Code listShelves(boolean showBooks){
        if(showBooks == true){
            System.out.println();
            for(Shelf shelf:shelves.values()){
               shelf.listBooks();
            }
        }for(Shelf shelf:shelves.values()){
            shelf.toString();
        }
        return Code.SUCCESS;
    }

    public Code addShelf(String shelfSubject){
        Shelf newShelf = new Shelf(shelves.size()+1, shelfSubject);
        addShelf(newShelf);
        return Code.SUCCESS; //here to not cause return error
    }

    public Code addShelf(Shelf shelf){
        if(shelves.containsValue(shelf)){ //checks if the shelf already exists
            System.out.println("ERROR: Shelf already exists " + shelf);
            return Code.SHELF_EXISTS_ERROR;
        }
        shelves.put(shelf.getSubject(), shelf);
        shelf.setShelfNumber(shelves.size());
        for(Book book:books.keySet()){
            int bookAmount = books.get(book);
            for(int i = 0; i < bookAmount; i++) {
                if (book.getSubject().equals(shelf.getSubject())) {
                    shelf.addBook(book);
                }
            }
        }
        return Code.SUCCESS;
    }

    public Shelf getShelf(Integer shelfNumber){
        for(Shelf shelf: shelves.values()){
            if(shelf.getShelfNumber() == shelfNumber){
                return shelf;
            }
//                System.out.println("No shelf number " + shelfNumber + " found");

        }
        System.out.println("No shelf number " + shelfNumber + " found");
        return null;
    }

    public Shelf getShelf(String subject){
        for(Shelf shelf:shelves.values()){
            if(shelf.getSubject().equals(subject)){
                return shelf;
            }
        }
        System.out.println("No shelf for " + subject + " books");
        return null;
    }

    public int listReaders(){
        for(Reader reader: readers){
            System.out.println(reader.toString());
        }
//        System.out.println(readers.toString());
        return readers.size();
    }

    public int listReaders(boolean showBooks){
        int count = 0;

        if(showBooks == true){
            for(Reader reader:readers){
                count++;
                System.out.println(reader.getName() + "(#" + count + ") has the following books: ");
                System.out.print("[");
                for(Book book: reader.getBooks()){
                    System.out.print(book.toString() + ", ");
                }
                System.out.print("]\n");
            }
            return count;
        }else{
            count = 0;
            for(Reader reader:readers){
                count++;
                System.out.println(reader.toString());
            }
        }
        return count;
    }

    public Reader getReaderByCard(int cardNumber){
        for(Reader reader:readers){
            if(reader.getCardNumber() == cardNumber){
                System.out.println("Returning Reader " + reader);
                return reader;
            }else{
                System.out.println("Could not find a reader with the card #" + cardNumber);
            }
        }
        return null;
    }

    public Code addReader(Reader reader){
        for(Reader reader1: readers){
            if(reader1.equals(reader)){
                System.out.println(reader.getName() + " already has an account!");
                return Code.READER_ALREADY_EXISTS_ERROR;
            }else if(reader1.getCardNumber() == reader.getCardNumber()){
                System.out.println(reader1.getName() + " and " + reader.getName() + " have the same card number!");
                return Code.READER_CARD_NUMBER_ERROR;
            }

        }
        readers.add(reader);
//        System.out.println(reader.getName() + " added to the library!");
        if(reader.getCardNumber() > this.getLibraryCardNumber()){
            libraryCard = reader.getCardNumber();
        }
        return Code.SUCCESS;
    }

    public Code removeReader(Reader reader){
        if(readers.contains(reader) && reader.getBookCount() > 0){
            System.out.println(reader.getName() + " must return all books!");
            return Code.READER_STILL_HAS_BOOKS_ERROR;
        }else if(!readers.contains(reader)) {
            System.out.println(reader + " is not a part of this Library");
            return Code.READER_NOT_IN_LIBRARY_ERROR;
        }else{
            readers.remove(reader);
        }
        return Code.SUCCESS;
    }

    public int convertInt(String recordCountString, Code code){
        int conInt = 0;
        try{
            conInt = Integer.parseInt(recordCountString);
        }catch(NumberFormatException e){
            if(code.equals(Code.BOOK_COUNT_ERROR)){
                System.out.println("Error: Could not read number of book");
            }else if(code.equals(Code.PAGE_COUNT_ERROR)){
                System.out.println("Error: could not parse page count");
            }else if(code.equals(Code.DATE_CONVERSION_ERROR)){
                System.out.println("Error: Could not parse date component");
            }else{
                System.out.println("Error: Unknown conversion error");
            }
        }
        return conInt;
    }


    public LocalDate convertDate(String date, Code errorCode){
        LocalDate newDate;
        int count = 0;
        boolean lessZero = false;
        for(String word:date.split("-")){
            count++;
            if(convertInt(word,Code.DATE_CONVERSION_ERROR) <= 0){ //checks if the word split is less than zero
                lessZero = true;
            }
        }
        if(date.equals("0000")){
            newDate = LocalDate.of(1970,1,1);
            return newDate;
        }else if(count != 3){
            System.out.println("ERROR: date conversion error, could not parse " + date);
            System.out.println("Using default date (01-jan-1970");
            return LocalDate.of(1970,1,1);
        }else if(lessZero == true){ //fixed
            System.out.println("Error converting date: Year ");
            System.out.println("Error converting date: Month ");
            System.out.println("Error converting date: Day ");
            System.out.println("Using default date (01-jan-1970)");
            return LocalDate.of(1970,1,1);
        }
        return newDate = LocalDate.parse(date);
    }

    public int getLibraryCardNumber(){
        return libraryCard+1;
    }

    private Code errorCode(int codeNumber) {
        for (Code code : Code.values()) {
            if (code.getCode() == codeNumber) {
                return code;
            }
        }
        return Code.UNKNOWN_ERROR;
    }

    ///////////////Getters and Setters///////////////

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int getLibraryCard() {
        return libraryCard;
    }

    public static void setLibraryCard(int libraryCard) {
        Library.libraryCard = libraryCard;
    }

    public List<Reader> getReaders() {
        return readers;
    }

    public void setReaders(List<Reader> readers) {
        this.readers = readers;
    }

    public HashMap<String, Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(HashMap<String, Shelf> shelves) {
        this.shelves = shelves;
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

}
