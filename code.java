package com.ing.P09498.cucumber.plugins.logger.evidence;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.Collection;
import java.util.Objects;

public final class HtmlElement {

    private HtmlElement() {
    }

    public static Element element(final String tag) {
        return new Element(tag);
    }

    public static Element html() {
        return element("html");
    }

    public static Element head() {
        return element("head");
    }

    public static Element body() {
        return element("body");
    }

    public static Element meta(final String contentType) {
        return element("meta")
                .attr("content", contentType)
                .attr("http-equiv", "Content-Type");
    }

    public static Element title(final String title) {
        return element("title").text(nullSafe(title));
    }

    public static Element div(final String classes) {
        return withClasses(element("div"), classes);
    }

    public static Element span(final String classes) {
        return withClasses(element("span"), classes);
    }

    public static Element span(final String classes, final String text) {
        return span(classes).text(nullSafe(text));
    }

    public static Element small(final String classes) {
        return withClasses(element("small"), classes);
    }

    public static Element small(final String classes, final String text) {
        return small(classes).text(nullSafe(text));
    }

    public static Element strong(final String classes, final String text) {
        return withClasses(element("strong"), classes).text(nullSafe(text));
    }

    public static Element pre(final String classes) {
        return withClasses(element("pre"), classes);
    }

    public static Element pre(final String classes, final String text) {
        return pre(classes).text(nullSafe(text));
    }

    public static Element details(final String classes) {
        return withClasses(element("details"), classes);
    }

    public static Element summary(final String classes) {
        return withClasses(element("summary"), classes);
    }

    public static Element table(final String classes) {
        return withClasses(element("table"), classes);
    }

    public static Element tbody() {
        return element("tbody");
    }

    public static Element tr() {
        return element("tr");
    }

    public static Element td() {
        return element("td");
    }

    public static Element append(final Element parent, final Element... children) {
        if (Objects.isNull(parent)) {
            return null;
        }

        if (Objects.nonNull(children)) {
            for (final Element child : children) {
                if (Objects.nonNull(child)) {
                    parent.appendChild(child);
                }
            }
        }

        return parent;
    }

    public static Element appendAll(final Element parent, final Collection<Element> children) {
        if (Objects.isNull(parent)) {
            return null;
        }

        if (Objects.nonNull(children)) {
            children.stream()
                    .filter(Objects::nonNull)
                    .forEach(parent::appendChild);
        }

        return parent;
    }

    public static Element withClasses(final Element element, final String classes) {
        if (Objects.nonNull(element) && Objects.nonNull(classes) && !classes.isBlank()) {
            for (final String cssClass : classes.trim().split("\\s+")) {
                element.addClass(cssClass);
            }
        }

        return element;
    }

    public static Element style(final Element element, final String style) {
        if (Objects.nonNull(element) && Objects.nonNull(style) && !style.isBlank()) {
            element.attr("style", style);
        }

        return element;
    }

    public static Element fragment(final String html) {
        return Jsoup.parseBodyFragment(html).body().firstElementChild();
    }

    public static Element empty() {
        return small("", "");
    }

    public static String nullSafe(final String text) {
        return Objects.isNull(text) ? "" : text;
    }
}


package com.ing.P09498.cucumber.plugins.logger.evidence;

import org.jsoup.nodes.Element;

public final class ReportStyle {

    private ReportStyle() {
    }

    public static Element styleElement() {
        return HtmlElement.element("style")
                .attr("type", "text/css")
                .html(CSS);
    }

