private static final String SCRIPT = """
        async function dg(e){if(e.dataset.loaded==='true')return;try{const b=Uint8Array.from(atob(e.dataset.gz),c=>c.charCodeAt(0));if(!('DecompressionStream'in window))return;const s=new Blob([b]).stream().pipeThrough(new DecompressionStream('gzip'));e.textContent=await new Response(s).text();e.dataset.loaded='true';e.removeAttribute('data-gz');}catch(x){}}
        document.addEventListener('toggle',e=>{if(e.target.open)e.target.querySelectorAll('[data-gz]').forEach(dg);},true);
        """;

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

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public final class ReportElementHelper {

    private static final String REPORT_TITLE = "Test Evidence";

    private static final String CONTENT_CONTAINER_CLASSES = "container mt-2";
    private static final String CONTENT_CARD_CLASSES = "card h-auto w-100 border border-gray shadow rounded";
    private static final String CARD_HEADER_CONTAINER_CLASSES = "container p-2";
    private static final String CARD_HEADER_CLASSES = "card-header text-center border border-gray shadow rounded";
    private static final String CARD_BODY_CLASSES = "card-body p-5 pt-1 pb-1";

    private static final String ROW_CENTER_CLASSES = "row justify-content-center";
    private static final String SECTION_CARD_CLASSES = "w-100 card p-1 mt-2 text-secondary border-gray";
    private static final String SECTION_TITLE_CLASSES = "border-bottom p-1 mb-2";
    private static final String DETAILS_CLASSES = "p-1 mb-2 w-100 border shadow rounded border-gray";
    private static final String STEP_DETAILS_CLASSES = "p-2 mb-2 w-100 border shadow rounded border-gray";
    private static final String SUMMARY_CLASSES = "w-100 p-1 m-0";

    private static final String BADGE_CLASSES = "text-info badge border mr-1 fw-normal";
    private static final String FLOAT_BADGE_CLASSES = "ml-1 badge border border-gray fw-normal runtime-badge";
    private static final String RUNTIME_BADGE_CLASSES = "text-info badge fw-normal runtime-badge";

    private static final String LOGS_CONTAINER_CLASSES = "container rounded p-2 m-0 logs-container";
    private static final String ERRORS_CONTAINER_CLASSES = "container rounded p-2 m-0 text-danger errors-container";
    private static final String LOG_SUMMARY_CLASSES = "log-line row container";
    private static final String LOG_BREAKDOWN_CONTAINER_CLASSES = "container p-2 log-breakdown-container";
    private static final String LOG_BREAKDOWN_ROW_CLASSES = "row border-bottom m-0 log-breakdown-row";

    private static final String TABLE_CONTAINER_CLASSES = "container table-container";
    private static final String TABLE_CLASSES = "table table-sm table-bordered text-secondary p-0 mb-1";

    private ReportElementHelper() {
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    public static Element htmlReport(final TestContext testContext) {
        return HtmlElement.html()
                .appendChild(buildHead())
                .appendChild(buildBody(testContext));
    }

    public static Element contentContainer(final TestContext testContext) {
        return buildReportContainer(testContext);
    }

    public static Element step(final StepContext stepContext) {
        return row(buildStepDetails(stepContext));
    }

    public static Element stepArgumentsCard(final DataTableArgument dataTableArgument) {
        if (Objects.isNull(dataTableArgument)) {
            return HtmlElement.empty();
        }

        return table(dataTableArgument.cells());
    }

    public static Element stepLogsCard(final List<TestLog> logs) {
        return stepLogsCard(logs, "Logs");
    }

    public static Element stepLogsCard(final List<TestLog> logs, final String title) {
        return card(title, buildLogsContainer(logs));
    }

    public static Element stepErrorsCard(final Throwable throwable) {
        if (Objects.isNull(throwable)) {
            return HtmlElement.empty();
        }

        return card("Errors", buildErrorContainer(throwable));
    }

    public static Element textCard(final List<String> lines, final String title) {
        final Element content = sectionCard(title);

        if (Objects.isNull(lines) || lines.isEmpty()) {
            content.appendChild(textLine("No content found"));
            return content;
        }

        final Iterator<String> lineIterator = lines.iterator();

        while (lineIterator.hasNext()) {
            content.appendChild(textLine(lineIterator.next()));

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

        rows.forEach(row -> tableBody.appendChild(tableRow(row)));

        return HtmlElement.div(TABLE_CONTAINER_CLASSES)
                .appendChild(HtmlElement.table(TABLE_CLASSES)
                        .appendChild(tableBody));
    }

    public static Element keyValueTable(final List<KeyValueRow> rows) {
        if (Objects.isNull(rows) || rows.isEmpty()) {
            return HtmlElement.small("", "No table data found");
        }

        final List<List<String>> tableRows = rows.stream()
                .map(row -> List.of(
                        HtmlElement.nullSafe(row.key()),
                        HtmlElement.nullSafe(row.value())
                ))
                .toList();

        return table(tableRows);
    }

    public static Element card(final String title, final Element content) {
        return sectionCard(title).appendChild(content);
    }

    public static Element sectionCard(final String title) {
        return HtmlElement.div(SECTION_CARD_CLASSES)
                .appendChild(HtmlElement.small(SECTION_TITLE_CLASSES, title));
    }

    public static Element detailsContainer(final Element summary, final List<Element> contentList) {
        final Element details = HtmlElement.details(DETAILS_CLASSES)
                .appendChild(summary);

        HtmlElement.appendAll(details, contentList);

        return HtmlElement.div("container")
                .appendChild(HtmlElement.div("p-1 justify-content-center")
                        .appendChild(details));
    }

    public static Element row(final Element content) {
        return HtmlElement.div(ROW_CENTER_CLASSES)
                .appendChild(content);
    }

    public static Element badge(final String text) {
        return HtmlElement.small(BADGE_CLASSES, text);
    }

    public static Element stepStatusBadge(
            final Status status,
            final boolean hasErrors,
            final boolean hasException
    ) {
        if (isFailed(status, hasException)) {
            return statusFailedIcon();
        }

        if (isPassed(status)) {
            return hasErrors
                    ? statusWarningIcon()
                    : statusSuccessIcon();
        }

        return statusWarningIcon();
    }

    public record KeyValueRow(String key, String value) {
    }

    // -------------------------------------------------------------------------
    // Document structure
    // -------------------------------------------------------------------------

    private static Element buildHead() {
        return HtmlElement.head()
                .appendChild(HtmlElement.meta("text/html; charset=UTF-8"))
                .appendChild(HtmlElement.title(REPORT_TITLE))
                .appendChild(ReportStyle.styleElement());
    }

    private static Element buildBody(final TestContext testContext) {
        return HtmlElement.body()
                .appendChild(buildReportContainer(testContext))
                .appendChild(HtmlElement.div("content-margin"));
    }

    private static Element buildReportContainer(final TestContext testContext) {
        return HtmlElement.div(CONTENT_CONTAINER_CLASSES)
                .appendChild(HtmlElement.div("row mb-3"))
                .appendChild(HtmlElement.div(CONTENT_CARD_CLASSES)
                        .appendChild(buildScenarioHeader(testContext))
                        .appendChild(buildScenarioBody(testContext)));
    }

    // -------------------------------------------------------------------------
    // Scenario section
    // -------------------------------------------------------------------------

    private static Element buildScenarioHeader(final TestContext testContext) {
        return HtmlElement.div(CARD_HEADER_CONTAINER_CLASSES)
                .appendChild(HtmlElement.div(CARD_HEADER_CLASSES)
                        .appendChild(testStatusBadge(testContext.getStatus()))
                        .appendChild(scenarioTitle(testContext.getName()))
                        .appendChild(scenarioBadges(testContext)));
    }

    private static Element buildScenarioBody(final TestContext testContext) {
        final Element body = HtmlElement.div(CARD_BODY_CLASSES);

        testContext.getStepContextMap()
                .values()
                .forEach(stepContext -> body.appendChild(step(stepContext)));

        body.appendChild(separator());
        body.appendChild(buildMoreInfoSection(testContext));

        return body;
    }

    private static Element scenarioTitle(final String scenarioName) {
        return HtmlElement.withClasses(HtmlElement.element("h5"), "text-secondary fs-4 mb-2 p-1 border-bottom")
                .text(HtmlElement.nullSafe(scenarioName));
    }

    private static Element scenarioBadges(final TestContext testContext) {
        return HtmlElement.div("text-secondary row pb-1 justify-content-center")
                .appendChild(badge("Started On: " + HtmlElement.nullSafe(testContext.getStartTime())))
                .appendChild(badge("Total Runtime: " + HtmlElement.nullSafe(testContext.getDuration())));
    }

    private static Element testStatusBadge(final Status status) {
        final Element statusContainer = HtmlElement.div(ROW_CENTER_CLASSES);

        if (isPassed(status)) {
            return statusContainer.appendChild(statusSuccessIcon());
        }

        return statusContainer.appendChild(statusFailedIcon());
    }

    // -------------------------------------------------------------------------
    // Step section
    // -------------------------------------------------------------------------

    private static Element buildStepDetails(final StepContext stepContext) {
        return HtmlElement.details(STEP_DETAILS_CLASSES)
                .appendChild(buildStepSummary(stepContext))
                .appendChild(stepArgumentsCard(stepContext.getStepArgument()))
                .appendChild(stepLogsCard(stepContext.getTestRunLogs()))
                .appendChild(stepErrorsCard(stepContext.getException()));
    }

    private static Element buildStepSummary(final StepContext stepContext) {
        return HtmlElement.summary(SUMMARY_CLASSES)
                .appendChild(HtmlElement.span("step-keyword", HtmlElement.nullSafe(stepContext.getKeyword())))
                .appendChild(HtmlElement.span("text-secondary", HtmlElement.nullSafe(stepContext.getName())))
                .appendChild(stepStatusBadge(
                        stepContext.getStatus(),
                        hasLogWarningsOrErrors(stepContext.getTestRunLogs()),
                        Objects.nonNull(stepContext.getException())
                ))
                .appendChild(HtmlElement.small(RUNTIME_BADGE_CLASSES, durationText(stepContext.getDuration())));
    }

    private static boolean hasLogWarningsOrErrors(final List<TestLog> logs) {
        return Objects.nonNull(logs) && logs.stream().anyMatch(TestLog::hasErrors);
    }

    // -------------------------------------------------------------------------
    // More Info section
    // -------------------------------------------------------------------------

    private static Element buildMoreInfoSection(final TestContext testContext) {
        final Element summary = moreInfoSummary();
        final Element details = HtmlElement.details(DETAILS_CLASSES);

        appendAppVersion(details, testContext);
        appendAmlAppLogs(details, summary, testContext);
        appendVpcSequenceLogs(details, summary, testContext);
        appendExecutionMetadata(details, testContext);
        appendPreviousFailedStep(details, summary, testContext);

        details.prependChild(summary);

        return row(details);
    }

    private static Element moreInfoSummary() {
        return HtmlElement.summary(SUMMARY_CLASSES)
                .appendChild(HtmlElement.span("text-info", "More Info"));
    }

    private static void appendAppVersion(final Element details, final TestContext testContext) {
        if (Objects.nonNull(testContext.getAppVersion())) {
            details.appendChild(moreInfoContent("App Version", testContext.getAppVersion()));
        }
    }

    private static void appendAmlAppLogs(
            final Element details,
            final Element summary,
            final TestContext testContext
    ) {
        final List<TestLog> logs = testContext.getAmlAppLogs();

        if (logs.isEmpty()) {
            return;
        }

        final boolean hasWarnings = logs.stream().anyMatch(TestLog::isWarn);
        final boolean hasErrors = logs.stream().anyMatch(TestLog::isError);

        if (hasWarnings || hasErrors) {
            summary.appendChild(statusWarningIcon());
        }

        details.appendChild(buildGroupedLogSection(
                logs,
                hasWarnings,
                hasErrors,
                "AML App Logs",
                "All Logs"
        ));
    }

    private static void appendVpcSequenceLogs(
            final Element details,
            final Element summary,
            final TestContext testContext
    ) {
        final List<TestLog> logs = testContext.getVpcContextLogs();

        if (logs.isEmpty()) {
            return;
        }

        final boolean hasErrors = logs.stream().anyMatch(TestLog::isError);

        if (hasErrors) {
            summary.appendChild(statusFailedIcon());
        }

        details.appendChild(buildGroupedLogSection(
                logs,
                false,
                hasErrors,
                "VPC Sequence",
                "*"
        ));
    }

    private static void appendExecutionMetadata(final Element details, final TestContext testContext) {
        details.appendChild(moreInfoContent("Test Run Count", String.valueOf(testContext.getTestExecutionCount())));
        details.appendChild(moreInfoContent("Test Tags", String.join(" ", testContext.getTags())));
        details.appendChild(moreInfoContent("Feature File", testContext.getFeatureFile()));
    }

    private static void appendPreviousFailedStep(
            final Element details,
            final Element summary,
            final TestContext testContext
    ) {
        final StepContext previousFailedStepContext = testContext.getPreviousFailedStepContext();

        if (Objects.isNull(previousFailedStepContext)) {
            return;
        }

        summary.appendChild(statusWarningIcon());
        details.appendChild(previousFailedStepContent(previousFailedStepContext));
    }

    private static Element moreInfoContent(final String title, final String textContent) {
        final Element summary = HtmlElement.summary(SUMMARY_CLASSES)
                .appendChild(HtmlElement.span("text-secondary", title));

        final Element content = HtmlElement.div("container text-info")
                .appendChild(HtmlElement.small("", HtmlElement.nullSafe(textContent)));

        return detailsContainer(summary, List.of(content));
    }

    private static Element previousFailedStepContent(final StepContext failedStepContext) {
        final Element summary = HtmlElement.summary(SUMMARY_CLASSES)
                .appendChild(HtmlElement.span("text-secondary", "Previously Failed Test Step"))
                .appendChild(statusWarningIcon());

        return detailsContainer(summary, List.of(buildStepDetails(failedStepContext)));
    }

    // -------------------------------------------------------------------------
    // Log sections
    // -------------------------------------------------------------------------

    private static Element buildGroupedLogSection(
            final List<TestLog> logs,
            final boolean hasWarnings,
            final boolean hasErrors,
            final String title,
            final String allLogsTitle
    ) {
        final Element summary = HtmlElement.summary(SUMMARY_CLASSES)
                .appendChild(HtmlElement.span("text-secondary", title));

        final List<Element> content = new ArrayList<>();

        if (hasErrors) {
            final List<TestLog> errors = filterErrors(logs);

            summary.appendChild(errorCountBadge(errors.size()));
            content.add(stepLogsCard(errors, "Errors"));
        }

        if (hasWarnings) {
            final List<TestLog> warnings = filterWarnings(logs);

            summary.appendChild(warningCountBadge(warnings.size()));
            content.add(stepLogsCard(warnings, "Warnings"));
        }

        content.add(stepLogsCard(logs, allLogsTitle));

        return detailsContainer(summary, content);
    }

    private static Element buildLogsContainer(final List<TestLog> logs) {
        if (Objects.isNull(logs) || logs.isEmpty()) {
            return HtmlElement.small("", "No logs found");
        }

        final Element logsContainer = HtmlElement.pre(LOGS_CONTAINER_CLASSES);

        logs.forEach(log -> logsContainer.appendChild(logEntry(log)));

        return logsContainer;
    }

    private static Element logEntry(final TestLog log) {
        final Element logDetails = HtmlElement.details("")
                .appendChild(logSummary(log));

        if (Objects.nonNull(log.getTrace())) {
            logDetails.appendChild(logBreakdown(log));
        }

        return logDetails;
    }

    private static Element logSummary(final TestLog log) {
        final String color = logColor(log);

        return HtmlElement.summary(LOG_SUMMARY_CLASSES)
                .attr("style", "color: " + color + ";")
                .appendChild(HtmlElement.small("", log.logLine()));
    }

    private static Element logBreakdown(final TestLog log) {
        final Element breakdown = HtmlElement.div(LOG_BREAKDOWN_CONTAINER_CLASSES)
                .appendChild(logBreakdownLine("Trace:", log.getTrace()));

        final List<String> args = log.getArgs();

        IntStream.range(0, args.size())
                .forEach(index -> breakdown.appendChild(
                        logBreakdownLine("Arg" + index + ": ", args.get(index))
                ));

        return breakdown;
    }

    private static Element logBreakdownLine(final String leftText, final String rightText) {
        return HtmlElement.pre(LOG_BREAKDOWN_ROW_CLASSES)
                .appendChild(HtmlElement.strong("log-breakdown-left", leftText))
                .appendChild(HtmlElement.span("log-breakdown-right", HtmlElement.nullSafe(rightText)));
    }

    private static Element buildErrorContainer(final Throwable throwable) {
        return HtmlElement.pre(ERRORS_CONTAINER_CLASSES, HtmlElement.nullSafe(throwable.getMessage()));
    }

    private static List<TestLog> filterErrors(final List<TestLog> logs) {
        return logs.stream()
                .filter(TestLog::isError)
                .toList();
    }

    private static List<TestLog> filterWarnings(final List<TestLog> logs) {
        return logs.stream()
                .filter(TestLog::isWarn)
                .toList();
    }

    private static String logColor(final TestLog log) {
        if (log.isError()) {
            return "orangered";
        }

        if (log.isWarn()) {
            return "orange";
        }

        return "lightgray";
    }

    // -------------------------------------------------------------------------
    // Small reusable UI components
    // -------------------------------------------------------------------------

    private static Element textLine(final String text) {
        return HtmlElement.div("container text-secondary")
                .appendChild(HtmlElement.small("", HtmlElement.nullSafe(text)));
    }

    private static Element tableRow(final List<String> cells) {
        final Element tableRow = HtmlElement.tr();

        if (Objects.isNull(cells)) {
            return tableRow;
        }

        cells.forEach(cell -> tableRow.appendChild(tableCell(cell)));

        return tableRow;
    }

    private static Element tableCell(final String cell) {
        return HtmlElement.td()
                .appendChild(HtmlElement.small("", HtmlElement.nullSafe(cell)));
    }

    private static Element separator() {
        return HtmlElement.div("container")
                .appendChild(HtmlElement.div("mb-2 pb-1 border-bottom w-100"));
    }

    private static Element warningCountBadge(final int count) {
        return HtmlElement.small("text-orange " + FLOAT_BADGE_CLASSES, "Warnings: " + count);
    }

    private static Element errorCountBadge(final int count) {
        return HtmlElement.small("text-danger " + FLOAT_BADGE_CLASSES, "Errors: " + count);
    }

    private static String durationText(final Duration duration) {
        if (Objects.isNull(duration)) {
            return "0m 0s 0ms";
        }

        return TestTimeUtils.durationToTimestamp(duration);
    }

    // -------------------------------------------------------------------------
    // Status helpers
    // -------------------------------------------------------------------------

    private static boolean isPassed(final Status status) {
        return Objects.nonNull(status) && status.is(Status.PASSED);
    }

    private static boolean isFailed(final Status status, final boolean hasException) {
        return hasException || Objects.nonNull(status) && status.is(Status.FAILED);
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

package com.ing.P09498.cucumber.plugins.logger.evidence.compression;

import lombok.Getter;

@Getter
public class CompressedText {

    private final String value;
    private final boolean compressed;
    private final int originalSize;
    private final int storedSize;

    private CompressedText(
            final String value,
            final boolean compressed,
            final int originalSize,
            final int storedSize
    ) {
        this.value = value;
        this.compressed = compressed;
        this.originalSize = originalSize;
        this.storedSize = storedSize;
    }

    public static CompressedText plain(final String value) {
        final String safeValue = value == null ? "" : value;
        return new CompressedText(
                safeValue,
                false,
                safeValue.length(),
                safeValue.length()
        );
    }

    public static CompressedText compressed(
            final String value,
            final int originalSize,
            final int storedSize
    ) {
        return new CompressedText(
                value,
                true,
                originalSize,
                storedSize
        );
    }
}

package com.ing.P09498.cucumber.plugins.logger.evidence.compression;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public final class TextCompressionUtils {

    private static final int MIN_COMPRESSION_SIZE = 1024;

    private TextCompressionUtils() {
    }

    public static CompressedText compressIfUseful(final String text) {
        if (Objects.isNull(text) || text.length() < MIN_COMPRESSION_SIZE) {
            return CompressedText.plain(text);
        }

        try {
            final byte[] originalBytes = text.getBytes(StandardCharsets.UTF_8);
            final byte[] compressedBytes = gzip(originalBytes);
            final String base64 = Base64.getEncoder().encodeToString(compressedBytes);

            if (base64.length() >= text.length()) {
                return CompressedText.plain(text);
            }

            return CompressedText.compressed(
                    base64,
                    text.length(),
                    base64.length()
            );
        } catch (final Exception e) {
            return CompressedText.plain(text);
        }
    }

    private static byte[] gzip(final byte[] bytes) throws Exception {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(bytes);
        }

        return byteArrayOutputStream.toByteArray();
    }
}

package com.ing.P09498.cucumber.plugins.logger;

import com.ing.P09498.cucumber.plugins.logger.evidence.compression.CompressedText;
import com.ing.P09498.cucumber.plugins.logger.evidence.compression.TextCompressionUtils;
import lombok.Getter;

@Getter
public class TestLogArgument {

    private final int index;
    private final CompressedText content;

    public TestLogArgument(final int index, final String value) {
        this.index = index;
        this.content = TextCompressionUtils.compressIfUseful(value);
    }

    public String label() {
        return "Arg" + index + ": ";
    }

    public boolean isCompressed() {
        return content.isCompressed();
    }

    public String value() {
        return content.getValue();
    }

    public int originalSize() {
        return content.getOriginalSize();
    }

    public int storedSize() {
        return content.getStoredSize();
    }
}

package com.ing.P09498.cucumber.plugins.logger;

import com.ing.P09498.cucumber.plugins.logger.utils.TestTimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.event.Level;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.util.ArrayList;
import java.util.List;

import static com.ing.P09498.utils.TransformationUtils.objectToPrettyString;

@Getter
@Setter
public class TestLog {

    private final String formattedMessage;
    private final String timestamp;
    private final List<TestLogArgument> args = new ArrayList<>();
    private final String message;
    private final String trace;
    private Level level;
    private boolean isSimpleLog = false;

    public TestLog(final String message, final Level level, final Object... objects) {
        this.timestamp = TestTimeUtils.dateNow();
        this.formattedMessage = formatMessage(message, objects);
        this.message = message;
        this.level = level;
        this.trace = generateTrace();
    }

    public String logLine() {
        if (isSimpleLog) {
            return formattedMessage;
        }

        return String.format("%s %s - %s", timestamp, level, formattedMessage);
    }

    private String formatMessage(final String message, final Object... objects) {
        final FormattingTuple formattingTuple = MessageFormatter.arrayFormat(message, objects);

        for (int index = 0; index < objects.length; index++) {
            final String prettyArgument = objectToPrettyString(objects[index]);
            args.add(new TestLogArgument(index, prettyArgument));
        }

        return formattingTuple.getMessage();
    }

    private String generateTrace() {
        final StackTraceElement[] stackTrace = new Exception().getStackTrace();
        final StackTraceElement element = stackTrace[3];

        return String.format("%s:%s", element.getFileName(), element.getLineNumber());
    }

    public boolean hasErrors() {
        return isError() || isWarn();
    }

    public boolean isWarn() {
        return level.equals(Level.WARN);
    }

    public boolean isError() {
        return level.equals(Level.ERROR);
    }
}

package com.ing.P09498.cucumber.plugins.logger.evidence;

import org.jsoup.nodes.Element;

public final class ReportScript {

    private ReportScript() {
    }

    public static Element inlineScriptElement() {
        return HtmlElement.element("script").html(SCRIPT);
    }

    private static final String SCRIPT = """
            async function dg(e){if(e.dataset.loaded==='true')return;try{e.textContent='Loading...';const b=Uint8Array.from(atob(e.dataset.gz),c=>c.charCodeAt(0));if(!('DecompressionStream'in window)){e.textContent='Browser does not support gzip decompression';return;}const s=new Blob([b]).stream().pipeThrough(new DecompressionStream('gzip'));const t=await new Response(s).text();e.textContent=t;e.dataset.loaded='true';e.removeAttribute('data-gz');}catch(x){e.textContent='Failed to decompress content: '+x.message;}}
            document.addEventListener('click',e=>{const t=e.target.closest('[data-gz]');if(t)dg(t);});
            document.addEventListener('toggle',e=>{if(e.target.open){e.target.querySelectorAll('[data-gz]').forEach(dg);}},true);
            """;
}

modificare in reportElementHelper

    private static Element buildHead() {
    return HtmlElement.head()
            .appendChild(HtmlElement.meta("text/html; charset=UTF-8"))
            .appendChild(HtmlElement.title(REPORT_TITLE))
            .appendChild(ReportStyle.inlineStyleElement())
            .appendChild(ReportScript.inlineScriptElement());
}


private static Element logArgumentBreakdownLine(final TestLogArgument argument) {
    return HtmlElement.pre(LOG_BREAKDOWN_ROW_CLASSES)
            .appendChild(HtmlElement.strong("ll", argument.label()))
            .appendChild(logArgumentValue(argument));
}

private static Element logArgumentValue(final TestLogArgument argument) {
    if (!argument.isCompressed()) {
        return HtmlElement.span("lv", argument.value());
    }

    return HtmlElement.span("lv cz", compressedArgumentPlaceholder(argument))
            .attr("data-gz", argument.value())
            .attr("data-loaded", "false")
            .attr("title", compressedArgumentTitle(argument));
}

private static String compressedArgumentPlaceholder(final TestLogArgument argument) {
    return String.format(
            "Compressed content. Click to load. Original: %s chars, stored: %s chars",
            argument.originalSize(),
            argument.storedSize()
    );
}

private static String compressedArgumentTitle(final TestLogArgument argument) {
    return String.format(
            "Click to decompress. Original size: %s chars. Stored size: %s chars.",
            argument.originalSize(),
            argument.storedSize()
    );
}
