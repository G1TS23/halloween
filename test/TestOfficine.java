import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.atomic.AtomicInteger;

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
    void testQuantiteIngredientInconnu() {
        assertThrows(IllegalArgumentException.class, () -> officine.quantite("Poudre de perlimpinpin"));
    }

    @Test
    void testRentrerProduitInexistant() {
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("3 oeufs de caille"));
    }

    @Test
    void testRentrer() {
        assertDoesNotThrow(() -> officine.rentrer("3 yeux de grenouille"));

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
        assertDoesNotThrow(() -> officine.rentrer("1 larme de brume funèbre"));
        assertThrows(IllegalArgumentException.class, () -> officine.rentrer("-1 larme de brume funèbre"));

        assertEquals(1, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testQuantiteSingulierPluriel() {
        assertDoesNotThrow(() -> officine.rentrer("5 larmes de brume funèbre"));

        assertEquals(5, officine.quantite("larme de brume funèbre"));
        assertEquals(5, officine.quantite("larmes de brume funèbre"));
    }

    @Test
    void testPreparer() {
        assertDoesNotThrow(() -> officine.rentrer("4 larmes de brume funèbre"));
        assertDoesNotThrow(() -> officine.rentrer("2 gouttes de sang de citrouille"));

        int prepared = assertDoesNotThrow(() -> officine.preparer("2 fioles de glaires purulentes"));

        assertEquals(2, prepared);
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
        assertEquals(2, officine.quantite("fiole de glaires purulentes"));
    }

    @Test
    void testPreparerSansIngredientsDisponibles() {
        int prepared = assertDoesNotThrow(() -> officine.preparer("2 fioles de glaires purulentes"));

        assertEquals(0, prepared);
    }

    @Test
    void testPreparerSansQuantite() {
        assertDoesNotThrow(() -> officine.rentrer("4 larmes de brume funèbre"));
        assertDoesNotThrow(() -> officine.rentrer("2 gouttes de sang de citrouille"));
        assertThrows(IllegalArgumentException.class, () ->
                officine.preparer("fioles de glaires purulentes"));

        assertEquals(4, officine.quantite("larme de brume funèbre"));
        assertEquals(2, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void testPreparerSansNomDeRecette() {
        assertThrows(IllegalArgumentException.class, () ->
            officine.preparer("2"));
    }

    @Test
    void testPreparerVide() {
        assertThrows(IllegalArgumentException.class, () -> officine.preparer(""));
    }

    @Test
    void testPreparerPotionInconnue() {
        assertThrows(IllegalArgumentException.class, () -> officine.preparer("2 goutte de sang de citrouille"));
    }

    @Test
    void testPreparerDeuxPotionsAvecSeulementLesStocksDUneSeule() {
        assertDoesNotThrow(() -> officine.rentrer("2 larmes de brume funèbre"));
        assertDoesNotThrow(() -> officine.rentrer("1 goutte de sang de citrouille"));

        int prepared = assertDoesNotThrow(() -> officine.preparer("2 fioles de glaires purulentes"));

        assertEquals(1, prepared);
    }

    @Test
    void testStockApresFabricationPartielle() {
        assertDoesNotThrow(() -> officine.rentrer("2 larmes de brume funèbre"));
        assertDoesNotThrow(() -> officine.rentrer("1 goutte de sang de citrouille"));

        int prepared = assertDoesNotThrow(() -> officine.preparer("5 fioles de glaires purulentes"));

        assertEquals(1, prepared);
        assertEquals(0, officine.quantite("larme de brume funèbre"));
        assertEquals(0, officine.quantite("goutte de sang de citrouille"));
    }

    @Test
    void testPreparerPotionComplexe() {

        assertDoesNotThrow(() -> officine.rentrer("2 larmes de brume funèbre"));
        assertDoesNotThrow(() -> officine.rentrer("1 goutte de sang de citrouille"));
        assertDoesNotThrow(() -> officine.preparer("1 fiole de glaires purulentes"));

        assertDoesNotThrow(() -> officine.rentrer("3 radicelles de racine hurlante"));

        int resultat = assertDoesNotThrow(() -> officine.preparer("1 baton de pâte sépulcrale"));

        assertEquals(1, resultat);
        assertEquals(0, officine.quantite("fiole de glaires purulentes"), "La potion intermédiaire aurait dû être consommée");
        assertEquals(1, officine.quantite("baton de pâte sépulcrale"));
    }
}