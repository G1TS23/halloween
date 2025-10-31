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
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("3 oeufs de caille"));
    }

    @Test
    void testRentrer() {
        officine.rentrer("3 yeux de grenouille");

        assertEquals(3, officine.quantite("œil de grenouille"));
    }

    @Test
    void testEntrerSansQuantite() {
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("larmes de brume funèbre"));
    }

    @Test
    void testEntrerSansIngredient() {
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("1"));
    }

    @Test
    void testEntrerSansRien() {
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer(""));
    }

    @Test
    void testEntrerQuantiteNegativeAvecStockZero() {
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("-1 larme de brume funèbre"));
        assertEquals(0, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testEntrerQuantiteNegativeAvecStockPosistif() {
        officine.rentrer("1 larme de brume funèbre");
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("-1 larme de brume funèbre"));

        assertEquals(1, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testQuantiteSingulierPluriel() {
        officine.rentrer("5 larmes de brume funèbre");

        assertEquals(5, officine.quantite("larme de brume funèbre"));
        assertEquals(5, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testPreparer() {
        officine.rentrer("4 larmes de brume funèbre");
        officine.rentrer("2 gouttes de sang de citrouille");
        int prepared = officine.preparer("2 fioles de glaires purulentes");

        assertEquals(2, prepared);
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
        assertEquals(2, officine.quantite("fiole de glaires purulentes"));
    }

    @Test
    void testPreparerSansIngredientsDisponibles() {
        int prepared = officine.preparer("2 fioles de glaires purulentes");

        assertEquals(0, prepared);
    }

    @Test
    void testPreparerSansQuantite() {
        officine.rentrer("4 larmes de brume funèbre");
        officine.rentrer("2 gouttes de sang de citrouille");
        int prepared = officine.preparer("fioles de glaires purulentes");

        assertEquals(0, prepared);
        assertEquals(4, officine.quantite("larme de brume funèbre"));
        assertEquals(2, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void testPreparerSansNomDeRecette() {
        int prepared = officine.preparer("2");

        assertEquals(0, prepared);
    }

    @Test
    void testPreparerVide() {
        int prepared = officine.preparer("");

        assertEquals(0, prepared);
    }

    @Test
    void testPreparerPotionInconnue() {
        int prepared = officine.preparer("2 goutte de sang de citrouille");

        assertEquals(0, prepared);
    }

    @Test
    void testPreparerDeuxPotionsAvecSeulementLesStocksDUneSeule() {
        officine.rentrer("2 larmes de brume funèbre");
        officine.rentrer("1 goutte de sang de citrouille");
        int prepared = officine.preparer("2 fioles de glaires purulentes");

        assertEquals(1, prepared);
    }
}