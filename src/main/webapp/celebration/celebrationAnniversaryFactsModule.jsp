<!-- ======== CELEBRATION ANNIVERSARY MODULE ======== -->
<%@ include file="/include/taglib.jspf"%>
<script type="text/template" id="celebrationAnniversaryFactsModuleTpl">
  <div class="module-liner">
    <div class="wide-view">
      <div class="module-content">
        <div id="yearThatWasCarousel" class="carousel">
          <div class="cycle carousel-inner">
            <c:forEach var="carouselItem" items="${celebrationValue.yearThatWasList}">
              <div class="item">
                <img src="${carouselItem.img}" alt="" />
                <div class="year-content">
                  <div class="year">${carouselItem.year}</div>
                  <div class="year-desc">${carouselItem.content}</div>
                </div>
              </div>
            </c:forEach>
          </div>
        </div> <!-- ./carousel -->
      </div> <!-- ./module-content -->
    </div>
  </div>
</script>
