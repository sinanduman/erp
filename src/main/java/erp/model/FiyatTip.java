package erp.model;

public enum FiyatTip {
    TL(1,"TL", "₺"),
    USD(2,"USD", "$"),
    EURO(3,"EURO", "€");
    private Integer value;
    private String ad;
    private String kod;

    FiyatTip(Integer value, String ad, String kod) {
        this.value = value;
        this.ad = ad;
        this.kod = kod;
    }
    
    public Integer value() {
        return value;
    }
    public String ad() {
        return ad;
    }
    public String kod() {
        return kod;
    }
}
