package frontend;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

final class IndexView {

    public void assertMainHeader(final String expectedText) {
        $("body > h1").shouldHave(
                Condition.exactText(expectedText)
        );
    }

}