    private static final String CSS = """
            * {
                box-sizing: border-box;
            }

            html {
                font-family: sans-serif;
                line-height: 1.15;
                -webkit-text-size-adjust: 100%;
                -webkit-tap-highlight-color: transparent;
            }

            body {
                margin: 0;
                font-size: 1rem;
                font-weight: 400;
                line-height: 1.5;
                color: #212529;
                background-color: #0000006e;
                text-align: left;
            }

            svg {
                vertical-align: middle;
                overflow: hidden;
            }

            pre {
                margin-top: 0;
                margin-bottom: 1rem;
                color: #212529;
                font-family: SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace;
                font-size: 87.5%;
                display: block;
            }

            small {
                font-size: 80%;
                font-weight: 400;
            }

            summary {
                cursor: pointer !important;
            }

            table {
                caption-side: bottom;
                border-collapse: collapse;
            }

            tbody,
            td,
            tfoot,
            th,
            thead,
            tr {
                border: 0 solid;
                border-color: inherit;
            }

            .container {
                width: 100%;
                padding-right: 15px;
                padding-left: 15px;
                margin-right: auto;
                margin-left: auto;
            }

            @media (min-width: 576px) {
                .container {
                    max-width: 540px;
                }
            }

            @media (min-width: 768px) {
                .container {
                    max-width: 720px;
                }
            }

            @media (min-width: 992px) {
                .container {
                    max-width: 960px;
                }
            }

            @media (min-width: 1200px) {
                .container {
                    max-width: 1140px;
                }
            }

            .row {
                display: flex;
                flex-wrap: wrap;
                margin-right: -15px;
                margin-left: -15px;
            }

            .card {
                position: relative;
                display: flex;
                flex-direction: column;
                min-width: 0;
                word-wrap: break-word;
                background-color: #fff;
                background-clip: border-box;
                border: 1px solid rgba(0, 0, 0, .125);
                border-radius: .25rem;
            }

            .card-header {
                padding: .75rem 1.25rem;
                margin-bottom: 0;
                background-color: rgba(0, 0, 0, .03);
                border-bottom: 1px solid rgba(0, 0, 0, .125);
            }

            .card-body {
                flex: 1 1 auto;
                padding: 1.25rem;
            }

            .card-footer {
                padding: .5rem 1rem;
                background-color: rgba(0, 0, 0, .03);
                border-top: 1px solid rgba(0, 0, 0, .125);
            }

            .badge {
                display: inline-block;
                padding: .25em .4em;
                font-size: 75%;
                font-weight: 700;
                line-height: 1;
                text-align: center;
                white-space: nowrap;
                vertical-align: baseline;
                border-radius: .25rem;
            }

            .table {
                width: 100%;
                margin-bottom: 1rem;
                color: #212529;
                vertical-align: top;
                border-color: #dee2e6;
                background-color: transparent;
            }

            .table > :not(caption) > * > * {
                padding: .5rem;
                border-bottom-width: 1px;
            }

            .table-sm > :not(caption) > * > * {
                padding: .25rem;
            }

            .table-bordered > :not(caption) > * {
                border-width: 1px 0;
            }

            .table-bordered > :not(caption) > * > * {
                border-width: 0 1px;
            }

            .w-100 {
                width: 100% !important;
            }

            .h-auto {
                height: auto !important;
            }

            .h-100 {
                height: 100% !important;
            }

            .m-0 {
                margin: 0 !important;
            }

            .m-1 {
                margin: .25rem !important;
            }

            .m-2 {
                margin: .5rem !important;
            }

            .mt-2 {
                margin-top: .5rem !important;
            }

            .mt-5 {
                margin-top: 3rem !important;
            }

            .mb-1 {
                margin-bottom: .25rem !important;
            }

            .mb-2 {
                margin-bottom: .5rem !important;
            }

            .mb-3 {
                margin-bottom: 1rem !important;
            }

            .ml-1 {
                margin-left: .25rem !important;
            }

            .mr-1 {
                margin-right: .25rem !important;
            }

            .mr-auto {
                margin-right: auto !important;
            }

            .p-0 {
                padding: 0 !important;
            }

            .p-1 {
                padding: .25rem !important;
            }

            .p-2 {
                padding: .5rem !important;
            }

            .p-4 {
                padding: 1.5rem !important;
            }

            .p-5 {
                padding: 2rem !important;
            }

            .pt-1 {
                padding-top: .25rem !important;
            }

            .pb-1 {
                padding-bottom: .25rem !important;
            }

            .border {
                border: 1px solid #dee2e6 !important;
            }

            .border-0 {
                border: 0 !important;
            }

            .border-bottom {
                border-bottom: 1px solid #dee2e6 !important;
            }

            .border-bottom-gray {
                border-bottom: 1px solid #00000042 !important;
            }

            .border-bottom-transparent {
                border-bottom: transparent !important;
            }

            .border-top-transparent {
                border-top: transparent !important;
            }

            .border-gray {
                border-color: #00000042 !important;
            }

            .rounded {
                border-radius: .25rem !important;
            }

            .shadow {
                box-shadow: 0 .5rem 1rem rgba(0, 0, 0, .15) !important;
            }

            .text-center {
                text-align: center !important;
            }

            .text-secondary {
                color: #6c757d !important;
            }

            .text-info {
                color: #17a2b8 !important;
            }

            .text-danger {
                color: #dc3545 !important;
            }

            .text-warning {
                color: #ffc107 !important;
            }

            .text-orange {
                color: darkorange !important;
            }

            .text-success {
                color: #35dc4ad9 !important;
            }

            .bg-light {
                background-color: #f8f9fa !important;
            }

            .justify-content-center {
                justify-content: center !important;
            }

            .fw-normal {
                font-weight: 400 !important;
            }

            .fw-bold {
                font-weight: 700 !important;
            }

            .fs-4 {
                font-size: calc(1.275rem + .3vw) !important;
            }

            .log-line:hover {
                color: #9370db !important;
            }

            .step-keyword {
                color: orange;
                font-weight: bold;
            }

            .runtime-badge {
                float: right;
            }

            .scenario-status {
                float: right;
            }

            .logs-container {
                overflow: auto;
                white-space: nowrap;
                background-color: #000000db;
                max-height: 500px;
            }

            .errors-container {
                overflow: auto;
                background-color: #000000db;
                max-height: 500px;
                font-size: small;
            }

            .log-breakdown-container {
                color: lightgray;
            }

            .log-breakdown-row {
                margin-left: initial;
                border-bottom: 1px solid #4c4c4c !important;
            }

            .log-breakdown-left {
                float: left;
                color: mediumpurple;
            }

            .log-breakdown-right {
                float: right;
                color: darkgoldenrod;
            }

            .content-margin {
                min-height: 1000px;
            }

            .table-container {
                overflow: auto;
            }
            """;
}

