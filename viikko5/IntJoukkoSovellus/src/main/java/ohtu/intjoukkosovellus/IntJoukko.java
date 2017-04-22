
package ohtu.intjoukkosovellus;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
                            OLETUSKASVATUS = 5;  // luotava uusi taulukko on
    // näin paljon isompi kuin vanha
    private int kasvatuskoko;     // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] lukujono;      // Joukon luvut säilytetään taulukon alkupäässä.
    private int alkioidenLkm;    // Tyhjässä joukossa alkioiden_määrä on nolla.

    public IntJoukko() {
        lukujono = new int[KAPASITEETTI];
        for (int i = 0; i < lukujono.length; i++) {
            lukujono[i] = 0;
        }
        this.kasvatuskoko = OLETUSKASVATUS;
    }

    public IntJoukko(int kapasiteetti) {
        if (kapasiteetti < 0) {
            return;
        }
        lukujono = new int[kapasiteetti];
        alustaLukujono();
        this.kasvatuskoko = OLETUSKASVATUS;

    }


    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) {
            throw new IndexOutOfBoundsException("Kapasitteetti väärin");//heitin vaan jotain :D
        }
        if (kasvatuskoko < 0) {
            throw new IndexOutOfBoundsException("kapasiteetti2");//heitin vaan jotain :D
        }
        lukujono = new int[KAPASITEETTI];
        alustaLukujono();
        this.kasvatuskoko = kasvatuskoko;

    }

    public void alustaLukujono() {
        for (int i = 0; i < lukujono.length ; i++) {
            lukujono[i] = 0;
        }
        alkioidenLkm = 0;
    }

    public boolean lisaa(int luku) {
        if (alkioidenLkm == 0) {
            lukujono[0] = luku;
            alkioidenLkm++;
            return true;
        } else {
        }
        if (!kuuluu(luku)) {
            lisaaUusiLuku(luku);
            return true;
        }
        return false;
    }

    private void lisaaUusiLuku(int luku) {
        lukujono[alkioidenLkm] = luku;
        alkioidenLkm++;
        if (alkioidenLkm % lukujono.length == 0) {
            int[] taulukkoOld = new int[lukujono.length];
            taulukkoOld = lukujono;
            kopioiTaulukko(lukujono, taulukkoOld);
            lukujono = new int[alkioidenLkm + kasvatuskoko];
            kopioiTaulukko(taulukkoOld, lukujono);
        }
    }

    public boolean kuuluu(int luku) {
        int on = 0;
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luku == lukujono[i]) {
                on++;
            }
        }
        if (on > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean poista(int luku) {
        int kohta = -1;
        int apu;
        for (int i = 0; i < alkioidenLkm; i++) {
            if (luku == lukujono[i]) {
                kohta = i; //siis luku löytyy tuosta kohdasta :D
                lukujono[kohta] = 0;
                break;
            }
        }
        if (kohta != -1) {
            poistaKohdasta(kohta, luku);
            return true;
        }


        return false;
    }

    private void poistaKohdasta(int kohta, int luku) {
        int apu;
        for (int j = kohta; j < alkioidenLkm - 1; j++) {
            apu = lukujono[j];
            lukujono[j] = lukujono[j + 1];
            lukujono[j + 1] = apu;
        }
        alkioidenLkm--;

    }

    private void kopioiTaulukko(int[] vanha, int[] uusi) {
        for (int i = 0; i < vanha.length; i++) {
            uusi[i] = vanha[i];
        }

    }

    public int mahtavuus() {
        return alkioidenLkm;
    }


    @Override
    public String toString() {
        if (alkioidenLkm == 0) {
            return "{}";
        } else if (alkioidenLkm == 1) {
            return "{" + lukujono[0] + "}";
        } else {
            return monenAlkionMerkkijono();
        }
    }

    public String monenAlkionMerkkijono() {
        String valmisMerkkijono = "{";
        for (int i = 0; i < alkioidenLkm - 1; i++) {
            valmisMerkkijono += lukujono[i];
            valmisMerkkijono += ", ";
        }
        valmisMerkkijono += lukujono[alkioidenLkm - 1];
        valmisMerkkijono += "}";
        return valmisMerkkijono;
    }

    public int[] toIntArray() {
        int[] taulu = new int[alkioidenLkm];
        for (int i = 0; i < taulu.length; i++) {
            taulu[i] = lukujono[i];
        }
        return taulu;
    }


    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) {
        IntJoukko x = new IntJoukko();
        int[] lisattava1 = a.toIntArray();
        int[] lisattava2 = b.toIntArray();
        x.lisaaKaikki(lisattava1);
        x.lisaaKaikki(lisattava2);
        return x;
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko leikkausjoukko = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        leikkausjoukko.leikkaaTaulut(aTaulu, bTaulu);
        return leikkausjoukko;
    }

    public void leikkaaTaulut(int[] taulu1, int[] taulu2) {
        for (int i = 0; i < taulu1.length; i++) {
            for (int j = 0; j < taulu2.length; j++) {
                if (taulu1[i] == taulu2[j]) {
                    this.lisaa(taulu2[j]);
                }
            }
        }
    }

    public static IntJoukko erotus ( IntJoukko a, IntJoukko b) {
        IntJoukko erotusjoukko = new IntJoukko();
        int[] lisattavaTaulu = a.toIntArray();
        int[] poistettavaTaulu = b.toIntArray();
        erotusjoukko.lisaaKaikki(lisattavaTaulu);
        erotusjoukko.poistaKaikki(poistettavaTaulu);

        return erotusjoukko;
    }

    public void lisaaKaikki( int[] lisattavaTaulu) {
        for (int i = 0; i<lisattavaTaulu.length; i++) {
            this.lisaa(lisattavaTaulu[i]);
        }
    }

    public void poistaKaikki(int[] poistettavaTaulu) {
        for (int i = 0; i<poistettavaTaulu.length; i++) {
            this.poista(poistettavaTaulu[i]);
        }
    }

}
