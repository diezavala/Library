import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Name: Diego Zavala
 * Date: 2/11/2023
 * Description: This is a unit test for the Reader.java class
 */

class ReaderTest {

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

    private int testCardNumber = 12;
    private String testName = "Diego Zavala";
    private String testPhone = "123-32-1";
    private ArrayList<Book> testBooks = new ArrayList<Book>();

    private int testCardNumber2 = 123;
    private String testName2 = "Bruce Wayne";
    private String testPhone2 = "123-45-67";
    private ArrayList<Book> testBooks2 = new ArrayList<Book>();

    private Book testBook = null;
    private Book testBook2 = null;

    private Reader testReader = null;
    private Reader testReader2 = null;
    private Reader testReader3 = null;
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
        testReader = new Reader(testCardNumber, testName, testPhone);
        testReader2 = new Reader(testCardNumber, testName, testPhone);
        testReader3 = new Reader(testCardNumber2, testName2, testPhone2);
        testBook = new  Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        testBook2 = new  Book(testISBN2, testTitle2, testSubject2, testPageCount2, testAuthor2, testDueDate2);

    }

    @AfterEach
    void tearDown() {
        System.out.println("Runs once after each test ");
        testReader = null;
        testBook = null;
    }

    @Test
    void addBook() {
        assertEquals(testReader.addBook(testBook), Code.SUCCESS);
        assertNotEquals(testReader.addBook(testBook), Code.SUCCESS);
        assertEquals(testReader.addBook((testBook)), Code.BOOK_ALREADY_CHECKED_OUT_ERROR);
    }

    @Test
    void removeBook() {
        assertEquals(testReader.removeBook(testBook),Code.READER_DOESNT_HAVE_BOOK_ERROR);
        assertEquals(testReader.addBook(testBook),Code.SUCCESS);
    }

    @Test
    void hasBook() {
        Book newBook = new Book(testISBN2,testTitle2, testSubject2,testPageCount2,testAuthor2,testDueDate2);
        assertFalse(testReader.hasBook(newBook));
        testReader.addBook(newBook);
        assertTrue(testReader.hasBook(newBook));
    }

    @Test
    void getBookCount() {
        Reader testReader2 = new Reader(testCardNumber2, testName2, testPhone2);
        assertEquals(testReader2.getBookCount(), 0);
        testReader2.addBook(testBook2);
        assertEquals(testReader2.getBookCount(), 1);
        testReader2.removeBook(testBook2);
        assertEquals(testReader2.getBookCount(), 0);

    }

    @Test
    void getBooks() {
        System.out.println("Testing getBooks");
        assertEquals(testBooks2, testReader.getBooks());
    }

    @Test
    void setBooks() {
        System.out.println("Testing setBooks");
        assertEquals(testBooks2, testReader.getBooks());
        testReader.setBooks(testBooks);
    }

    @Test
    void getCardNumber() {
        System.out.println("Testing getCardNumber");
        assertEquals(testCardNumber, testReader.getCardNumber());
    }

    @Test
    void setCardNumber() {
        System.out.println("Testing setCardNumber");
        assertEquals(testCardNumber,testReader.getCardNumber());
        testReader.setCardNumber(15);
        assertNotEquals(testCardNumber,testReader.getCardNumber());
        assertEquals(15, testReader.getCardNumber());
    }

    @Test
    void getName() {
        System.out.println("Testing getName");
        assertEquals(testName, testReader.getName());
    }

    @Test
    void setName() {
        System.out.println("Testing setName");
        assertEquals(testName, testReader.getName());
        testReader.setName("Aang");
        assertNotEquals(testName, testReader.getName());
        assertEquals("Aang", testReader.getName());
    }

    @Test
    void getPhone() {
        System.out.println("Testing getPhone");
        assertEquals(testPhone, testReader.getPhone());
    }

    @Test
    void setPhone() {
        System.out.println("Testing setPhone");
        assertEquals(testPhone, testReader.getPhone());
        testReader.setPhone("559-542");
        assertNotEquals(testPhone, testReader.getPhone());
        assertEquals("559-542", testReader.getPhone());
    }

    @Test
    void testEquals() {
        System.out.println("testEquals test");
        assertEquals(true, testReader.equals(testReader2));
        assertNotEquals(true, testBook.equals(testReader3));
    }

    @Test
    void testHashCode() {
        System.out.println("testHashCode test");
        assertEquals(testReader.hashCode(),testReader2.hashCode());
        assertNotEquals(testReader.hashCode(), testReader3.hashCode());
    }

    @Test
    void testToString() {
        System.out.println("toString test");
        assertEquals(testReader.toString(),testReader2.toString());
        testReader.addBook(testBook);
        testReader.addBook(testBook2);
        System.out.println(testReader.toString());
    }
}