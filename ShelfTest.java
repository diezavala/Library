import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    //////Variables for the Shelf/////
    private int testShelfNumber = 3;
    private String testSubjectShelf = "math";
    private HashMap<Book, Integer> testBooks = new HashMap<Book, Integer>();
    private HashMap<Book, Integer> testBooks2 = new HashMap<Book, Integer>();
    /////Variables for the Book/////
    private String testISBN = "1337";
    private String testTitle = "Headfirst Java";
    private String testSubject = "education";
    private int testPageCount = 1337;
    private String testAuthor = "Grady Booch";
    private LocalDate testDueDate = LocalDate.now();

    private String testISBN2 = "1250";
    private String testTitle2 = "Intro to C++";
    private String testSubject2 = "programming";
    private int testPageCount2 = 900;
    private String testAuthor2 = "Robert Man";
    private LocalDate testDueDate2 = LocalDate.now();
    /////Creating 2 book objects/////
    private Book testBook = null;
    private Book testBook2 = null;
    /////Creating 3 shelf objects/////
    private Shelf testShelf = null;
    private Shelf testShelf2 = null;
    private Shelf testShelf3 = null;

    @BeforeAll
    static void mainSetup(){
        System.out.println("Main setup, runs once before all");
    }

    @AfterAll
    static void mainTearDown(){
        System.out.println("Main tear down, runs once at the end");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Runs once before each test");
        testBook = new  Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        testBook2 = new  Book(testISBN2, testTitle2, testSubject2, testPageCount2, testAuthor2, testDueDate2);


        testShelf = new Shelf(testShelfNumber, testSubjectShelf); //3, math
        testShelf2 = new Shelf(testShelfNumber, testSubjectShelf); //3, math
        testShelf3 = new Shelf(5, "education");

        testBooks.put(testBook, 1); //headfirst java count 0
        testBooks2.put(testBook2, 2);//intro to c++ count 2


    }

    @AfterEach
    void tearDown() {
        System.out.println("Runs once after each test ");
        testBook = null;
        testBook2 = null;
        testShelf = null;
        testShelf2 = null;
        testShelf3 = null;
        testBooks = null;
        testBooks2 = null;
    }

    @Test
    void getShelfNumber() {
        System.out.println("Testing getShelfNumber");
        assertEquals(testShelfNumber, testShelf.getShelfNumber());
    }

    @Test
    void setShelfNumber() {
        System.out.println("Testing setShelfNumber");
        assertEquals(testShelfNumber, testShelf.getShelfNumber());
        testShelf.setShelfNumber(7);
        assertNotEquals(testShelfNumber, testShelf.getShelfNumber());
        assertEquals(7, testShelf.getShelfNumber());
    }

    @Test
    void getSubject() {
        System.out.println("Testing getSubject");
        assertEquals(testSubjectShelf, testShelf2.getSubject());
    }

    @Test
    void setSubject() {
        System.out.println("Testing setSubject");
        assertEquals(testSubjectShelf, testShelf2.getSubject());
        testShelf2.setSubject("history");
        assertNotEquals(testSubjectShelf, testShelf.getShelfNumber());
        assertEquals("history", testShelf2.getSubject());
    }

    @Test
    void getBooks() {
        System.out.println("Testing getBooks");
        testShelf3.addBook(testBook); //shelf3 is number 5 and education
        System.out.println("getBooks():  " + testShelf3.getBooks());
        assertEquals(testBooks, testShelf3.getBooks());
    }

    @Test
    void setBooks() {
        System.out.println("Testing setBooks");
        testShelf3.addBook(testBook);
//        System.out.println("setBooks():  " + testShelf3.getBooks());
        assertEquals(testBooks, testShelf3.getBooks());
        testShelf3.setBooks(testBooks2);
//        System.out.println("new setBooks():  " + testShelf3.getBooks());
        assertEquals(testBooks2, testShelf3.getBooks());
        assertNotEquals(testBooks, testShelf3.getBooks());
    }

    @Test
    void testEquals() {
        System.out.println("testEquals test");
//        System.out.println("Comparing testShelf " + testShelf + " with testShelf2 " + testShelf2);
        assertEquals(true, testShelf.equals(testShelf2));
//        System.out.println("Comparing testShelf " + testShelf + " with testShelf3 " + testShelf3);
        assertNotEquals(true, testShelf.equals(testShelf3));
    }

    @Test
    void testHashCode() {
        System.out.println("testHashCode test");
        assertEquals(testShelf.hashCode(),testShelf2.hashCode());
        assertNotEquals(testShelf.hashCode(), testShelf3.hashCode());
    }


    @Test
    void getBookCount() {
        Book book = new Book("123","Stats", "math", 400, "John", LocalDate.now());
        Random  random = new Random();
        int randNum = random.nextInt(50);
        System.out.println("Count: " + randNum);
        for(int i = 0; i < randNum; i++){
            testShelf.addBook(book);
        }
        assertEquals(randNum, testShelf.getBookCount(book));
        testShelf.removeBook(book);
        assertNotEquals(randNum, testShelf.getBookCount(book));
        for(int i = 0; i < randNum-1; i++){
            testShelf.removeBook(book);
        }
        assertNotEquals(randNum, testShelf.getBookCount(book));
        assertEquals(-1, testShelf.getBookCount(testBook2));
//        System.out.println("Shelf " + testShelf + " checking: " + testBook2 + " is on the shelf " + testShelf.getBookCount(testBook2));
    }
//
    @Test
    void addBook() {
        Book book = new Book("123","Stats", "math", 400, "John", LocalDate.now());
        assertEquals(testShelf.addBook(book), Code.SUCCESS);
        assertEquals(1, testShelf.getBookCount(book));
        assertEquals(testShelf.addBook(book), Code.SUCCESS);
        assertEquals(2, testShelf.getBookCount(book));
        Book book2 = new Book(testISBN, testTitle, testSubject, testPageCount,testAuthor, LocalDate.now());
        assertEquals(testShelf.addBook(book2), Code.SHELF_SUBJECT_MISMATCH_ERROR);
    }

    @Test
    void removeBook() {
        Book book2 = new Book(testISBN, testTitle, testSubject, testPageCount,testAuthor, LocalDate.now());
        assertEquals(testShelf.removeBook(book2), Code.BOOK_NOT_IN_INVENTORY_ERROR);
        Book book = new Book("123","Stats", "math", 400, "John", LocalDate.now());
        assertEquals(testShelf.addBook(book), Code.SUCCESS);
        assertEquals(1, testShelf.getBookCount(book));
        assertEquals(testShelf.removeBook(book), Code.SUCCESS);
        assertEquals(0, testShelf.getBookCount(book));
        for(int i = 0; i < 3; i++){
            testShelf.addBook(book);
        }
        for(int i = 0; i < 3; i++){
            System.out.println("There are " + testShelf.getBookCount(book) + " copies of " + book);
            testShelf.removeBook(book);
        }
        System.out.println("There are " + testShelf.getBookCount(book) + " copies of " + book);

    }

    @Test
    void listBooks() {
        Book book = new Book("123","Stats", "math", 400, "John", LocalDate.now());
        Book book2 = new Book("678","Algebra", "math", 400, "John", LocalDate.now());

        testShelf.addBook(book);
        testShelf.addBook(book);
        testShelf.addBook(book);
        testShelf.addBook(book2);
        testShelf.addBook(book2);
        assertEquals("2 books on shelf: 3: math\n" +
                "Stats by John ISBN: 123 3\n" +
                "Algebra by John ISBN: 678 2", testShelf.listBooks());
    }
}