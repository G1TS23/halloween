import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class TestOfficine {
    private Officine officine;

    @BeforeEach
    void setUp() {
        officine = new Officine();
    }

    @Test
    void initOfficine() {
        assertEquals(0, officine.quantite("œil de grenouille"));
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("croc de troll"));
        assertEquals(0, officine.quantite("bille d'âme évanescente"));
        assertEquals(0, officine.quantite("batons de pâte sépulcrale"));
    }

    @Test
    void testRentrerProduitInexistant() {
        boolean result = officine.rentrer("3 oeufs de caille");
        assertFalse(result);
    }

    @Test
    void testRentrer() {
        boolean result = officine.rentrer("3 yeux de grenouille");
        assertTrue(result);
        assertEquals(3, officine.quantite("œil de grenouille"));
    }

    @Test
    void testQuantiteSingulierPluriel() {
        boolean result = officine.rentrer("5 larmes de brume funèbre");
        assertTrue(result);
        assertEquals(5, officine.quantite("larme de brume funèbre"));
        assertEquals(5, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testPreparer() {
        boolean result01 = officine.rentrer("4 larmes de brume funèbre");
        boolean result02 = officine.rentrer("2 gouttes de sang de citrouille");
        int prepared = officine.preparer("2 fioles de glaires purulentes");
        assertTrue(result01);
        assertTrue(result02);
        assertEquals(2, prepared);
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
        assertEquals(2, officine.quantite("fiole de glaires purulentes"));
    }

    @Test
    void testPreparerSansIngredients() {
        int prepared = officine.preparer("2 fioles de glaires purulentes");
        assertEquals(0, prepared);
    }

    @Test
    void testPreparerPotionInconnue() {
        int prepared = officine.preparer("2 goutte de sang de citrouille");
        assertEquals(0, prepared);
    }

    @Test
    void testPreparerDeuxPotionsAvecSeulementLesStocksDUneSeule() {
        boolean result01 = officine.rentrer("2 larmes de brume funèbre");
        boolean result02 = officine.rentrer("1 gouttes de sang de citrouille");
        int prepared = officine.preparer("2 fioles de glaires purulentes");
        assertTrue(result01);
        assertTrue(result02);
        assertEquals(1, prepared);
    }
}