package com.ing.P09498.cucumber.plugins.logger.evidence;

import com.ing.P09498.cucumber.plugins.logger.TestLog;
import com.ing.P09498.cucumber.plugins.logger.context.StepContext;
import com.ing.P09498.cucumber.plugins.logger.context.TestContext;
import com.ing.P09498.cucumber.plugins.logger.utils.TestTimeUtils;
import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.Status;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public final class ReportElementHelper {

    private ReportElementHelper() {
    }

    public static Element htmlReport(final TestContext testContext) {
        return HtmlElement.html()
                .appendChild(head())
                .appendChild(body(testContext));
    }

    private static Element head() {
        return HtmlElement.head()
                .appendChild(HtmlElement.meta("text/html; charset=UTF-8"))
                .appendChild(HtmlElement.title("Test Evidence"))
                .appendChild(ReportStyle.styleElement());
    }

    private static Element body(final TestContext testContext) {
        return HtmlElement.body()
                .appendChild(contentContainer(testContext))
                .appendChild(HtmlElement.div("content-margin"));
    }

    public static Element contentContainer(final TestContext testContext) {
        final Element stepCardBody = HtmlElement.div("card-body p-5 pt-1 pb-1");

        testContext.getStepContextMap()
                .forEach((uuid, stepContext) -> stepCardBody.appendChild(step(stepContext)));

        stepCardBody.appendChild(separator());
        stepCardBody.appendChild(moreDetailsContainer(testContext));

        return HtmlElement.div("container mt-2")
                .appendChild(HtmlElement.div("row mb-3"))
                .appendChild(HtmlElement.div("card h-auto w-100 border border-gray shadow rounded")
                        .appendChild(contentCardHeader(testContext))
                        .appendChild(stepCardBody));
    }

    private static Element contentCardHeader(final TestContext testContext) {
        final Element testBadgeContainer = HtmlElement.div("text-secondary row pb-1 justify-content-center")
                .appendChild(badge("Started On: " + testContext.getStartTime()))
                .appendChild(badge("Total Runtime: " + testContext.getDuration()));

        return HtmlElement.div("container p-2")
                .appendChild(HtmlElement.div("card-header text-center border border-gray shadow rounded")
                        .appendChild(testStatusBadge(testContext.getStatus()))
                        .appendChild(HtmlElement.element("h5")
                                .addClass("text-secondary")
                                .addClass("fs-4")
                                .addClass("mb-2")
                                .addClass("p-1")
                                .addClass("border-bottom")
                                .text(HtmlElement.nullSafe(testContext.getName())))
                        .appendChild(testBadgeContainer));
    }

    public static Element step(final StepContext stepContext) {
        return row(
                HtmlElement.details("p-2 mb-2 w-100 border shadow rounded border-gray")
                        .appendChild(stepSummary(stepContext))
                        .appendChild(stepArgumentsCard(stepContext.getStepArgument()))
                        .appendChild(stepLogsCard(stepContext.getTestRunLogs()))
                        .appendChild(stepErrorsCard(stepContext.getException()))
        );
    }

    private static Element stepSummary(final StepContext stepContext) {
        return HtmlElement.summary("w-100 p-1 m-0")
                .appendChild(HtmlElement.span("step-keyword", HtmlElement.nullSafe(stepContext.getKeyword())))
                .appendChild(HtmlElement.span("text-secondary", HtmlElement.nullSafe(stepContext.getName())))
                .appendChild(stepStatusBadge(
                        stepContext.getStatus(),
                        hasTestRunLogsErrors(stepContext.getTestRunLogs()),
                        Objects.nonNull(stepContext.getException())
                ))
                .appendChild(HtmlElement.small(
                        "text-info badge fw-normal runtime-badge",
                        TestTimeUtils.durationToTimestamp(stepContext.getDuration())
                ));
    }

    private static boolean hasTestRunLogsErrors(final List<TestLog> testRunLogs) {
        return Objects.nonNull(testRunLogs) && testRunLogs.stream().anyMatch(TestLog::hasErrors);
    }

    private static Element moreDetailsContainer(final TestContext testContext) {
        final Element reportInfoSummary = HtmlElement.summary("w-100 p-1 m-0")
                .appendChild(HtmlElement.span("text-info", "More Info"));

        final Element moreInfoContent = HtmlElement.details("p-1 mb-2 w-100 border shadow rounded border-gray");

        if (Objects.nonNull(testContext.getAppVersion())) {
            moreInfoContent.appendChild(moreInfoContent("App Version", testContext.getAppVersion()));
        }

        appendAmlAppLogsTextContent(moreInfoContent, reportInfoSummary, testContext);
        appendVpcSequenceTextContent(moreInfoContent, reportInfoSummary, testContext);

        moreInfoContent.appendChild(moreInfoContent("Test Run Count", String.valueOf(testContext.getTestExecutionCount())));
        moreInfoContent.appendChild(moreInfoContent("Test Tags", String.join(" ", testContext.getTags())));
        moreInfoContent.appendChild(moreInfoContent("Feature File", testContext.getFeatureFile()));

        if (Objects.nonNull(testContext.getPreviousFailedStepContext())) {
            reportInfoSummary.appendChild(statusWarningIcon());
            moreInfoContent.appendChild(previousFailedStepContent(testContext.getPreviousFailedStepContext()));
        }

        moreInfoContent.prependChild(reportInfoSummary);

        return row(moreInfoContent);
    }

    private static void appendVpcSequenceTextContent(
            final Element moreInfoContent,
            final Element reportInfoSummary,
            final TestContext testContext
    ) {
        if (!testContext.getVpcContextLogs().isEmpty()) {
            final boolean hasFatalVpc = testContext.getVpcContextLogs().stream().anyMatch(TestLog::isError);

            if (hasFatalVpc) {
                reportInfoSummary.appendChild(statusFailedIcon());
            }

            moreInfoContent.appendChild(logContent(
                    testContext.getVpcContextLogs(),
                    false,
                    hasFatalVpc,
                    "VPC Sequence",
                    "*"
            ));
        }
    }

    private static void appendAmlAppLogsTextContent(
            final Element moreInfoContent,
            final Element reportInfoSummary,
            final TestContext testContext
    ) {
        if (!testContext.getAmlAppLogs().isEmpty()) {
            final boolean hasWarnings = testContext.getAmlAppLogs().stream().anyMatch(TestLog::isWarn);
            final boolean hasErrors = testContext.getAmlAppLogs().stream().anyMatch(TestLog::isError);

            if (hasErrors || hasWarnings) {
                reportInfoSummary.appendChild(statusWarningIcon());
            }

            moreInfoContent.appendChild(logContent(
                    testContext.getAmlAppLogs(),
                    hasWarnings,
                    hasErrors,
                    "AML App Logs",
                    "All Logs"
            ));
        }
    }

    private static Element logContent(
            final List<TestLog> testLogs,
            final boolean hasWarnings,
            final boolean hasErrors,
            final String title,
            final String logsTitle
    ) {
        final Element summary = HtmlElement.summary("w-100 p-1 m-0")
                .appendChild(HtmlElement.span("text-secondary", title));

        final List<Element> contentList = new ArrayList<>();

        if (hasErrors) {
            final List<TestLog> errors = testLogs.stream()
                    .filter(TestLog::isError)
                    .toList();

            summary.appendChild(logErrorsBadge("Errors: " + errors.size()));
            contentList.add(stepLogsCard(errors, "Errors"));
        }

        if (hasWarnings) {
            final List<TestLog> warnings = testLogs.stream()
                    .filter(TestLog::isWarn)
                    .toList();

            summary.appendChild(logWarningsBadge("Warnings: " + warnings.size()));
            contentList.add(stepLogsCard(warnings, "Warnings"));
        }

        contentList.add(stepLogsCard(testLogs, logsTitle));

        return detailsContainer(summary, contentList);
    }

    private static Element testStatusBadge(final Status status) {
        final Element statusContainer = HtmlElement.div("row justify-content-center");

        if (Objects.nonNull(status) && status.is(Status.PASSED)) {
            return statusContainer.appendChild(statusSuccessIcon());
        }

        return statusContainer.appendChild(statusFailedIcon());
    }

    public static Element stepStatusBadge(final Status status, final boolean hasErrors, final boolean hasException) {
        final boolean isFailed = hasException || Objects.nonNull(status) && status.is(Status.FAILED);
        final boolean isPassed = Objects.nonNull(status) && status.is(Status.PASSED);

        if (isFailed) {
            return statusFailedIcon();
        }

        if (isPassed) {
            if (hasErrors) {
                return statusWarningIcon();
            }

            return statusSuccessIcon();
        }

        return statusWarningIcon();
    }

    public static Element stepArgumentsCard(final DataTableArgument dataTableArgument) {
        if (Objects.isNull(dataTableArgument)) {
            return HtmlElement.empty();
        }

        return table(dataTableArgument.cells());
    }

    public static Element stepLogsCard(final List<TestLog> logs) {
        return card("Logs", stepLogsContainer(logs));
    }

    public static Element stepLogsCard(final List<TestLog> logs, final String title) {
        return card(title, stepLogsContainer(logs));
    }

    public static Element stepErrorsCard(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return HtmlElement.empty();
        }

        return card("Errors", stepErrorContainer(throwable));
    }

    public static Element textCard(final List<String> lines, final String title) {
        final Element content = sectionCard(title);

        if (Objects.isNull(lines) || lines.isEmpty()) {
            content.appendChild(HtmlElement.div("container text-secondary")
                    .appendChild(HtmlElement.small("", "No content found")));
            return content;
        }

        final Iterator<String> lineIterator = lines.listIterator();

        while (lineIterator.hasNext()) {
            final String line = lineIterator.next();

            content.appendChild(HtmlElement.div("container text-secondary")
                    .appendChild(HtmlElement.small("", line)));

            if (lineIterator.hasNext()) {
                content.appendChild(separator());
            }
        }

        return content;
    }

    public static Element table(final List<List<String>> rows) {
        if (Objects.isNull(rows) || rows.isEmpty()) {
            return HtmlElement.small("", "No table data found");
        }

        final Element tableBody = HtmlElement.tbody();

        rows.forEach(row -> {
            final Element tableRow = HtmlElement.tr();

            if (Objects.nonNull(row)) {
                row.forEach(cell -> tableRow.appendChild(HtmlElement.td()
                        .appendChild(HtmlElement.small("", cell))));
            }

            tableBody.appendChild(tableRow);
        });

        return HtmlElement.div("container table-container")
                .appendChild(HtmlElement.table("table table-sm table-bordered text-secondary p-0 mb-1")
                        .appendChild(tableBody));
    }

    public static Element keyValueTable(final List<KeyValueRow> rows) {
        if (Objects.isNull(rows) || rows.isEmpty()) {
            return HtmlElement.small("", "No table data found");
        }

        final List<List<String>> tableRows = rows.stream()
                .map(row -> List.of(row.key(), row.value()))
                .toList();

        return table(tableRows);
    }

    public static Element card(final String title, final Element content) {
        return sectionCard(title).appendChild(content);
    }

    public static Element sectionCard(final String title) {
        return HtmlElement.div("w-100 card p-1 mt-2 text-secondary border-gray")
                .appendChild(HtmlElement.small("border-bottom p-1 mb-2", title));
    }

    public static Element detailsContainer(final Element summary, final List<Element> contentList) {
        final Element details = HtmlElement.details("p-1 mb-2 w-100 border shadow rounded border-gray")
                .appendChild(summary);

        HtmlElement.appendAll(details, contentList);

        return HtmlElement.div("container")
                .appendChild(HtmlElement.div("p-1 justify-content-center")
                        .appendChild(details));
    }

    public static Element row(final Element content) {
        return HtmlElement.div("row justify-content-center")
                .appendChild(content);
    }

    public static Element badge(final String text) {
        return HtmlElement.small("text-info badge border mr-1 fw-normal", text);
    }

    private static Element logWarningsBadge(final String text) {
        return HtmlElement.small("text-orange ml-1 badge border border-gray fw-normal runtime-badge", text);
    }

    private static Element logErrorsBadge(final String text) {
        return HtmlElement.small("text-danger ml-1 badge border border-gray fw-normal runtime-badge", text);
    }

    private static Element moreInfoContent(final String title, final String textContent) {
        final Element summary = HtmlElement.summary("w-100 p-1 m-0")
                .appendChild(HtmlElement.span("text-secondary", title));

        final Element content = HtmlElement.div("container text-info")
                .appendChild(HtmlElement.small("", textContent));

        return detailsContainer(summary, List.of(content));
    }

    private static Element previousFailedStepContent(final StepContext failedStepContext) {
        final Element failedStepElement = step(failedStepContext);
        failedStepElement.removeClass("row");

        final Element summary = HtmlElement.summary("w-100 p-1 m-0")
                .appendChild(HtmlElement.span("text-secondary", "Previously Failed Test Step"))
                .appendChild(statusWarningIcon());

        return detailsContainer(summary, List.of(failedStepElement));
    }

    private static Element stepLogsContainer(final List<TestLog> logs) {
        if (Objects.isNull(logs) || logs.isEmpty()) {
            return HtmlElement.small("", "No logs found");
        }

        final Element logsContainer = HtmlElement.pre("container rounded p-2 m-0 logs-container");

        logs.forEach(log -> {
            final Element logDetails = HtmlElement.details("")
                    .appendChild(logSummary(log));

            if (Objects.nonNull(log.getTrace())) {
                logDetails.appendChild(logBreakdownContainer(log));
            }

            logsContainer.appendChild(logDetails);
        });

        return logsContainer;
    }

    private static Element stepErrorContainer(final Throwable throwable) {
        return HtmlElement.pre("container rounded p-2 m-0 text-danger errors-container",
                HtmlElement.nullSafe(throwable.getMessage()));
    }

    private static Element logSummary(final TestLog log) {
        final Element logLine = HtmlElement.small("", log.logLine());

        if (log.isWarn()) {
            return HtmlElement.summary("log-line row container")
                    .attr("style", "color: orange;")
                    .appendChild(logLine);
        }

        if (log.isError()) {
            return HtmlElement.summary("log-line row container")
                    .attr("style", "color: orangered;")
                    .appendChild(logLine);
        }

        return HtmlElement.summary("log-line row container")
                .attr("style", "color: lightgray;")
                .appendChild(logLine);
    }

    private static Element logBreakdownContainer(final TestLog log) {
        final Element breakdownLineContainer = HtmlElement.div("container p-2 log-breakdown-container")
                .appendChild(logBreakdownLine("Trace:", log.getTrace()));

        final List<String> args = log.getArgs();

        IntStream.range(0, args.size())
                .forEach(i -> breakdownLineContainer.appendChild(logBreakdownLine("Arg" + i + ": ", args.get(i))));

        return breakdownLineContainer;
    }

    private static Element logBreakdownLine(final String leftText, final String rightText) {
        return HtmlElement.pre("row border-bottom m-0 log-breakdown-row")
                .appendChild(HtmlElement.strong("log-breakdown-left", leftText))
                .appendChild(HtmlElement.span("log-breakdown-right", rightText));
    }

    private static Element separator() {
        return HtmlElement.div("container")
                .appendChild(HtmlElement.div("mb-2 pb-1 border-bottom w-100"));
    }

    private static Element statusSuccessIcon() {
        return HtmlElement.fragment("""
                <span class="text-success scenario-status">
                    <svg fill="currentColor" width="20" height="20" viewBox="0 0 448 512">
                        <path d="M438.6 105.4c12.5 12.5 12.5 32.8 0 45.3l-256 256c-12.5 12.5-32.8 12.5-45.3 0l-128-128c-12.5-12.5-12.5-32.8 0-45.3s32.8-12.5 45.3 0L160 338.7 393.4 105.4c12.5-12.5 32.8-12.5 45.3 0z"></path>
                    </svg>
                </span>
                """);
    }

    private static Element statusFailedIcon() {
        return HtmlElement.fragment("""
                <span class="text-danger scenario-status">
                    <svg fill="currentColor" width="20" height="20" viewBox="0 0 384 512">
                        <path d="M342.6 150.6c12.5-12.5 12.5-32.8 0-45.3s-32.8-12.5-45.3 0L192 210.7 86.6 105.4c-12.5-12.5-32.8-12.5-45.3 0s-12.5 32.8 0 45.3L146.7 256 41.4 361.4c-12.5 12.5-12.5 32.8 0 45.3s32.8 12.5 45.3 0L192 301.3 297.4 406.6c12.5 12.5 32.8 12.5 45.3 0s12.5-32.8 0-45.3L237.3 256 342.6 150.6z"></path>
                    </svg>
                </span>
                """);
    }

    private static Element statusWarningIcon() {
        return HtmlElement.fragment("""
                <span class="text-warning scenario-status">
                    <svg fill="currentColor" width="20" height="20" viewBox="0 0 64 512">
                        <path d="M64 64c0-17.7-14.3-32-32-32S0 46.3 0 64V320c0 17.7 14.3 32 32 32s32-14.3 32-32V64zM32 480a40 40 0 1 0 0-80 40 40 0 1 0 0 80z"></path>
                    </svg>
                </span>
                """);
    }

    public record KeyValueRow(String key, String value) {
    }
}

