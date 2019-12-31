<%@ page import="erp.entity.Bakim" %>
<%@ page import="erp.entity.Calisan" %>
<%@ page import="erp.entity.Genel" %>
<%@ page import="erp.entity.Mamul" %>
<%@ page import="erp.util.Util" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title><%=Genel.TITLE%>
    </title>

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/irfan.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/fonts.css">
    <link rel="stylesheet" href="css/all.css">
    <link rel="stylesheet" href="css/jquery-ui-1.10.0.custom.css">
    <link rel="stylesheet/less" type="text/css" href="less/datepicker.less">

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css?<%=Util.nanoTime%>">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%@ include file="logincheck.jsp" %>
<%
    @SuppressWarnings("unchecked")
    List<Calisan> calisan = (List<Calisan>) request.getAttribute("calisan");
    @SuppressWarnings("unchecked")
    Map<Integer, Calisan> calisanmap = (HashMap<Integer, Calisan>) request.getAttribute("calisanmap");
    @SuppressWarnings("unchecked")
    List<Mamul> mamul = (List<Mamul>) request.getAttribute("mamul");
    @SuppressWarnings("unchecked")
    Map<Integer, Mamul> mamulmap = (HashMap<Integer, Mamul>) request.getAttribute("mamulmap");
    @SuppressWarnings("unchecked")
    List<Bakim> bakimrapor = (List<Bakim>) request.getAttribute("bakimrapor");
    String mamulid = request.getAttribute("mamulid")==null?"": (String) request.getAttribute("mamulid");
    String calisanid = request.getAttribute("calisanid")==null?"": (String)request.getAttribute("calisanid");
    String bas_tarih = (String) request.getAttribute("bas_tarih");
    String bit_tarih = (String) request.getAttribute("bit_tarih");
    String excelsql = (String) request.getAttribute("excelsql");

    if(bas_tarih == null || bas_tarih.isEmpty())
        bas_tarih = Util.convertLocalDateTarihTR(LocalDate.now().minusDays(730)).toString();
    if(bit_tarih == null || bit_tarih.isEmpty())
        bit_tarih = Util.convertLocalDateTarihTR(LocalDate.now()).toString();
%>

<script>
    var mamul = [
        <%String delimeter = "";
        for (Mamul m : mamul) {
            out.println(delimeter
        + " { id:" + m.getId()
        + ",bilesenid:" + m.getId()
        + ",bilesenad:'" + m.getAd().replaceAll("'", "") + "'"
        + ",bilesenkod:'" + m.getKod() + "'"
        + ",label:'" + m.getKod() + "'" + "}");
            delimeter = ",";
        }%>];
    var mamuldef = mamul;

    var mamul2 = [
        <%delimeter = "";
        for (Mamul m : mamul) {
            out.println(delimeter + " { value:'"+ m.getKod() + "', label:'" + m.getKod() + " ["+m.getAd().replaceAll("'", "") +"]',id:" + m.getId() + "}");
            delimeter = ",";
        }%>];

</script>


