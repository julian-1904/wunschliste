package wunschliste.web;

public class MeineReservierungEintrag {

    private final Long listenId;
    private final String listenTitel;
    private final Long wunschId;
    private final String wunschTitel;
    private final double preis;

    public MeineReservierungEintrag(Long listenId,
                                    String listenTitel,
                                    Long wunschId,
                                    String wunschTitel,
                                    double preis) {
        this.listenId = listenId;
        this.listenTitel = listenTitel;
        this.wunschId = wunschId;
        this.wunschTitel = wunschTitel;
        this.preis = preis;
    }

    public Long getListenId() {
        return listenId;
    }

    public String getListenTitel() {
        return listenTitel;
    }

    public Long getWunschId() {
        return wunschId;
    }

    public String getWunschTitel() {
        return wunschTitel;
    }

    public double getPreis() {
        return preis;
    }
}