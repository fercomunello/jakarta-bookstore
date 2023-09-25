import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages(value = {"frontend"})
@SuiteDisplayName("Jakarta Bookstore - JUnit Test Suite")
final class BookstoreTestSuite {

}
