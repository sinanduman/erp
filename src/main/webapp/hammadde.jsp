<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="erp.entity.Bilesen" %>
<%@ page import="erp.entity.Firma" %>
<%@ page import="erp.entity.Genel" %>
<%@ page import="erp.model.BilesenTip" %>
<%@ page import="erp.model.BirimTip" %>
<%@ page import="erp.model.FiyatTip" %>
<%@ page import="erp.util.Util" %>
<%@ page import="java.util.List" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="img/favicon.ico">
    <title><%= Genel.TITLE %>
    </title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="css/bootstrap.css?<%=Util.nanoTime%>">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/irfan.css?<%=Util.nanoTime%>">
    <link rel="stylesheet" href="css/fonts.css">
    <link rel="stylesheet" href="css/all.css">
    <link rel="stylesheet" href="css/jquery-ui-1.10.0.custom.css">
    <link rel="stylesheet/less" type="text/css" href="less/datepicker.less">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="assets/js/html5shiv.js"></script>
    <script src="assets/js/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<%@ include file="logincheck.jsp" %>

<%
    List<Firma> firma = (List<Firma>) request.getAttribute("firma");
    List<Bilesen> hammadde = (List<Bilesen>) request.getAttribute("hammadde");
    List<Bilesen> hammaddetum = (List<Bilesen>) request.getAttribute("hammaddetum");
    Long token = (Long) request.getAttribute("token");
    String message = (String) request.getAttribute("message");
    String admin = (String) session.getAttribute("admin");

    Integer totalrecord = (Integer) request.getAttribute("totalrecord");
    Integer currentpage = (Integer) request.getAttribute("currentpage");
    Integer noofpages = (Integer) request.getAttribute("noofpages");
    String bilesenid = (String) request.getAttribute("bilesenid");
    String stoklisteid = (String) request.getAttribute("stoklisteid");
%>

<script type="text/javascript">
    var bilesen = [
        <%String delimeter = "";
        for (Bilesen b : hammaddetum) {
            out.println(delimeter
        + " { id:" + b.getId()
        + ",bilesenid:" + b.getId()
        + ",bilesenad:'" + b.getAd().replaceAll("'", "") + "'"
        + ",bilesenkod:'" + b.getKod() + "'"
        + ",bilesentipid:" + b.getBilesentipid()
        + ",bilesentipad:'" + b.getBilesentipad() + "'"
        + ",firmaid:" + b.getFirmaid()
        + ",firmaad:'" + b.getFirmaad().replaceAll("'", "") + "'" + "}");
            delimeter = ",";
        }%>];
    var bilesen2 = [
        <%delimeter = "";
        for (Bilesen b : hammaddetum) {
            out.println(delimeter + " { label:'" + b.getKod() + " ["+b.getAd().replaceAll("'", "") +"]',id:" + b.getId() + "}");
            delimeter = ",";
        }%>];

