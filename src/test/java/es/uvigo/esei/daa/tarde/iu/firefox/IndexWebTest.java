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
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import es.uvigo.esei.daa.tarde.daos.BaseDAOTest;
import es.uvigo.esei.daa.tarde.daos.articles.BookDAO;
import es.uvigo.esei.daa.tarde.entities.articles.Book;

@RunWith(Parameterized.class)
public class IndexWebTest extends BaseDAOTest{
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

    public IndexWebTest(final String _, final Book [ ] bookList) {
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


        final String baseUrl = "http://localhost:8080/DAATarde/";

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
    //este test solo será pasado si hay 10 articulos en la BD
    public void index_can_show_ten_articles() throws Exception {
        verifyXpathCount("//div[@ng-repeat='article in articles']",10);
    }

    @Test
    //pensado para 10 articles per page
    public void index_can_search_empty_category_todo() throws Exception {

        driver.findElements(By.xpath("//input[@ng-model='terms']")).clear();
        final WebElement input = driver.findElement(By.xpath("//input[@ng-model='terms']"));
        input.sendKeys(Keys.RETURN);
        verifyXpathCount("//div[@ng-repeat='article in articles']",10);

    }

    @Test
    //test funciona para 10 books en BD
    public void index_can_search_empty_category_books() throws Exception {

        driver.findElements(By.xpath("//input[@ng-model='terms']")).clear();
        final WebElement but = driver.findElement(By.xpath("//button[@data-toggle='dropdown']"));
        but.click();
        driver.findElement(By.linkText("Libros")).click();;
                        
        final WebElement input = driver.findElement(By.xpath("//input[@ng-model='terms']"));
        input.sendKeys(Keys.RETURN);
        verifyXpathCount("//div[@ng-repeat='article in articles']",10);

    }
    
    @Test
    //test funciona para 10 books en BD
    public void index_can_search_empty_category_comics() throws Exception {

        driver.findElements(By.xpath("//input[@ng-model='terms']")).clear();
        final WebElement but = driver.findElement(By.xpath("//button[@data-toggle='dropdown']"));
        but.click();
        driver.findElement(By.linkText("Cómics")).click();;
        
        final WebElement input = driver.findElement(By.xpath("//input[@ng-model='terms']"));
        input.sendKeys(Keys.RETURN);
        verifyXpathCount("//div[@ng-repeat='article in articles']",0);
        
    }
    
    @Test
    //test funciona para 10 books en BD
    public void index_can_search_empty_category_movies() throws Exception {

        driver.findElements(By.xpath("//input[@ng-model='terms']")).clear();
        final WebElement but = driver.findElement(By.xpath("//button[@data-toggle='dropdown']"));
        but.click();
        driver.findElement(By.linkText("Películas")).click();;
        
        final WebElement input = driver.findElement(By.xpath("//input[@ng-model='terms']"));
        input.sendKeys(Keys.RETURN);
        verifyXpathCount("//div[@ng-repeat='article in articles']",10);
    }
    
    @Test
    //test funciona para 10 books en BD
    public void index_can_search_empty_category_music() throws Exception {

        driver.findElements(By.xpath("//input[@ng-model='terms']")).clear();
        final WebElement but = driver.findElement(By.xpath("//button[@data-toggle='dropdown']"));
        but.click();
        driver.findElement(By.linkText("Música")).click();;
        
        final WebElement input = driver.findElement(By.xpath("//input[@ng-model='terms']"));
        input.sendKeys(Keys.RETURN);
        verifyXpathCount("//div[@ng-repeat='article in articles']",10);

    }
    
    @Test
    public void index_can_navigate_to_add_page() throws Exception {
        driver.findElement(By.linkText("Añadir Artículo")).click();
        driver.findElement(By.name("Cancelar"));
        
    }

    private void verifyXpathCount(final String xpath, final int count) {
        try {
            assertEquals(count, driver.findElements(By.xpath(xpath)).size());
        } catch (final Error e) {
            verificationErrors.append(e.toString());
        }
    }
}