package com.ing.P09498.cucumber.plugins.logger.context;

import com.ing.P09498.cucumber.plugins.logger.TestLog;
import com.ing.P09498.cucumber.plugins.logger.evidence.ReportElementHelper;
import com.ing.P09498.cucumber.plugins.logger.utils.TestTimeUtils;
import io.cucumber.plugin.event.DataTableArgument;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.Step;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.nodes.Element;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class StepContext {

    private final List<TestLog> testRunLogs = new ArrayList<>();
    private final String keyword;
    private final String name;
    private final DataTableArgument stepArgument;
    private final String startTime;
    private String endTime;
    private Duration duration;
    private Status status;
    private Throwable exception;

    public StepContext(final PickleStepTestStep testStep, final Instant startTime) {
        final Step step = testStep.getStep();

        this.keyword = step.getKeyword();
        this.name = step.getText();
        this.stepArgument = step.getArgument() instanceof DataTableArgument dataTableArgument
                ? dataTableArgument
                : null;
        this.startTime = TestTimeUtils.instantToLongDate(startTime);
    }

    public void addLog(final TestLog log) {
        if (Objects.nonNull(log)) {
            this.testRunLogs.add(log);
        }
    }

    public void addResult(final Result result, final Instant endTime) {
        this.duration = result.getDuration();
        this.endTime = TestTimeUtils.instantToLongDate(endTime);
        this.status = result.getStatus();
        this.exception = result.getError();
    }

    public Element toHtmlElement() {
        return ReportElementHelper.step(this);
    }
}


de inlocuit toHtmlReport in testcontext

        public String toHtmlReport() throws TestReportException {
    try {
        return ReportElementHelper.htmlReport(this).outerHtml();
    } catch (final Exception e) {
        final StackTraceElement[] stackTraceElements = e.getStackTrace();
        final StackTraceElement traceElement = stackTraceElements[0];

        final String message = String.format(
                "Test context to HTML process failed for scenario:%s%nReason: %s:%s",
                this.getName(),
                traceElement.getClassName(),
                traceElement.getLineNumber()
        );

        throw new TestReportException(message);
    }
}
