<%@ page import="erp.entity.*" %>
<%@ page import="erp.util.Util" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
    List<Mamul> mamul = (List<Mamul>) request.getAttribute("mamul");
    List<Bilesen> bilesen = (List<Bilesen>) request.getAttribute("bilesen");
    List<Makina> makina = (List<Makina>) request.getAttribute("makina");
    List<Calisan> calisan = (List<Calisan>) request.getAttribute("calisan");
    List<HataSebep> hatasebep = (List<HataSebep>) request.getAttribute("hatasebep");
    List<DurusSebep> durussebep = (List<DurusSebep>) request.getAttribute("durussebep");
    List<UretimPlan> uretimplan = (List<UretimPlan>) request.getAttribute("uretimplan");
    String admin = (String) session.getAttribute("admin");
%>

<script type="text/javascript">
    var bilesen = [
        <%String delimeter = "";
        for (Bilesen b : bilesen) {
            out.println(delimeter
        + " { id:" + b.getId()
        + ",bilesenid:" + b.getId()
        + ",bilesenad:'" + b.getAd().replaceAll("'", "") + "'"
        + ",bilesenkod:'" + b.getKod() + "'"
        + ",bilesentipid:" + b.getBilesentipid()
        + ",bilesentipad:'" + b.getBilesentipad() + "'" + "}");
            delimeter = ",";
        }%>];
    var mamul = [
        <%delimeter = "";
        for (Mamul m : mamul) {
            out.println(delimeter
        + " { id:" + m.getId()
        + ",mamulid:" + m.getId()
        + ",mamulad:'" + m.getAd().replaceAll("'", "") + "'"
        + ",mamulkod:'" + m.getKod() + "'"
        + ",esmamulid:" + m.getEsmamulid()
        + ",esmamulad:'" + m.getEsmamulad().replaceAll("'", "") + "'"
        + ",esmamulkod:'" + m.getEsmamulkod() + "'"
        + ",hammadde:'" + m.getHammadde().replaceAll("'", "") + "'"
        + ",hammaddegkrno:'" + m.getHammaddegkrno() + "'"
        + ",hammaddeagir:'" + m.getHammaddemiktar() + "'"
        + ",hammaddemamulbasi:'" + m.getHammaddemamulbasi() + "'"
        + ",yarimamul:'" + m.getYarimamul().replaceAll("'", "") + "'"
        + ",yarimamulgkrno:'" + m.getYarimamulgkrno() + "'"
        + ",yarimamulagir:'" + m.getYarimamulmiktar() + "'"
        + ",yarimamulmamulbasi:'" + m.getYarimamulmamulbasi() + "'"
        + ",firmaad:'" + m.getFirmaad().replaceAll("'", "") + "'"
        + ",figur:" + m.getFigur()
        + ",cevrimsure:" + m.getCevrimsuresi() + "}");
            delimeter = ",";
        }%>];
    var mamul2 = [
        <%delimeter = "";
        for (Mamul m : mamul) {
            // out.println(delimeter + " { label:'" + m.getKod() + "'" + ",id:" + m.getId() + "}");
            out.println(delimeter + " { value:'"+ m.getKod() + "', label:'" + m.getKod() + " ["+m.getAd().replaceAll("'", "") +"]',id:" + m.getId() + "}");
            delimeter = ",";
        }%>];

</script>


