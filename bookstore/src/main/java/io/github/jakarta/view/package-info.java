/**
 * <h2>The view components</h2>
 *
 * <p>This package provides all the underlying abstractions for handling web UI interactions
 * between server and client agents backed by an enhanced <strong>MPA</strong> (multi-page) architecture.</p> <br>
 *
 * <p>{@link io.github.jakarta.view.Form} view implementations receives FORM data submits using
 * {@code application/x-www-form-urlencoded} format (RFC 1738). <br>
 *
 * <p>{@link io.github.jakarta.view.DataTable} view implementations can iterate over a set of rows
 * fetched in a pageable way which is used in HTML tables.</p> <br>
 *
 * <p>And the JSTL tag files under {@code WEB-INF/tags} works like a template which is imported by other JSP files.</p> <br>
 *
 * <h3>Why MPA instead of SPA (single-page)?</h3>
 * <p>Yes, I understand that a nice UI/UX can easily be achieved just by using SPA frameworks on the client-side, this
 * was widely adopted years ago with the premisse of avoiding full page reloads and heavy pages,
 * which of course it's disliked by the end user.</p> <br>
 *
 * <p>However, we realized back then that SPA comes with SEO issues and also "break" the web in a certain way...
 * Because of that, developers started to migrate from plain React to hybrid frameworks like Next.js, which
 * is a web server like any other but runs on top of Node.js - and as you know, this change was smooth for
 * front-end developers, but it's not a good fit for fullstack developers working on enterprise software,
 * specially when there is a constrained environment not being driven by JavaScript at any sense.</p>
 *
 * <p>Moreover, performance aspects, security and been lightweight are another reasons to evaluate whether the
 * front-end should be completly decoupled from the back-end or not. We start with monoliths and then, if needed,
 * we decouple into a few niched microservices, and we can definitely do that without changing
 * our view rendering stack nor architecture.</p> <br
 *
 * <p>There are other developers talking about that, so here is a
 * <a href="https://stackoverflow.blog/2021/12/28/what-i-wish-i-had-known-about-single-page-applications/">
 *  good reading</a> from the Stackoverflow's Blog.</p> <br>
 *
 * <h3>Ok, and why choose JSP over template engines?</h3>
 * <p>Maybe you did not expect that but JSPs are incredibly fast though being old - it's fast because it has a
 * compiler built-in, this compiler gets our JSP files and compile to Java bytcode (.class), thus there is no need
 * to waste CPU cycles on runtime later as a normal template engine would do! </p> <br>
 *
 * <p>Of course, the enhanced MPA architecture can be made with template engines too, but we are in Jakarta EE so let's
 * stick with that, we try as much as possible to not compete with the platform but use it in our favor. </p><br>
 *
 * <h3>Sending "HTML over the wire"</h3>
 * <p>To achieve a good user experience with continuous feedback to our end user, we need to eliminate page reloads
 * by rendering the HTML fragments on demand, not re-rendering it every time, this is the key concept of enhancing
 * web pages while keeping the server-side architecture and SEO optimizations, we will basically use JavaScript as
 * a light, front end scripting language for enchacing our application - JS was originally intended for that.</p> <br>
 *
 * <p>Through some HTTP headers we will be able to know if our user really needs a full page reload or only needs to
 * partially render some HTML fragment and update the DOM content on demand. This is hypermedia driven, it's HTML over
 * the wire! </p>
 *
 * <h3>Going beyond HTML by adopting htmx.js</h3>
 * <p><a href="https://htmx.org">HTMX</a> is a small library (~14k min.gz’d), dependency-free, extendable,
 * IE11 compatible & has reduced code base sizes by 67% when compared with react</p> <br>
 *
 * <p>Motivations for a lib like that:</p>
 * <ul>
 * <li>Why should only anchors and forms be able to make HTTP requests?</li>
 * <li>Why should only click & submit events trigger them?</li>
 * <li>Why should only GET & POST methods be available?</li>
 * <li>Why should you only be able to replace the entire screen?</li>
 * </ul>
 *
 * <p>This JavaScript library gives access to AJAX, CSS Transitions, WebSockets and SSE directly in HTML5, using
 * using attributes, so we can build modern user interfaces with the simplicity and power of hypertext.</p> <br>
 *
 * <h3>And since we are in Jakarta EE, why not JSF?</h3>
 * <p>JSF is still good for some kind of applications, but the short answer to why not use is:
 * for this app we need full control of the generated HTML, which is kinda of a low-level thing, and Jakarta Faces
 * has a more dynamic and high level approach, it's even more complex when you consider that it has it's own lifecycle
 * and works on top of postbacks with a stateful server-side component tree. </p> <br>
 *
 * <strong>You may also want to read the EE specifications:</strong> <br>
 * <a href="https://jakarta.ee/specifications/mvc/2.1/jakarta-mvc-spec-2.1.pdf">
 *     Jakarta MVC Specification</a> <br>
 *
 * <a href="https://jakarta.ee/specifications/pages/3.1/jakarta-server-pages-spec-3.1.pdf">
 *     Jakarta Server Pages (JSP) Specification</a> <br>
 *
 * <a href="https://jakarta.ee/specifications/tags/3.0/jakarta-tags-spec-3.0.pdf">
 *     Jakarta Standard Tag Library (JSTL) Specification</a> <br>
 *
 * <a href="https://eclipse-ee4j.github.io/krazo/documentation/latest/index.html">
 *     Eclipse Krazo (MVC) Implementation</a> <br>
 *
 * @see io.github.jakarta.view.JakartaMvc
 */
package io.github.jakarta.view;

