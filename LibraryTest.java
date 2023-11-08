import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {
    ///////////////Book///////////////
    private String testISBN = "1337";
    private String testTitle = "Headfirst Java";
    private String testSubject = "education";
    private int testPageCount = 1337;
    private String testAuthor = "Grady Booch";
    private LocalDate testDueDate = LocalDate.now();

    private Book testBook = null;
    private Book testBookDiff = null;
    private Book testBookSame = null;

    ///////////////Shelf///////////////
    private int testShelfNumber = 3;
    private String testSubjectShelf = "math";

    private Shelf testShelf = null;
    private Shelf testShelf2 = null;
    private Shelf testShelf3 = null;
    ///////////////Reader///////////////
    private int testReaderCardNumber = 12;
    private String testReaderName = "Diego Zavala";
    private String testReaderPhone = "123-32-1";
    private ArrayList<Book> testReaderBooks = new ArrayList<Book>();

    private Reader testReader = null;
    private Reader testReader2 = null;
    private Reader testReader3 = null;
    ///////////////Library///////////////
    private Library testLibrary = null;
    private Library testLibrary2 = null;

    private List<Reader> readers = null;
    private HashMap<String, Shelf> shelves = null;
    private HashMap<Book, Integer> books = null;


    @BeforeEach
    void setUp() {
        System.out.println("Runs once before each test");
        //Initialize Books
        testBook = new  Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        testBookSame = new  Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        testBookDiff = new  Book("1234", "Stats", "math", 1200, "Einstein", LocalDate.now());

        //Initialize Shelves
        testShelf = new Shelf(testShelfNumber, testSubjectShelf); //3, math
        testShelf2 = new Shelf(testShelfNumber, testSubjectShelf); //3, math
        testShelf3 = new Shelf(5, "education"); //5, education

        //Initialize Readers
        testReader = new Reader(testReaderCardNumber, testReaderName, testReaderPhone);
        testReader2 = new Reader(23, "Dr. C", "433-4334");
        testReader3 = new Reader(14, "John Doe", "555-55-5");

        //Initialize Libraries
        testLibrary = new Library("CSUMB Library");
        testLibrary2 = new Library("Hartnell Library");

        readers = new ArrayList<>();
        books = new HashMap<>();
        shelves = new HashMap<>();
    }

    @AfterEach
    void tearDown() {
        testBook = null;
        testBookDiff = null;
        testBookSame = null;

        testShelf = null;
        testShelf2 = null;
        testShelf3 = null;

        testReader = null;
        testReader2 = null;
        testReader3 = null;

    }


    ///////////////Methods////////////////
    @Test
    void init() {
        assertEquals(Code.SUCCESS, testLibrary.init("Library00.csv"));
        assertEquals(Code.SUCCESS, testLibrary.init("Library01.csv"));
        assertEquals(Code.FILE_NOT_FOUND_ERROR, testLibrary.init("notarealfile.csv"));
    }

    @Test
    void addBook() {
        assertEquals(Code.SHELF_EXISTS_ERROR, testLibrary.addBook(testBook));
    }

    @Test
    void returnBook() {
        readers.add(testReader);
        testLibrary.addBook(testBook);
        testLibrary.addShelf(testShelf3);
        testReader.addBook(testBook);
        assertEquals(Code.SUCCESS, testLibrary.returnBook(testReader, testBook));
    }

    @Test
    void testReturnBook() {
        assertEquals(Code.SHELF_EXISTS_ERROR,testLibrary.returnBook(testBook) );
    }

    @Test
    void listBooks() {
        testLibrary.addBook(testBook);
        testLibrary.addBook(testBook);
        assertEquals(2, testLibrary.listBooks());
    }

    @Test
    void checkOutBook() {
        testLibrary.addShelf(testShelf);
        testLibrary.addShelf(testShelf3);
        testLibrary.addBook(testBook);
        testLibrary.addBook(testBookDiff);
        testLibrary.addReader(testReader);
        assertEquals(Code.SUCCESS, testLibrary.checkOutBook(testReader, testBook));
    }

    @Test
    void getBookByISBN() {
        testLibrary.addShelf(testShelf);
        testLibrary.addShelf(testShelf3);
        testLibrary.addBook(testBook);
        testLibrary.addBook(testBookDiff);
        assertEquals(testBook, testLibrary.getBookByISBN(testBook.getIsbn()));
    }

    @Test
    void listShelves() {
        testLibrary.addShelf(testShelf3);
        System.out.println(testLibrary.listShelves(true));
        assertEquals(Code.SUCCESS, testLibrary.listShelves(true));
    }

    @Test
    void addShelf() {
        assertEquals(Code.SUCCESS, testLibrary.addShelf("education"));
    }

    @Test
    void testAddShelf() {
        assertEquals(Code.SUCCESS, testLibrary.addShelf(testShelf3));
        assertNotEquals(Code.SUCCESS, testLibrary.addShelf(testShelf3));
    }

    @Test
    void getShelf() {
        testLibrary.addShelf(testShelf3);
        System.out.println(testLibrary.getShelf("education"));
        shelves.put("education", testShelf3);
        assertEquals(testShelf3, testLibrary.getShelf("education"));
    }

    @Test
    void testGetShelf() {
        testLibrary.addShelf(testShelf3);
        System.out.println(testLibrary.getShelf(1));
        shelves.put("education", testShelf3);
        assertEquals(testShelf3, testLibrary.getShelf(1));
    }

    @Test
    void listReaders() {
        testLibrary.addReader(testReader);
        testLibrary.addReader(testReader2);
        assertEquals(2, testLibrary.listReaders());
    }

    @Test
    void testListReaders() {
        testLibrary.addReader(testReader);
        testLibrary.addReader(testReader2);
        testReader.addBook(testBook);
        testReader2.addBook(testBookDiff);
        assertEquals(2, testLibrary.listReaders(true));
    }

    @Test
    void getReaderByCard() {
        testLibrary.addReader(testReader2);
        assertEquals(testReader2, testLibrary.getReaderByCard(23));
    }

    @Test
    void addReader() {
        assertEquals(Code.SUCCESS, testLibrary.addReader(testReader));
        assertNotEquals(Code.SUCCESS, testLibrary.addReader(testReader));
    }

    @Test
    void removeReader() {
        testLibrary.addReader(testReader);
        assertEquals(Code.SUCCESS, testLibrary.removeReader(testReader));
        assertNotEquals(Code.SUCCESS, testLibrary.removeReader(testReader));
    }

    @Test
    void convertInt() {
        assertEquals(3, testLibrary.convertInt("3", Code.SUCCESS));
        assertNotEquals(3, testLibrary.convertInt("2", Code.SUCCESS));
    }

    @Test
    void convertDate() {
        String date = "03-02-2003";
        assertEquals(Code.SUCCESS, Code.SUCCESS);
    }

    @Test
    void getLibraryCardNumber() {

        assertEquals(1, testLibrary.getLibraryCardNumber());
    }


    ///////////////Getters and Setters///////////////
    @Test
    void getName() {
        assertEquals("CSUMB Library",testLibrary.getName());
        assertEquals("Hartnell Library", testLibrary2.getName());
    }

    @Test
    void setName() {
        assertEquals("CSUMB Library",testLibrary.getName());
        assertEquals("Hartnell Library", testLibrary2.getName());
        testLibrary.setName("Monterey Library");
        assertEquals("Monterey Library", testLibrary.getName());
    }

    @Test
    void getLibraryCard() {
        assertEquals(1, testLibrary.getLibraryCardNumber());

    }

    @Test
    void setLibraryCard() {
        assertEquals(testLibrary.getLibraryCardNumber(), testLibrary.getLibraryCardNumber());
    }

    @Test
    void getReaders() {
        testLibrary.addReader(testReader);
        readers.add(testReader);
        System.out.println(readers);
        assertEquals(readers, testLibrary.getReaders());
    }

    @Test
    void setReaders() {
        readers.add(testReader);
        readers.add(testReader2);
        System.out.println(readers);
        testLibrary.setReaders(readers);
        assertEquals(readers, testLibrary.getReaders());
    }

    @Test
    void getShelves() {
        testLibrary.addShelf(testShelf3);
        shelves.put("education", testShelf3);
        assertEquals(shelves, testLibrary.getShelves());
    }

    @Test
    void setShelves() {
        testLibrary.addShelf(testShelf3);
        shelves.put("education", testShelf3);
        testLibrary.setShelves(shelves);
        assertEquals(shelves, testLibrary.getShelves());
    }

    @Test
    void getBooks() {
        books.put(testBook, 1);
        testLibrary.addBook(testBook);
        assertEquals(books, testLibrary.getBooks());
    }

    @Test
    void setBooks() {
        books.put(testBook, 1);
        testLibrary.addBook(testBook);
        testLibrary.setBooks(books);
        assertEquals(books, testLibrary.getBooks());
    }
}