<div class="container">
    <div class="row text-warning" style="text-align: center;">
        <label class="text-danger rounded">Üretim Takibi</label>
    </div>
    <div class="row">
        <form class="form-inline" name="uretimtakipform" id="uretimtakipform" method="post" action="uretimtakip">
            <div class="form-group">
                <label for="tarih" class="text-baslik">Tarih</label>
                <div>
                    <input type="text" class="form-control xm" name="tarih" id="tarih" placeholder="Tarih">
                </div>
            </div>

            <div class="form-group">
                <label for="bassaat" class="text-baslik">Başlangıç</label>
                <div>
                    <div>
                        <select class="form-control time" name="bassaat" id="bassaat">
                            <%
                                for (int i = 0; i < 24; i++) {
                                    String temp = "0" + i;
                                    temp = temp.substring(temp.length() - 2);
                                    out.print("<option value='" + temp + "'>");
                                    out.print((temp));
                                    out.println("</option>");
                                }
                            %>
                        </select><strong>:</strong><select class="form-control time" name="basdakika" id="basdakika">
                        <%
                            for (int i = 0; i < 60; i = i + 5) {
                                String temp = "0" + i;
                                temp = temp.substring(temp.length() - 2);
                                out.print("<option value='" + temp + "'>");
                                out.print((temp));
                                out.println("</option>");
                            }
                        %>
                    </select>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="bitsaat" class="text-baslik">Bitiş</label>
                <div>
                    <div>
                        <select class="form-control time" name="bitsaat" id="bitsaat">
                            <%
                                for (int i = 0; i < 24; i++) {
                                    String temp = "0" + i;
                                    temp = temp.substring(temp.length() - 2);
                                    out.print("<option value='" + temp + "'>");
                                    out.print((temp));
                                    out.println("</option>");
                                }
                            %>
                        </select><strong>:</strong><select class="form-control time" name="bitdakika" id="bitdakika">
                        <%
                            for (int i = 0; i < 60; i = i + 5) {
                                String temp = "0" + i;
                                temp = temp.substring(temp.length() - 2);
                                out.print("<option value='" + temp + "'>");
                                out.print((temp));
                                out.println("</option>");
                            }
                        %>
                    </select>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label for="makinaid" class="text-baslik">Makina/Band</label>
                <div>
                    <select class="form-control" id="makinaid" name="makinaid">
                        <%
                            for (Makina m : makina) {
                                out.println("<option value='" + m.getId() + "'>" + m.getAd() + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="calisanid" class="text-baslik">Operatör</label>
                <div>
                    <select class="form-control" id="calisanid" name="calisanid">
                        <%
                            for (Calisan c : calisan) {
                                out.println("<option value='" + c.getId() + "'>" + c.getFullName() + "</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group" id="div_mamulizno" >
                <label for="mamulizleno" class="text-baslik">Mamül İzl.No</label>
                <div>
                    <select class="form-control normal" name="mamulizleno" id="mamulizleno"></select>
                </div>
            </div>

            <div class="clearfix"></div>

            <div class="form-group">
                <label for="mamulkod" class="text-baslik">Mamül Kodu</label>
                <div>
                    <input type="text" class="form-control small" name="mamulkod" id="mamulkod" placeholder="Mamül Kodu"
                           autocomplete="off">
                    <input type="hidden" name="mamulid" id="mamulid">
                    <input type="hidden" name="yarimamulgkrno" id="yarimamulgkrno">
                    <input type="hidden" name="hammaddegkrno" id="hammaddegkrno">
                </div>
            </div>

            <div class="form-group">
                <label for="mamulad" class="text-baslik">Mamül Adı</label>
                <div>
                    <input type="text" class="form-control small" name="mamulad" id="mamulad" readonly="readonly">
                </div>
            </div>

            <div class="form-group" id="div_hammadde">
                <label for="hammadde" class="text-baslik">Hammadde / Kalan Miktar / Mamül Başı</label>
                <div>
                    <select class="form-control big" name="hammadde" id="hammadde" readonly="readonly"></select>
                </div>
            </div>

            <div class="form-group" id="div_yarimamul">
                <label for="yarimamul" class="text-baslik">Yarımamül / Kalan Miktar / Mamül Başı</label>
                <div>
                    <select class="form-control big" name="yarimamul" id="yarimamul" readonly="readonly"></select>
                </div>
            </div>
            <%-- EŞ MAMÜL TANIMLARI --%>
            <div class="form-group" id="div_esmamul" style="display: none;">
                <label for="cxb_esmamul" class="text-baslik" id="lbl_esmamul">Eş Mamül</label>
                <div style="height: 32px;">
                    <input type="checkbox" name="cxb_esmamul" id="cxb_esmamul" aria-label="Eş Mamül de üretilsin mi?">
                    <span id="esmamulad"></span>
                    <input type="hidden" name="esmamulid" id="esmamulid" value="">
                    <input type="hidden" name="esmamulid_shadow" id="esmamulid_shadow" value="">
                </div>
            </div>
            <div class="form-group" id="div_esmamulizno" style="display: none;">
                <label for="esmamulizleno" class="text-baslik">Eş Mamül İzl.No</label>
                <div>
                    <select class="form-control normal" name="esmamulizleno" id="esmamulizleno"></select>
                </div>
            </div>
            <%-- EŞ MAMÜL TANIMLARI --%>

            <div class="clearfix"></div>

            <div class="form-group">
                <label for="firmaad" class="text-baslik">Firma</label>
                <div>
                    <input type="text" class="form-control small" name="firmaad" id="firmaad" readonly="readonly">
                </div>
            </div>


            <div class="form-group">
                <label for="uretimadet" class="text-baslik">Üretim Ad.</label>
                <div>
                    <input type="number" class="form-control xs text-right" name="uretimadet" id="uretimadet" autocomplete="off">
                </div>
            </div>

            <div class="form-group">
                <label for="hataliadet" class="text-baslik">Hatalı Ad.</label>
                <div>
                    <input type="number" class="form-control xs text-right" name="hataliadet" id="hataliadet" autocomplete="off">
                </div>
            </div>

            <div class="form-group">
                <label for="hatasebepid" class="text-baslik">Hata Kodu</label>
                <div>
                    <select name="hatasebepid" id="hatasebepid" class="form-control small">
                        <%
                            out.println("<option value='0' selected>...Seçiniz...</option>");
                            for (HataSebep hs : hatasebep) {
                                out.println("<option value='" + hs.getId() + "'>" + hs.getAd() + " (" + hs.getKod() + ")</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-group">
                <label for="duruszaman" class="text-baslik">Duruş Zamanı</label>
                <div>
                    <input type="text" class="form-control xs text-right" type="number" name="duruszaman" id="duruszaman"><span class="birim">Dk.</span>
                </div>
            </div>


            <div class="form-group">
                <label for="durussebepid" class="text-baslik">Duruş Kodu</label>
                <div>
                    <select name="durussebepid" id="durussebepid" class="form-control small">
                        <%
                            out.println("<option value='0' selected>...Seçiniz...</option>");
                            for (DurusSebep ds : durussebep) {
                                out.println("<option value='" + ds.getId() + "'>" + ds.getAd() + " (" + ds.getKod() + ")</option>");
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="clearfix"></div>

            <%
                if (admin != null && (admin.equals("1") || (admin.equals("2")))) {
            %>
            <div class="form-group" style="margin:10px 10px 0 0;">
                <div>&nbsp;</div>
                <div>
                    <input type="hidden" name="uretimplanid" id="uretimplanid" value="0">
                    <input type="hidden" name="islemid" id="islemid" value="0">
                    <button type="button" class="btn btn-danger" name="uretimekle" id="uretimekle"> Ekle</button>
                    <button type="button" class="btn btn-danger" name="uretimiptal" id="uretimiptal" style="display:none;">İptal</button>
                </div>
            </div>
            <div class="form-group">
                <label for="cevrimsure" class="text-baslik">ÇS/Fg.</label>
                <div>
                    <input type="text" class="form-control xs" name="cevrimsure" id="cevrimsure" readonly="readonly">
                </div>
            </div>
            <div class="form-group">
                <label for="toplamsure" class="text-baslik">Toplam Süre</label>
                <div>
                    <input type="text" class="form-control xm" name="toplamsure" id="toplamsure" readonly="readonly">
                </div>
            </div>

            <div class="form-group">
                <label for="tahminiuretim" class="text-baslik">Tahmini Üretim</label>
                <div>
                    <input type="text" class="form-control xm" name="tahminiuretim" id="tahminiuretim" readonly="readonly"><span class="birim"> Adet</span>
                </div>
            </div>
            <div class="clearfix"></div>
            <%
                }
            %>

        </form>
    </div>
    <%
        int sayac = 0;
    %>

    <div class="row size12px" style="font-size:12px;">
        <table class="tableplan">
            <script type="text/javascript" charset="utf-8">
                let plan = {};
                let plantemp = {};
            </script>
            <%
                for (UretimPlan u : uretimplan) {
            %>
            <script type="text/javascript" charset="utf-8">
                plantemp = {
                    id: "<%=u.getId()%>",
                    tarihtr: "<%=u.getTarihTR()%>",
                    tarihtrshort: "<%=u.getTarihTRShort()%>",
                    baslangic: "<%=u.getBasZaman()%>",
                    bitis: "<%=u.getBitZaman()%>",
                    makinaad: "<%=u.getMakinaad()%>",
                    makinaid: "<%=u.getMakinaid()%>",
                    calisanad: "<%=u.getCalisanShortName()%>",
                    calisanid: "<%=u.getCalisanid()%>",
                    firmaad: "<%=u.getFirmaad()%>",
                    firmaid: "<%=u.getFirmaid()%>",
                    hammadde: "<%=u.getHammaddead()%>",
                    gkrno: "<%=u.getHammaddeizlno()%>",
                    mamulad: "<%=u.getMamulad()%>",
                    mamulid: "<%=u.getMamulid()%>",
                    mamulkod: "<%=u.getMamulkod()%>",
                    uretimadet: "<%=u.getUretilenmiktar()%>",
                    hataliadet: "<%=(u.getHatalimiktar()==0) ? "" : u.getHatalimiktar()%>",
                    hataid: "<%=u.getHataid()%>",
                    hatakod: "<%=u.getHatakodu()%>",
                    duruszaman: "<%=(u.getDuruszaman()==0)? "" : u.getDuruszaman()%>",
                    durusid: "<%=u.getDurusid()%>",
                    duruskod: "<%=u.getDuruskodu()%>",
                    izlemeno: "<%=u.getMamulizlno()%>"
                };
                plan[<%=u.getId()%>] = plantemp;
            </script>
            <%
                if (sayac++ == 0) {
            %>
            <tr>
                <th>Sıra</th>
                <th>Tarih</th>
                <th>Başl.</th>
                <th>Bit.</th>
                <th>Makina</th>
                <th>Operatör</th>
                <th>Müşteri</th>
                <th>Hammadde</th>
                <th>GKR.No</th>
                <th>Ürün Kodu</th>
                <th>Ürün Adı</th>
                <th>İzl.No</th>
                <th>Ür.Adet</th>
                <th>Hatalı</th>
                <th>Hata Kd.</th>
                <th>Duruş Zm.</th>
                <th>Duruş Kd.</th>
                <th>Aksiyon</th>
            </tr>
            <%
                }
            %>
            <tr id="tr<%=u.getId()%>">
                <td class="text-right"><%=u.getId()%></td>
                <td nowrap><%=u.getTarihTRShort()%></td>
                <td><%=u.getBasZaman()%></td>
                <td><%=u.getBitZaman()%></td>
                <td nowrap><%=u.getMakinaad()%></td>
                <td><%=u.getCalisanShortName()%></td>
                <td><%=u.getFirmaad()%></td>
                <td><%=Util.splitLine(u.getHammaddead(), ";", "<br>")%></td>
                <td><%=Util.splitLine(u.getHammaddeizlno(), ";", "<br>")%></td>
                <td><%= u.getMamulkod() %></td>
                <td><%= u.getMamulad() %></td>
                <td><%= u.getMamulizlno() %></td>
                <td class="text-right"><%= u.getUretilenmiktar() %></td>
                <td class="text-right"><%= (u.getHatalimiktar() == 0) ? "" : u.getHatalimiktar() %></td>
                <td><%= u.getHatakodu() %></td>
                <td><%= (u.getDuruszaman() == 0) ? "" : u.getDuruszaman() + " Dk."%></td>
                <td><%= u.getDuruskodu() %></td>
                <td nowrap>
                    <div id="div<%= u.getId() %>">
                        <a href="javascript:uretimtakipguncelle('#tr<%= u.getId() %>','<%= u.getId() %>');" title="Güncelle"><span class="fa fa-sync fa-lg text-warning"></span></a>
                        <a href="javascript:uretimtakipok(<%= u.getId() %>);" title="Onayla"><span class="fa fa-check-circle fa-lg text-success"></span></a>
                        <a href="javascript:uretimtakipsil(<%= u.getId() %>);" title="Sil"><span class="fa fa-minus-circle fa-lg text-danger"></span></a>
                    </div>
                </td>
            </tr>
            <% } %>
        </table>

        <jsp:include page="paging.jsp">
            <jsp:param value="noofpages" name="noofpages"/>
            <jsp:param value="currentpage" name="currentpage"/>
            <jsp:param value="uretimtakip" name="pagename"/>
        </jsp:include>

    </div>

    <div class="row" style="font-size:12px;margin-top:10px;">
        <label class="text-danger">Not:</label>
        <br>
        <span class="text-danger"><i class="fas fa-paperclip"></i></span> <span class="text-success">Aksiyon bölümünde "Onayla" simgesine tıklayınca, listeye yansıyan "Ür.Adet" miktarı, ürün stoğuna eklenir.</span>
        <br>
        <span class="text-danger"><i class="fas fa-paperclip"></i></span> <span class="text-success">Aksiyon bölümünde "Onayla" simgesine tıklayınca, listeye yansıyan "Ür.Adet" miktarına göre, hammadde ve yarımamül stoğundan düşülür.</span>
        <br>
    </div>
    <div id="okdiv" class="bb-alert alert alert-success" style="display:none;">
        <span>Confirm result: false</span>
    </div>
    <div id="errordiv" class="bb-alert alert alert-danger" style="display:none;">
        <span>Confirm result: false</span>
    </div>
</div>

<script src="js/jquery.min.js" type="text/javascript"></script>
<script src="js/jquery-ui.min.js" type="text/javascript"></script>
<script src="js/bootstrap.min.js" type="text/javascript"></script>
<script src="js/bootbox.js" type="text/javascript"></script>
<script src="js/moment.min.js" type="text/javascript"></script>
<script src="js/irfan.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script src="js/uretim-takip.js?<%=Util.nanoTime%>" type="text/javascript"></script>
<script type="text/javascript">
    $("#cxb_esmamul").on("change", function () {
        if ($(this).prop("checked")) {
            es_mamul_checkbox_on();
        }
        else{
            es_mamul_checkbox_off();
        }
    });

    //$("#tr"+planid).attr("disabled","disabled").css( "background-color", "darkkhaki");

    let globalMamul = new Object();
    $(function () {
        $('#tarih').datepicker({
            inline: true,
            format: 'dd-mm-yy',
            firstDay: 1,
        }).datepicker("option", "dateFormat", "dd-mm-yy").datepicker("setDate", "today");
    });
    $("#mamulkod").autocomplete({
        source: mamul2,
        minLength: 1,
        select: function (event, ui) {
            //$("#go").removeAttr("disabled");
            console.log(ui.item.id);
            console.log(ui.item.value);
            console.log(event);
            changefun_uretim(ui.item);
            $(this).val(ui.item.value);
        }
    });

    $("#mamulkod").on("change", function () {
        changefun_uretim(this);
    });
    $("#mamulkod").on("blur", function () {
        clearFields(this);
    });

    $("#bassaat, #basdakika, #bitsaat, #bitdakika").change(function () {
        if ($("#mamulid").val() != "") {
            dateDiff();
        }
    });

    function dateDiff() {
        let basFullDate = document.uretimtakipform.tarih.value + " " + document.uretimtakipform.bassaat.value + ":" + document.uretimtakipform.basdakika.value;
        let bitFullDate = document.uretimtakipform.tarih.value + " " + document.uretimtakipform.bitsaat.value + ":" + document.uretimtakipform.bitdakika.value;

        let basTime = document.uretimtakipform.bassaat.value + document.uretimtakipform.basdakika.value;
        let bitTime = document.uretimtakipform.bitsaat.value + document.uretimtakipform.bitdakika.value;

        let basDay = moment(basFullDate, "DD-MM-YYYY HH:mm");
        let bitDay = moment(bitFullDate, "DD-MM-YYYY HH:mm");

        if (bitTime <= basTime) {
            bitDay = bitDay.add(1, 'days');
        }

        let diffMinutes = bitDay.diff(basDay, 'minutes');

        let diff = diffMinutes; /* Difference in Minutes */

        let diffHour = Math.floor(diff / 60);
        let diffMin = diff - diffHour * 60;

        let diffHourStr = (diffHour == 0) ? "" : diffHour + " saat ";
        let diffMinStr = (diffMin == 0) ? "" : diffMin + " dakika";

        document.uretimtakipform.cevrimsure.value = globalMamul.cevrimsure + "/" + globalMamul.figur;
        document.uretimtakipform.toplamsure.value = diffHourStr + diffMinStr;
        document.uretimtakipform.tahminiuretim.value = Math.floor(diff * 60 / globalMamul.cevrimsure) * globalMamul.figur;
    }
</script>
</body>

</html>