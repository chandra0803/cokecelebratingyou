{
    <%-- Sets a custom message when chart is loading.
         FC Default: "Loading Chart. Please Wait." --%>
    "LoadingText" : "<cms:contentText key='LOADING_DATA' code='report.display.page' />",
    <%-- Sets a custom message when data is being parsed by chart.
         FC Default: "Reading Data. Please Wait." --%>
    "ParsingDataText" : "<cms:contentText key='PARSING_DATA' code='report.display.page' />",
    <%-- Sets a custom message when the chart has retrieved data which does not contain any data for chart to plot or the data does not conform to the data structure required by the chart type.
         FC Default: "No data to display." --%>
    "ChartNoDataText" : "<cms:contentText key='NO_DATA_FOUND' code='report.display.page' />",
    <%-- Sets a custom message when the chart is being drawn.
         FC Default: "Rendering Chart. Please Wait." --%>
    "RenderingChartText" : "<cms:contentText key='RENDERING_CHART' code='report.display.page' />",
    <%-- Sets a custom message when there is error in loading the chart data from the data URL provided as datasource.This may happen when the URL is invalid or inaccessible.
         FC Default: "Error in loading data." --%>
    "LoadDataErrorText" : "<cms:contentText key='LOAD_DATA_ERROR' code='report.display.page' />"
}