package frontend;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

final class SmokeUITest {

    private final BookstoreUI bookstore = new BookstoreUI();

    @Test
    void testIndexView() {
        final var view = this.bookstore.indexView();
        view.assertMainHeader("Hello, world!");
    }

    @AfterAll
    static void cleanUp() {
        BookstoreUI.close();
    }

}