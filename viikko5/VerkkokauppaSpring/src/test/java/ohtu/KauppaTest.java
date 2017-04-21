package ohtu;

import ohtu.verkkokauppa.*;


import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class KauppaTest {

    Kauppa kauppa;
    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;

    @Before
    public void setUp() {
       this.pankki = mock(Pankki.class);
       this.viite = mock(Viitegeneraattori.class);
       this.varasto = mock(Varasto.class);
       this.kauppa = new Kauppa(varasto, pankki, viite);
   }

    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaan() {

        when(viite.uusi()).thenReturn(42);


        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));



        // tehdään ostokset
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        kauppa.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), anyInt());
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }

    @Test
    public void aloitaAsiointiLisaaTuoteTilisiirtoKutsutaan() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");

        verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), anyString(), eq(5));
    }


    @Test
    public void kaksiSamaaTuotettaTilisiirtoOikeatTiedot() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pakka", "112");

        verify(pankki).tilisiirto(eq("pakka"), eq(42), eq("112"), anyString(), eq(10));
    }

    @Test
    public void kaksiEriTuotettaTilisiirtoOikeatTiedot() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(20);
        when(varasto.saldo(2)).thenReturn(20);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "veri", 10));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Bela Lugosi", "1882");

        verify(pankki).tilisiirto(eq("Bela Lugosi"), eq(42), eq("1882"), anyString(), eq(15));
    }

    @Test
    public void yhtaTuotettaTarpeeksiJaYksiLoppu() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(2);
        when(varasto.saldo(2)).thenReturn(0);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "veri", 10));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Bela Lugosi", "1882");

        verify(pankki).tilisiirto(eq("Bela Lugosi"), eq(42), eq("1882"), anyString(), eq(5));

    }

    @Test
    public void aloitaAsiointiNollaaOstoksenTiedot() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(3);
        when(varasto.saldo(2)).thenReturn(3);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "veri", 10));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("Bela Lugosi", "1882");

        verify(pankki).tilisiirto(eq("Bela Lugosi"), eq(42), eq("1882"), anyString(), eq(5));
    }

    @Test
    public void uusiViiteJokaAsioinnille() {
        when(viite.uusi()).thenReturn(42).thenReturn(1000);
        when(varasto.saldo(1)).thenReturn(2);
        when(varasto.saldo(2)).thenReturn(2);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "veri", 10));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("Bela Lugosi", "1882");

        verify(pankki).tilisiirto(eq("Bela Lugosi"), eq(42), eq("1882"), anyString(), eq(5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("Lela Bugosi", "1956");

        verify(pankki).tilisiirto(eq("Lela Bugosi"), eq(1000), eq("1956"), anyString(), eq(15));

    }

    @Test
    public void koristaPoistettuTuoteEiMaksa() {
        when(viite.uusi()).thenReturn(42);
        when(varasto.saldo(1)).thenReturn(3);

        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "alennusveri", 5));

        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        kauppa.tilimaksu("Bela Lugosi", "1882");

        verify(pankki).tilisiirto(eq("Bela Lugosi"), eq(42), eq("1882"), anyString(), eq(5));


    }

}