<div class="container">
    <div class="row text-warning" style="text-align: center;">
        <label class="text-danger rounded">Mamül Kalıp Bakım Raporu</label>
    </div>
    <div class="row">
        <form class="form-inline" role="form" name="raporform" id="raporform" method="post" action="bakimrapor">
            <div class="form-group">
                <label for="bas_tarih" class="text-baslik">Başlangıç Tarihi</label>
                <div>
                    <input type="text" class="form-control xm" name="bas_tarih" id="bas_tarih" placeholder="Tarih">
                </div>
            </div>

            <div class="form-group">
                <label for="bit_tarih" class="text-baslik">Bitiş Tarihi</label>
                <div>
                    <input type="text" class="form-control xm" name="bit_tarih" id="bit_tarih" placeholder="Tarih">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulkod" class="text-baslik">Mamül Kodu</label>
                <div>
                    <input type="text" class="form-control small" name="mamulkod" id="mamulkod" autocomplete="off">
                    <input type="hidden" name="mamulid" id="mamulid">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulad" class="text-baslik">Mamül Adı</label>
                <div>
                    <input type="text" class="form-control small" name="mamulad" id="mamulad" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="calisanid" class="text-baslik">Çalışan</label>
                <div>
                    <select class="form-control small" id="calisanid" name="calisanid">
                        <%
                            out.println("<option value=''>...Tüm...</option>");
                            for (Calisan c : calisan) {
                                if (c.getId().toString().equals(calisanid)) {
                                    out.println("<option value='" + c.getId() + "' selected>" + c.getFullName() + "</option>");
                                } else {
                                    out.println("<option value='" + c.getId() + "'>" + c.getFullName() + "</option>");
                                }
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="raporgetir" class="text-success bgsaydam">&nbsp;&nbsp;</label>
                <div>
                    <button type="button" class="btn btn-danger" name="raporgetir" id="raporgetir">Rapor Getir</button>
                    <input type="hidden" name="raporgetirid" id="raporgetirid" value="0">
                </div>
            </div>

            <div class="clearfix"></div>

        </form>
    </div>
    <%
        int sayac = 0;
        /*
        Integer MONTH_LENGTH = 30;
        String kalanSure = "";
        java.sql.Date bakimTarihi = mamulmap.get(r.getMamulid()).getBakimTarihi();
        if (bakimTarihi != null) {
            LocalDate localDate = bakimTarihi.toLocalDate();
            localDate = localDate.plusDays(mamulmap.get(r.getMamulid()).getBakimSuresi() * MONTH_LENGTH);
            Period period = Period.between(LocalDate.now(), localDate);
            //long daysBetween = DAYS.between(LocalDate.now(), localDate);
            String yil = period.getYears() > 0 ? period.getYears() + " yıl, " : "";
            String ay = period.getMonths() > 0 ? period.getMonths() + " ay, " : "";
            String gun = period.getDays() > 0 ? period.getDays() + " gün" : "";
            kalanSure = yil + ay + gun;
        }
         */
    %>
    <div class="row" style="font-size:12px;">
        <table class="tableplan">
            <script type="text/javascript" charset="utf-8">
                let plan = {};
                let plantemp = {};
            </script>
            <%
                for (Bakim r : bakimrapor) {
                if (sayac++ == 0) {
            %>
            <tr>
                <th>Bakıma Kalan Baskı</th>
                <th>Baskı Sayısı</th>
                <th>Üretim Miktarı</th>
                <th>Mamul Kod</th>
                <th>Mamul Ad</th>
                <th>Son Bakım Yapan</th>
                <th>Son Bakım Tarihi</th>
            </tr>
            <%
                }
            %>
            <tr id="tr<%=r.getId()%>">
                <%
                    String formatted = String.format ("%,d", r.getKalanBaski());
                    if(r.getKalanBaski() < Util.KALAN_BASKI_SAYISI) {
                        formatted = "<span style='font-weight:bold;color:#d43f3a;'>" + formatted + "</span>";
                    }
                %>
                <td nowrap align="right"><%= formatted + " {" + String.format ("%,d", mamulmap.get(r.getMamulid()).getBaskiSayisi()) + "}" %></td>
                <td nowrap align="right"><%= String.format ("%,d", r.getBaskiSayisi()) %></td>
                <td nowrap align="right"><%= String.format ("%,d", r.getUretimMiktar()) %></td>
                <td nowrap><%= mamulmap.get(r.getMamulid()) == null ? "" : mamulmap.get(r.getMamulid()).getKod() %></td>
                <td nowrap><%= mamulmap.get(r.getMamulid()) == null ? "" : "<form method='post' action='mamul'><input type='hidden' name='stoklisteid' value='1'><input type='hidden' value='"+r.getMamulid()+"' name='mamulidara'><input type='button' value='"+ mamulmap.get(r.getMamulid()).getAd()+"' class='mamullink' onclick='javascript:this.form.submit();'></form>" %></td>
                <td nowrap><%= calisanmap.get(r.getCalisanid()) == null ? "" : calisanmap.get(r.getCalisanid()).getFullName() %></td>
                <td nowrap><%= Util.getTarihTR(r.getBakimtarihi()) %></td>
            </tr>
            <% } %>
        </table>

        <%--For displaying Previous link except for the 1st page --%>
        <%--For displaying Page numbers.
        The when condition does not display a link for the current page--%>
        <%
            String param1 = (mamulid == null || mamulid.equals("")) ? "" : "&amp;mamulid=" + mamulid;
            String param2 = (calisanid == null || calisanid.equals("")) ? "" : "&amp;calisanid=" + calisanid;
            String param3 = (bas_tarih == null || bas_tarih.equals("")) ? "" : "&amp;bas_tarih=" + bas_tarih;
            String param4 = (bit_tarih == null || bit_tarih.equals("")) ? "" : "&amp;bit_tarih=" + bit_tarih;
        %>
        <jsp:include page="paging.jsp">
            <jsp:param value="noofpages" name="noofpages"/>
            <jsp:param value="currentpage" name="currentpage"/>
            <jsp:param value="bakimrapor" name="pagename"/>
            <jsp:param value="<%=param1 %>" name="param1"/>
            <jsp:param value="<%=param2 %>" name="param2"/>
            <jsp:param value="<%=param3 %>" name="param3"/>
            <jsp:param value="<%=param4 %>" name="param4"/>
        </jsp:include>
    </div>
    <%
        if (sayac > 0) {
    %>
    <div class="row text-right">
        <div>
            <form method="post" name="excelform" id="excelform" action="excelbakim">
                <button type="button" class="btn btn-danger" name="exceleaktar_bakimrapor"
                        id="exceleaktar_bakimrapor">Excele Aktar
                </button>
                <input type="hidden" value="0" name="excelegonder" id="excelegonder">
                <input type="hidden" name="excelbastarih" id="excelbastarih" value="<%=bas_tarih%>">
                <input type="hidden" name="excelbittarih" id="excelbittarih" value="<%=bit_tarih%>">
                <input type="hidden" name="excelmamulid" id="excelmamulid" value="<%=mamulid%>">
                <input type="hidden" name="excelcalisanid" id="excelcalisanid" value="<%=calisanid%>">
                <input type="hidden" name="excelsql" id="excelsql" value="<%=excelsql%>">
            </form>
        </div>
    </div>
    <%
        }
    %>


</div>

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/irfan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/rapor.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script>
    $(function () {
        $('#bas_tarih').datepicker({
            inline: true,
            format: 'dd-mm-yy',
            firstDay: 1,
        }).datepicker("option", "dateFormat", "dd-mm-yy").datepicker("setDate", "<%= bas_tarih %>");

        $('#bit_tarih').datepicker({
            inline: true,
            format: 'dd-mm-yy',
            firstDay: 1,
        }).datepicker("option", "dateFormat", "dd-mm-yy").datepicker("setDate", "<%= bit_tarih %>");

        $("#mamulkod").autocomplete({
            source: mamul2,
            minLength: 1,
            select: function (event, ui) {
                changefun(ui.item);
                $(this).val(ui.item.value);
            }
        });
        $("#mamulkod").change(function () {
            changefun(this);
        });

        if ($("#mamulid").val() != "") {
            let found_mamulid = mamul.filter(function (obj) {
                return obj.id == $("#mamulid").val();
            });

            if (found_mamulid != undefined) {
                //console.log(found_bilesenid);
                $("#mamulid").val(found_bilesenid[0].bilesenid);
                $("#mamulad").val(found_bilesenid[0].bilesenad);
                $("#mamulkod").val(found_bilesenid[0].bilesenkod);
            }
        }

    });

    function changefun(elem) {
        let found = false;
        if (elem != null) {
            for (i in mamul) {
                if (elem.value == mamul[i].bilesenkod) {
                    //console.log(mamul[i]);
                    found = true;
                    document.raporform.mamulid.value = mamul[i].bilesenid;
                    document.raporform.mamulad.value = mamul[i].bilesenad;
                    document.raporform.mamulkod.value = mamul[i].bilesenkod;
                }
            }
        }
        if (!found) {
            document.raporform.mamulid.value = "";
            document.raporform.mamulad.value = "";
            document.raporform.mamulkod.value = "";
        }
    }
</script>
</body>
</html>