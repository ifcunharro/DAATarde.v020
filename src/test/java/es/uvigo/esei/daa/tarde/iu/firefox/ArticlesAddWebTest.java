package es.uvigo.esei.daa.tarde.iu.firefox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import es.uvigo.esei.daa.tarde.daos.BaseDAOTest;
import es.uvigo.esei.daa.tarde.daos.articles.BookDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Book;

@RunWith(Parameterized.class)
public class ArticlesAddWebTest extends BaseDAOTest{
    private static final int DEFAULT_WAIT_TIME = 7;
    private WebDriver driver;
    private final StringBuffer verificationErrors = new StringBuffer();



    @Parameters(name = "{index}: {0}")
    public static Collection<Object[ ]> createBookData( ) {
        return Arrays.asList(new Object[ ][ ] {
            { "asoiaf", new Book[ ] {
                new Book("A Game of Thrones", new LocalDate(1996,  8,  6)),
                new Book("A Clash of Kings",  new LocalDate(1998, 11, 16)),
                new Book("A Storm of Words",  new LocalDate(2000,  8,  8)),
                new Book("A Fest for Crows",  new LocalDate(2005, 10, 17))
            }}
        });
    }
    

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private BookDAO dao;
    private final List<Book> bookList;

    public ArticlesAddWebTest(final String _, final Book [ ] bookList) {
        this.bookList = Arrays.asList(bookList);
    }


    @Before
    public void createBookDAO( ) {
        dao = new BookDAO();
    }

    @Before
    public void insertBooks( ) {
        entityManager.getTransaction().begin();
        for (final Book b : bookList) {
            b.setVerified(true);
            if (b.getId() == null) entityManager.persist(b);
            else                   entityManager.merge(b);
        }
        entityManager.getTransaction().commit();
    }

    @Before
    public void setUp() throws Exception {


        final String baseUrl = "http://localhost:8080/DAATarde/#/articles/add";

        driver = new FirefoxDriver();
        driver.get(baseUrl);
        driver.manage().addCookie(new Cookie("token", "bXJqYXRvOm1yamF0bw=="));

        // Driver will wait DEFAULT_WAIT_TIME if it doesn't find and element.
        driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);

        driver.get(baseUrl);
        driver.findElement(By.id("footer"));
    }

    @After
    public void tearDown() throws Exception {

        driver.quit();
        final String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

   @Test
   public void add_page_can_show_books_form(){
       Select select = new Select(driver.findElement(By.xpath("//select[@ng-model='category']")));
       
       select.selectByVisibleText("Libros");
       driver.findElement(By.xpath("//input[@name='bookFieldEditorial']"));
   }
   
   @Test
   public void add_page_can_show_comics_form(){
       Select select = new Select(driver.findElement(By.xpath("//select[@ng-model='category']")));
       
       select.selectByVisibleText("Cómics");
       driver.findElement(By.xpath("//input[@name='comicFieldSerie']"));
   }
   
   @Test
   public void add_page_can_show_movies_form(){
       Select select = new Select(driver.findElement(By.xpath("//select[@ng-model='category']")));
       
       select.selectByVisibleText("Películas");
       driver.findElement(By.xpath("//input[@name='movieFieldGenre']"));
   }
   
   @Test
   public void add_page_can_show_music_form(){
       Select select = new Select(driver.findElement(By.xpath("//select[@ng-model='category']")));
       
       select.selectByVisibleText("Música");
       driver.findElement(By.xpath("//input[@name='musicFieldTracks']"));
   }

   @Test
   public void add_page_can_navigate_to_index_page(){
       driver.findElement(By.xpath("//a[@title='Universidade de Vigo']")).click();
       verifyXpathCount("//div[@ng-repeat='article in articles']",10);
   }
   
   @Test
   public void add_page_can_insert_books(){
       Select select = new Select(driver.findElement(By.xpath("//select[@ng-model='category']")));
       
       select.selectByVisibleText("Libros");
       driver.findElement(By.name("name")).sendKeys("Homero");
       driver.findElement(By.xpath("//textarea[@ng-model='article.description']")).sendKeys("o");
       driver.findElement(By.name("bookFieldAuthor")).sendKeys("Homero");
       driver.findElement(By.name("bookFieldEditorial")).sendKeys("Homero");
       driver.findElement(By.name("date")).sendKeys("2015/02/02");
       driver.findElement(By.name("botonSubmitAlta")).click();
       driver.switchTo().alert().accept();
       
   }
   
   private void verifyXpathCount(final String xpath, final int count) {
       try {
           assertEquals(count, driver.findElements(By.xpath(xpath)).size());
       } catch (final Error e) {
           verificationErrors.append(e.toString());
       }
   }

}
