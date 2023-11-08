import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Name: Diego Zavala
 * Date: 2/2/2023
 * Description: This is a unit test for the Book.java class
 */

class BookTest {

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

    private Book testBook = null;
    private Book testBookDiff = null;
    private Book testBookSame = null;

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
        testBookSame = new  Book(testISBN, testTitle, testSubject, testPageCount, testAuthor, testDueDate);
        testBookDiff = new  Book(testISBN2, testTitle2, testSubject2, testPageCount2, testAuthor2, testDueDate2);
    }

    @AfterEach
    void tearDown() {
        System.out.println("Runs once after each test ");
        testBook = null;
    }
    @Test
    void testConstructor(){
        System.out.println("Testing the constructor");
        Book testBook2 = null;
        assertNull(testBook2);
        testBook2 = testBook;
        assertNotNull(testBook2);
        assertEquals(testBook2, testBook);
    }

    @Test
    void testToString() {
        System.out.println("toString test");
        assertEquals(testBook.toString(),testBookSame.toString());
    }

    @Test
    void getIsbn() {
        System.out.println("getIsbn test");
        assertEquals("1337", testBook.getIsbn());
    }

    @Test
    void setIsbn() {
        System.out.println("setIsbn test");
        assertEquals("1337", testBook.getIsbn());
        testBook.setIsbn("1437");
        assertEquals("1437", testBook.getIsbn());
        assertNotEquals("1337", testBook.getIsbn());
    }

    @Test
    void getTitle() {
        System.out.println("getTitle test");
        assertEquals("Headfirst Java", testBook.getTitle());
    }

    @Test
    void setTitle() {
        System.out.println("setTitle test");
        assertEquals("Headfirst Java", testBook.getTitle());
        testBook.setTitle("Headfirst Python");
        assertEquals("Headfirst Python", testBook.getTitle());
        assertNotEquals("Headfirst Java", testBook.getTitle());
    }

    @Test
    void getSubject() {
        System.out.println("getSubject test");
        assertEquals("education", testBook.getSubject());
    }

    @Test
    void setSubject() {
        System.out.println("setSubject test");
        assertEquals("education", testBook.getSubject());
        testBook.setSubject("software");
        assertEquals("software", testBook.getSubject());
        assertNotEquals("education", testBook.getSubject());
    }

    @Test
    void getPageCount() {
        System.out.println("getPageCount test");
        assertEquals(1337, testBook.getPageCount());
    }

    @Test
    void setPageCount() {
        System.out.println("setPageCount test");
        assertEquals(1337, testBook.getPageCount());
        testBook.setPageCount(2000);
        assertEquals(2000, testBook.getPageCount());
        assertNotEquals(1337, testBook.getPageCount());
    }

    @Test
    void getAuthor() {
        System.out.println("getAuthor test");
        assertEquals("Grady Booch", testBook.getAuthor());
    }

    @Test
    void setAuthor() {
        System.out.println("setAuthor test");
        assertEquals("Grady Booch", testBook.getAuthor());
        testBook.setAuthor("George Orwell");
        assertEquals("George Orwell", testBook.getAuthor());
        assertNotEquals("Grady Booch", testBook.getAuthor());
    }

    @Test
    void getDueDate() {
        System.out.println("getDueDate test");
        assertEquals(LocalDate.now(), testBook.getDueDate());
    }

    @Test
    void setDueDate() {
        System.out.println("setDueDate test");
        assertEquals(LocalDate.now(), testBook.getDueDate());
        testBook.setDueDate(LocalDate.of(2023,2,13));
        assertEquals(LocalDate.of(2023,2,13),testBook.getDueDate());
        assertNotEquals(LocalDate.now(), testBook.getDueDate());
    }

    @Test
    void testEquals() {
        System.out.println("testEquals test");
        assertEquals(true, testBook.equals(testBookSame));
        assertNotEquals(true, testBook.equals(testBookDiff));
    }

    @Test
    void testHashCode() {
        System.out.println("testHashCode test");
        assertEquals(testBookSame.hashCode(),testBook.hashCode());
        assertNotEquals(testBookDiff.hashCode(), testBook.hashCode());
    }
}