</script>
<div class="container">
    <form class="form-horizontal" name="hammadde_form" id="hammadde_form" method="post" action="hammadde">
        <div class="row text-warning" style="text-align:center;">
            <div class="col-xs-10">
                <label class="text-danger rounded">Hammadde ve Yarı Mamül Tanımı</label>
            </div>
        </div>

        <div class="row">
            <div class="col-xs-10">
                <div class="form-group">
                    <label for="hamkod" class="col-xs-3 control-label text-baslik">Malzeme Kodu: </label>
                    <div>
                        <input type="text" class="form-control big" name="hamkod" id="hamkod" placeholder="Malzeme Kodu" autocomplete="off">
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamad" class="col-xs-3 control-label text-baslik">Malzeme Adı: </label>
                    <div>
                        <input type="text" class="form-control big" name="hamad" id="hamad" placeholder="Malzeme Adı" autocomplete="off">
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamtip" class="col-xs-3 control-label text-baslik">Malzeme Tipi: </label>
                    <div>
                        <select class="form-control big" name="hamtip" id="hamtip">
                            <%
                                for (BilesenTip bt : BilesenTip.values()) {
                                    if (BilesenTip.MAMUL.value() != bt.value()) {
                                        if (bt.value() != BilesenTip.HAMMADDEYARIMAMUL.value()) {
                                            out.println("<option value='" + bt.value() + "'>" + bt.ad() + "</option>");
                                        }
                                    }
                                }
                            %>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="hambirim" class="col-xs-3 control-label text-baslik">Birim: </label>
                    <div>
                        <select class="form-control big" name="hambirim" id="hambirim">
                            <%
                                for (BirimTip bt : BirimTip.values()) {
                                    if (BirimTip.KILOGRAM.value() != bt.value()) {
                                        out.println("<option value='" + bt.value() + "'>" + bt.ad() + "</option>");
                                    }
                                }
                            %>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamfirma" class="col-xs-3 control-label text-baslik">Tedarikçi: </label>
                    <div>
                        <select class="form-control big" name="hamfirma" id="hamfirma">
                            <%
                                for (Firma f : firma) {
                                    out.println("<option value='" + f.getId() + "'>" + f.getAd() + "</option>");
                                }
                            %>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamfiyatid" class="col-xs-3 control-label text-baslik">Para Tipi: </label>
                    <div>
                        <select class="form-control big" name="hamfiyatid" id="hamfiyatid">
                        <%
                            for (FiyatTip ft : FiyatTip.values()) {
                                out.println("<option value='" + ft.value() + "'>" + ft.ad() + " ("+ ft.kod() +")</option>");
                            }
                        %>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamfiyat" class="col-xs-3 control-label text-baslik">Birim Fiyat: </label>
                    <div>
                        <input type="number" class="form-control big" name="hamfiyat" id="hamfiyat" placeholder="Birim Fiyat" autocomplete="off">
                    </div>
                </div>

                <div class="form-group">
                    <label for="hamkritikesik" class="col-xs-3 control-label text-baslik">Minimum Stok: </label>
                    <div>
                        <input type="number" class="form-control big" name="hamkritikesik" id="hamkritikesik" placeholder="Minimum Stok" autocomplete="off">
                    </div>
                </div>

                <%
                    if (admin != null && admin.equals("1")) {
                %>
                <div class="row" sytle="margin-bottom:5px;">
                    <div class="form-group col-xs-4 text-right">
                        <button type="button" class="btn btn-danger" onclick="hammaddeEkle()">Ekle</button>
                        <input type="hidden" name="token" id="token" value="<%= token %>">
                    </div>

                    <div class="form-group col-xs-7 text-right">
                        <input type="text" class="form-control big margin-right-10px" name="bilesenkodara"
                               id="bilesenkodara"
                               placeholder="Malzeme Kodu" autocomplete="off">
                        <input type="hidden" name="bilesenidara" id="bilesenidara" value="<%=bilesenid%>">
                    </div>

                    <div class="form-group col-xs-1">
                        <button type="button" class="btn btn-danger" name="stokliste" id="stokliste" disabled="disabled">Malzeme Getir</button>
                        <input type="hidden" name="stoklisteid" id="stoklisteid" value="0">
                    </div>

                </div>

                <% } %>
            </div>
        </div>
    </form>

    <!-- ALERT BOX -->
    <%
        if (message.equals("-1")) {
    %>
    <%
        } else if (message.equals("0")) {
    %>
    <div class="row alert alert-success">
        <strong>Başarılı</strong> olarak eklendi!
    </div>
    <%
        } else if (!message.equals("") && message.length() > 1) {
    %>
    <div class="row alert alert-danger">
        <strong>Hata! </strong><%= message %>
    </div>
    <%
        }
    %>
    <!-- ALERT BOX -->

    <div class="row" style="font-size:12px;">
        <div>
            <table class="tableplan">
                <% int sayac = 0; %>
                <% for (Bilesen h : hammadde) { %>
                <% if (sayac++ == 0) { %>
                <tr>
                    <td><label class="text-success">Malzeme Kodu</label></td>
                    <td><label class="text-success">Malzeme Adı</label></td>
                    <td nowrap><label class="text-success">Malzeme Tipi</label></td>
                    <td><label class="text-success">Birim</label></td>
                    <td><label class="text-success">Tedarikçi</label></td>
                    <td nowrap><label class="text-success">Para Tipi</label></td>
                    <td nowrap><label class="text-success">Birim Fiyat</label></td>
                    <td nowrap><label class="text-success">Min.Stok</label></td>
                    <td><label class="text-success">Aktif</label></td>
                    <%
                        if (admin != null && admin.equals("1")) {
                    %>
                    <td><label class="text-success">Aksiyon</label></td>
                    <%  } %>
                </tr>
                <% } %>
                <form name=action_form<%= h.getId()%> id="action_form<%= h.getId()%>">
                    <tr id="tr<%=h.getId()%>" <%= (h.getDurum()==0 ? "style='background-color:darkKhaki;'" : "") %>>
                        <td><input type="text" class="form-control small" value="<%= h.getKod() %>" id="liste_kod<%=h.getId()%>" autocomplete="off"></td>
                        <td><input type="text" class="form-control" value="<%= h.getAd().replaceAll("\"", "") %>" id="liste_ad<%=h.getId()%>" autocomplete="off"></td>
                        <td style="vertical-align: center;"><span><%= h.getBilesentipad() %></span><input type="hidden" id="liste_bilesentipid<%=h.getId()%>" value="<%=h.getBilesentipid()%>"/></td>
                        <td>
                            <select class="form-control xs" name="liste_birimid" id="liste_birimid">
                                <%
                                    for (BirimTip bt : BirimTip.values()) {
                                        if (bt.value() == h.getBirimid()) {
                                            out.println("<option value='" + bt.value() + "' selected>" + bt.ad() + "</option>");
                                        } else {
                                            if (bt.value() != BirimTip.KILOGRAM.value()) {
                                                out.println("<option value='" + bt.value() + "'>" + bt.ad() + "</option>");
                                            }
                                        }
                                    }
                                %>
                            </select>
                            <%-- h.getBirimad() --%>
                        </td>
                        <td>
                            <select class="form-control" name="liste_firmaid" id="liste_firmaid">
                                <%
                                    for (Firma f : firma) {
                                        if (f.getId() == h.getFirmaid()) {
                                            out.println("<option value='" + f.getId() + "' selected>" + f.getAd() + "</option>");
                                        } else {
                                            out.println("<option value='" + f.getId() + "'>" + f.getAd() + "</option>");
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td>
                            <select class="form-control xs" name="fiyatid" id="fiyatid">
                                <%
                                    for (FiyatTip ft : FiyatTip.values()) {
                                        if (ft.value() == h.getFiyatId()) {
                                            out.println("<option value='" + ft.value() + "' selected>" + ft.ad() + " ("+ ft.kod() +")</option>");
                                        } else {
                                            out.println("<option value='" + ft.value() + "'>" + ft.ad() + " ("+ ft.kod() +")</option>");
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td><input type="number" class="form-control xss" value="<%= h.getFiyat() %>" id="fiyat" autocomplete="off"></td>
                        <td><input type="number" class="form-control xss" value="<%= h.getKritikesik() %>" id="kritikesik" autocomplete="off"></td>
                        <td>
                            <select class="form-control xss" name="liste_bilesendurum" id="liste_bilesendurum">
                                <%
                                    if (h.getDurum() == 1) {
                                        out.println("<option value='" + 1 + "' selected>Aktif</option>");
                                        out.println("<option value='" + 0 + "'>Pasif</option>");
                                    } else {
                                        out.println("<option value='" + 0 + "' selected>Pasif</option>");
                                        out.println("<option value='" + 1 + "'>Aktif</option>");
                                    }
                                %>
                            </select>
                        </td>
                        <%
                            if (admin != null && admin.equals("1")) {
                        %>
                        <td>
                            <div id="div<%= h.getId() %>" class="text-center">
                                <a href='javascript:updateHammaddeGo("hammadde", "<%=h.getId()%>", document.action_form<%=h.getId()%>,1);' title="Güncelle"><span class="fa fa-sync fa-lg text-warning"></span></a>
                                <a href='javascript:deleteHammaddeGo("hammadde", "<%=h.getId()%>", document.action_form<%=h.getId()%>,3);' title="Sil"><span class="fa fa-minus-circle fa-lg text-danger"></span></a>
                            </div>
                        </td>
                        <%  } %>
                    </tr>
                </form>
                <% } %>
            </table>
            <%--For displaying Previous link except for the 1st page --%>
            <%--For displaying Page numbers.
            The when condition does not display a link for the current page--%>
            <jsp:include page="paging.jsp" flush="true">
                <jsp:param value="noofpages" name="noofpages"/>
                <jsp:param value="currentpage" name="currentpage"/>
                <jsp:param value="hammadde" name="pagename"/>
            </jsp:include>

        </div>
        <div class="col-sm-1"></div>
    </div><!-- row -->
</div><!-- container -->

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.min.js" type="text/javascript"></script>
<script src="js/angular.min.js"></script>
<script src="js/angular-route.min.js"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/irfan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/hammadde.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/hammadde-helper.js?<%=Util.nanoTime%>" type="text/javascript"></script>

</body>
</html>