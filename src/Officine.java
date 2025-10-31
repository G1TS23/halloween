import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Officine {

    enum Item {
        // Ingrédients de base
        OEIL_GRENOUILLE("œil de grenouille", "yeux de grenouille"),
        LARME_BRUME("larme de brume funèbre", "larmes de brume funèbre"),
        RADICELLE_RACINE("radicelle de racine hurlante", "radicelles de racine hurlante"),
        POUDRE_LUNE("pincée de poudre de lune", "pincées de poudre de lune"),
        CROC_TROLL("croc de troll", "crocs de troll"),
        ECAILLE_DRAGONNET("fragment d'écaille de dragonnet", "fragments d'écaille de dragonnet"),
        SANG_CITROUILLE("goutte de sang de citrouille", "gouttes de sang de citrouille"),

        // Potions
        GLAIRES_PURULENTES("fiole de glaires purulentes", "fioles de glaires purulentes"),
        AME_EVANESCENTE("bille d'âme évanescente", "billes d'âme évanescente"),
        SELS_SUFFOCANTS("soupçon de sels suffocants", "soupçons de sels suffocants"),
        PATE_SEPULCRALE("baton de pâte sépulcrale", "batons de pâte sépulcrale"),
        ESSENCE_CAUCHEMAR("bouffée d'essence de cauchemar", "bouffées d'essence de cauchemar");

        private final String singulier;
        private final String pluriel;

        Item(String singulier, String pluriel) {
            this.singulier = singulier;
            this.pluriel = pluriel;
        }

        public String getSingulier() {
            return singulier;
        }

        public String getPluriel() {
            return pluriel;
        }

        public static Item fromNom(String nom) {
            String nomNormalise = nom.trim().toLowerCase();
            for (Item item : values()) {
                if (item.singulier.equals(nomNormalise) || item.pluriel.equals(nomNormalise)) {
                    return item;
                }
            }
            return null;
        }
    }

    private final Map<Item, Integer> stocks;
    private final Map<Item, String[]> recettes;

    public Officine() {
        this.stocks = new HashMap<>();
        this.recettes = new HashMap<>();

        // Initialisation des recettes
        recettes.put(Item.GLAIRES_PURULENTES,
                new String[]{"2 larmes de brume funèbre", "1 goutte de sang de citrouille"});
        recettes.put(Item.AME_EVANESCENTE,
                new String[]{"3 pincées de poudre de lune", "1 œil de grenouille"});
        recettes.put(Item.SELS_SUFFOCANTS,
                new String[]{"2 crocs de troll", "1 fragment d'écaille de dragonnet", "1 radicelle de racine hurlante"});
        recettes.put(Item.PATE_SEPULCRALE,
                new String[]{"3 radicelles de racine hurlante", "1 fiole de glaires purulentes"});
        recettes.put(Item.ESSENCE_CAUCHEMAR,
                new String[]{"2 pincées de poudre de lune", "2 larmes de brume funèbre"});
    }

    /**
     * Ajoute des ingrédients au stock
     * @param ingredient Chaîne au format "quantité nom_ingredient" (ex: "3 yeux de grenouille")
     */
    public void rentrer(String ingredient) throws IllegalArgumentException{
        Pattern pattern = Pattern.compile("^(\\d+)\\s+(.+)$");
        Matcher matcher = pattern.matcher(ingredient);

        if (matcher.find()) {
            int quantite = Integer.parseInt(matcher.group(1));
            if (quantite <= 0) {
                throw new IllegalArgumentException("Quantité invalide");
            }
            String nom = matcher.group(2);
            Item item = Item.fromNom(nom);

            if (item != null) {
                stocks.put(item, stocks.getOrDefault(item, 0) + quantite);
            } else {
                throw new IllegalArgumentException("Ingredient invalide");
            }
        } else {
            throw new IllegalArgumentException("Ingredient invalide");
        }
    }

    /**
     * Retourne la quantité en stock d'un ingrédient
     * @param ingredient Nom de l'ingrédient (singulier ou pluriel)
     * @return Quantité en stock
     */
    public int quantite(String ingredient) {
        Item item = Item.fromNom(ingredient);
        if (item == null) {
            return 0;
        }
        return stocks.getOrDefault(item, 0);
    }

    /**
     * Prépare une ou plusieurs potions selon la recette
     * @param demande Chaîne au format "quantité nom_potion" (ex: "2 fioles de glaires purulentes")
     * @return Nombre de potions réellement préparées
     */
    public int preparer(String demande) {
        Pattern pattern = Pattern.compile("^(\\d+)\\s+(.+)$");
        Matcher matcher = pattern.matcher(demande);

        if (!matcher.find()) {
            return 0;
        }

        int quantiteDemandee = Integer.parseInt(matcher.group(1));
        String nomPotion = matcher.group(2);
        Item potion = Item.fromNom(nomPotion);

        if (potion == null || !recettes.containsKey(potion)) {
            return 0;
        }

        // Calculer combien de potions peuvent être préparées
        int maxPreparable = quantiteDemandee;
        String[] ingredients = recettes.get(potion);

        for (String ingredient : ingredients) {
            Pattern ingPattern = Pattern.compile("^(\\d+)\\s+(.+)$");
            Matcher ingMatcher = ingPattern.matcher(ingredient);

            if (ingMatcher.find()) {
                int quantiteNecessaire = Integer.parseInt(ingMatcher.group(1));
                String nomIngredient = ingMatcher.group(2);
                Item itemIngredient = Item.fromNom(nomIngredient);

                if (itemIngredient == null) {
                    return 0;
                }

                int stockDisponible = stocks.getOrDefault(itemIngredient, 0);
                int possibleAvecCetIngredient = stockDisponible / quantiteNecessaire;
                maxPreparable = Math.min(maxPreparable, possibleAvecCetIngredient);
            }
        }

        // Préparer les potions et actualiser les stocks
        if (maxPreparable > 0) {
            for (String ingredient : ingredients) {
                Pattern ingPattern = Pattern.compile("^(\\d+)\\s+(.+)$");
                Matcher ingMatcher = ingPattern.matcher(ingredient);

                if (ingMatcher.find()) {
                    int quantiteNecessaire = Integer.parseInt(ingMatcher.group(1));
                    String nomIngredient = ingMatcher.group(2);
                    Item itemIngredient = Item.fromNom(nomIngredient);

                    int stockActuel = stocks.get(itemIngredient);
                    stocks.put(itemIngredient, stockActuel - (quantiteNecessaire * maxPreparable));
                }
            }

            // Ajouter les potions au stock
            stocks.put(potion, stocks.getOrDefault(potion, 0) + maxPreparable);
        }

        return maxPreparable;
    }
}