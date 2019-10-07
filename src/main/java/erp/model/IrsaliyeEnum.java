package erp.model;

public enum IrsaliyeEnum {
    IRSALIYE_EKLE(0),
    IRSALIYE_BILESEN_GUNCELLE(1),
    IRSALIYE_KAPAT(2),
    IRSALIYE_SIL(3),
    IRSALIYE_ONAYLA(4),
    IRSALIYE_BILESEN_SIL(5),
    IRSALIYE_NO_KONTROL(6);

    private int value;

    IrsaliyeEnum(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }
}
