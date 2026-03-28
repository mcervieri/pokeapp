package com.pokeapp.application.seeder;

import com.opencsv.CSVReader;
import com.pokeapp.domain.pokemon.*;
import com.pokeapp.domain.move.*;
import com.pokeapp.domain.item.*;
import com.pokeapp.application.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@ConditionalOnProperty(name = "app.seeder.enabled", havingValue = "true")
public class DataSeederService implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeederService.class);

    private final GenerationRepository generationRepository;
    private final TypeRepository typeRepository;
    private final StatRepository statRepository;
    private final AbilityRepository abilityRepository;
    private final MoveRepository moveRepository;
    private final ItemRepository itemRepository;
    private final NatureRepository natureRepository;
    private final EvolutionChainRepository evolutionChainRepository;
    private final PokemonSpeciesRepository pokemonSpeciesRepository;
    private final PokemonRepository pokemonRepository;
    private final PokemonTypeRepository pokemonTypeRepository;
    private final PokemonStatRepository pokemonStatRepository;
    private final PokemonAbilityRepository pokemonAbilityRepository;
    private final PokemonMoveRepository pokemonMoveRepository;
    private final TypeEffectivenessRepository typeEffectivenessRepository;
    private final DamageClassRepository damageClassRepository;

    public DataSeederService(
            GenerationRepository generationRepository,
            TypeRepository typeRepository,
            StatRepository statRepository,
            AbilityRepository abilityRepository,
            MoveRepository moveRepository,
            ItemRepository itemRepository,
            NatureRepository natureRepository,
            EvolutionChainRepository evolutionChainRepository,
            PokemonSpeciesRepository pokemonSpeciesRepository,
            PokemonRepository pokemonRepository,
            PokemonTypeRepository pokemonTypeRepository,
            PokemonStatRepository pokemonStatRepository,
            PokemonAbilityRepository pokemonAbilityRepository,
            PokemonMoveRepository pokemonMoveRepository,
            TypeEffectivenessRepository typeEffectivenessRepository,
            DamageClassRepository damageClassRepository) {
        this.generationRepository = generationRepository;
        this.typeRepository = typeRepository;
        this.statRepository = statRepository;
        this.abilityRepository = abilityRepository;
        this.moveRepository = moveRepository;
        this.itemRepository = itemRepository;
        this.natureRepository = natureRepository;
        this.evolutionChainRepository = evolutionChainRepository;
        this.pokemonSpeciesRepository = pokemonSpeciesRepository;
        this.pokemonRepository = pokemonRepository;
        this.pokemonTypeRepository = pokemonTypeRepository;
        this.pokemonStatRepository = pokemonStatRepository;
        this.pokemonAbilityRepository = pokemonAbilityRepository;
        this.pokemonMoveRepository = pokemonMoveRepository;
        this.typeEffectivenessRepository = typeEffectivenessRepository;
        this.damageClassRepository = damageClassRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("=== DataSeeder starting ===");

        seedGenerations();
        seedTypes();
        seedStats();
        seedAbilities();
        seedDamageClasses();
        seedMoves();
        seedItems();
        seedNatures();
        seedEvolutionChains();
        seedPokemonSpecies();
        seedPokemon();
        seedPokemonTypes();
        seedPokemonStats();
        seedPokemonAbilities();
        seedPokemonMoves();
        seedTypeEffectiveness();

        log.info("=== DataSeeder complete ===");
    }

    // ─── Helpers ────────────────────────────────────────────────────────────────

    private String[][] readCsv(String filename) throws Exception {
        var resource = new ClassPathResource("db/seed/" + filename);
        try (var reader = new CSVReader(new InputStreamReader(
                resource.getInputStream(), StandardCharsets.UTF_8))) {
            var rows = reader.readAll();
            rows.remove(0); // remove header row
            return rows.toArray(new String[0][]);
        }
    }

    private Integer parseNullableInt(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ─── Seeders ────────────────────────────────────────────────────────────────

    private void seedGenerations() throws Exception {
        if (generationRepository.count() > 0) {
            log.info("generations - skipped");
            return;
        }
        // generations.csv columns: id, identifier, main_region_id
        // identifiers: generation-i, generation-ii, ... → we store as "I", "II", ...
        // region names we don't have without region CSV — store identifier as name for
        // now
        var rows = readCsv("generations.csv");
        List<Generation> list = new ArrayList<>();
        for (var row : rows) {
            list.add(new Generation(Integer.parseInt(row[0]), row[1], null));
        }
        generationRepository.saveAll(list);
        log.info("generations - {} rows", list.size());
    }

    private void seedTypes() throws Exception {
        if (typeRepository.count() > 0) {
            log.info("types - skipped");
            return;
        }
        var rows = readCsv("types.csv");
        List<Type> list = new ArrayList<>();
        for (var row : rows) {
            int csvId = Integer.parseInt(row[0]);
            if (csvId > 10000)
                continue;
            list.add(new Type(csvId, row[1]));
        }
        typeRepository.saveAll(list);
        log.info("types - {} rows", list.size());
    }

    private void seedStats() throws Exception {
        if (statRepository.count() > 0) {
            log.info("stats - skipped");
            return;
        }
        var rows = readCsv("stats.csv");
        // columns: id, damage_class_id, identifier, is_battle_only, game_index
        List<Stat> list = new ArrayList<>();
        for (var row : rows) {
            list.add(new Stat(Integer.parseInt(row[0]), row[2])); // pokeapiId=row[0], name=row[2]
        }
        statRepository.saveAll(list);
        log.info("stats - {} rows", list.size());
    }

    private void seedAbilities() throws Exception {
        if (abilityRepository.count() > 0) {
            log.info("abilities - skipped");
            return;
        }
        var rows = readCsv("abilities.csv");
        List<Ability> list = new ArrayList<>();
        for (var row : rows) {
            int csvId = Integer.parseInt(row[0]);
            if (csvId > 10000)
                continue;
            list.add(new Ability(csvId, row[1], null)); // pokeapiId, name, effectText=null
        }
        abilityRepository.saveAll(list);
        log.info("abilities - {} rows", list.size());
    }

    private void seedDamageClasses() throws Exception {
        if (damageClassRepository.count() > 0) {
            log.info("damage_classes - skipped");
            return;
        }
        List<DamageClass> list = List.of(
                new DamageClass("physical"),
                new DamageClass("special"),
                new DamageClass("status"));
        damageClassRepository.saveAll(list);
        log.info("damage_classes - 3 rows");
    }

    private void seedMoves() throws Exception {
        if (moveRepository.count() > 0) {
            log.info("moves - skipped");
            return;
        }

        Map<Integer, Type> typeMap = new HashMap<>();
        typeRepository.findAll().forEach(t -> typeMap.put(t.getPokeapiId(), t));

        // DamageClass CSV ids: 1=status, 2=physical, 3=special
        // Map them by name to match what we seeded
        Map<Integer, DamageClass> damageClassMap = new HashMap<>();
        damageClassRepository.findAll().forEach(dc -> {
            switch (dc.getName()) {
                case "status" -> damageClassMap.put(1, dc);
                case "physical" -> damageClassMap.put(2, dc);
                case "special" -> damageClassMap.put(3, dc);
            }
        });

        var rows = readCsv("moves.csv");
        // columns: id, identifier, generation_id, type_id, power, pp, accuracy,
        // priority, target_id, damage_class_id, effect_id, effect_chance
        List<Move> list = new ArrayList<>();
        for (var row : rows) {
            Integer pokeapiId = Integer.parseInt(row[0]);
            String name = row[1];
            Type type = typeMap.get(Integer.parseInt(row[3]));
            DamageClass dc = damageClassMap.get(Integer.parseInt(row[9]));
            if (type == null || dc == null)
                continue;

            Integer power = parseNullableInt(row[4]);
            Integer pp = parseNullableInt(row[5]);
            Integer accuracy = parseNullableInt(row[6]);
            Integer priority = Integer.parseInt(row[7]);

            list.add(new Move(pokeapiId, name, type, dc, power, accuracy, pp, priority, null));
        }
        moveRepository.saveAll(list);
        log.info("moves - {} rows", list.size());
    }

    private void seedItems() throws Exception {
        if (itemRepository.count() > 0) {
            log.info("items - skipped");
            return;
        }
        var rows = readCsv("items.csv");
        // columns: id, identifier, category_id, cost, fling_power, fling_effect_id
        List<Item> list = new ArrayList<>();
        for (var row : rows) {
            Integer pokeapiId = Integer.parseInt(row[0]);
            String name = row[1];
            list.add(new Item(pokeapiId, name, null, null));
        }
        itemRepository.saveAll(list);
        log.info("items - {} rows", list.size());
    }

    private void seedNatures() throws Exception {
        if (natureRepository.count() > 0) {
            log.info("natures - skipped");
            return;
        }

        Map<Integer, Stat> statMap = new HashMap<>();
        statRepository.findAll().forEach(s -> statMap.put(s.getPokeapiId(), s));

        var rows = readCsv("natures.csv");
        // columns: id, identifier, decreased_stat_id, increased_stat_id, ...
        List<Nature> list = new ArrayList<>();
        for (var row : rows) {
            Integer pokeapiId = Integer.parseInt(row[0]);
            String name = row[1];

            // Neutral natures (like Hardy) have 0 for both stat columns
            Integer decreasedStatId = parseNullableInt(row[2]);
            Integer increasedStatId = parseNullableInt(row[3]);

            Stat decreasedStat = decreasedStatId != null ? statMap.get(decreasedStatId) : null;
            Stat increasedStat = increasedStatId != null ? statMap.get(increasedStatId) : null;

            list.add(new Nature(pokeapiId, name, increasedStat, decreasedStat));
        }
        natureRepository.saveAll(list);
        log.info("natures - {} rows", list.size());
    }

    private void seedEvolutionChains() throws Exception {
        if (evolutionChainRepository.count() > 0) {
            log.info("evolution_chains — skipped");
            return;
        }
        var rows = readCsv("evolution_chains.csv");
        List<EvolutionChain> list = new ArrayList<>();
        for (var row : rows) {
            var e = new EvolutionChain();
            e.setId(Integer.parseInt(row[0]));
            list.add(e);
        }
        evolutionChainRepository.saveAll(list);
        log.info("evolution_chains — {} rows", list.size());
    }

    private void seedPokemonSpecies() throws Exception {
        if (pokemonSpeciesRepository.count() > 0) {
            log.info("pokemon_species - skipped");
            return;
        }

        Map<Integer, Generation> generationMap = new HashMap<>();
        generationRepository.findAll().forEach(g -> generationMap.put(g.getPokeapiId(), g));

        var rows = readCsv("pokemon_species.csv");
        // columns: id, identifier, generation_id, evolves_from_species_id,
        // evolution_chain_id,
        // color_id, shape_id, habitat_id, gender_rate, capture_rate, base_happiness,
        // is_baby, is_legendary, is_mythical, ...
        List<PokemonSpecies> list = new ArrayList<>();
        for (var row : rows) {
            Generation generation = generationMap.get(Integer.parseInt(row[2]));
            if (generation == null)
                continue;

            Integer pokeapiId = Integer.parseInt(row[0]);
            String name = row[1];
            boolean legendary = row[11].equals("1");
            boolean mythical = row[12].equals("1");
            Integer baseHappiness = parseNullableInt(row[10]);
            Integer captureRate = parseNullableInt(row[9]);

            list.add(new PokemonSpecies(pokeapiId, name, generation,
                    legendary, mythical, baseHappiness, captureRate));
        }
        pokemonSpeciesRepository.saveAll(list);
        log.info("pokemon_species - {} rows", list.size());
    }

    private void seedPokemon() throws Exception {
        if (pokemonRepository.count() > 0) {
            log.info("pokemon - skipped");
            return;
        }

        Map<Integer, PokemonSpecies> speciesMap = new HashMap<>();
        pokemonSpeciesRepository.findAll().forEach(s -> speciesMap.put(s.getPokeapiId(), s));

        var rows = readCsv("pokemon.csv");
        // columns: id, identifier, species_id, height, weight, base_experience, order,
        // is_default
        List<Pokemon> list = new ArrayList<>();
        for (var row : rows) {
            int pokeapiId = Integer.parseInt(row[0]);
            if (pokeapiId > 10000)
                continue;

            PokemonSpecies species = speciesMap.get(Integer.parseInt(row[2]));
            if (species == null)
                continue;

            String name = row[1];
            Integer heightDm = Integer.parseInt(row[3]);
            Integer weightHg = Integer.parseInt(row[4]);
            Integer baseExp = parseNullableInt(row[5]);
            boolean defaultForm = row[7].equals("1");

            list.add(new Pokemon(pokeapiId, name, species, defaultForm, heightDm, weightHg, baseExp, null));
        }
        pokemonRepository.saveAll(list);
        log.info("pokemon - {} rows", list.size());
    }

    private void seedPokemonTypes() throws Exception {
        if (pokemonTypeRepository.count() > 0) {
            log.info("pokemon_types - skipped");
            return;
        }

        Map<Integer, Pokemon> pokemonMap = new HashMap<>();
        pokemonRepository.findAll().forEach(p -> pokemonMap.put(p.getPokeapiId(), p));

        Map<Integer, Type> typeMap = new HashMap<>();
        typeRepository.findAll().forEach(t -> typeMap.put(t.getPokeapiId(), t));

        var rows = readCsv("pokemon_types.csv");
        // columns: pokemon_id, type_id, slot
        List<PokemonType> list = new ArrayList<>();
        for (var row : rows) {
            int pokemonId = Integer.parseInt(row[0]);
            if (pokemonId > 10000)
                continue;

            Pokemon pokemon = pokemonMap.get(pokemonId);
            Type type = typeMap.get(Integer.parseInt(row[1]));
            if (pokemon == null || type == null)
                continue;

            Integer slot = Integer.parseInt(row[2]);

            list.add(new PokemonType(pokemon, type, slot));
        }
        pokemonTypeRepository.saveAll(list);
        log.info("pokemon_types - {} rows", list.size());
    }

    private void seedPokemonStats() throws Exception {
        if (pokemonStatRepository.count() > 0) {
            log.info("pokemon_stats - skipped");
            return;
        }

        Map<Integer, Pokemon> pokemonMap = new HashMap<>();
        pokemonRepository.findAll().forEach(p -> pokemonMap.put(p.getPokeapiId(), p));

        Map<Integer, Stat> statMap = new HashMap<>();
        statRepository.findAll().forEach(s -> statMap.put(s.getPokeapiId(), s));

        var rows = readCsv("pokemon_stats.csv");
        // columns: pokemon_id, stat_id, base_stat, effort
        List<PokemonStat> list = new ArrayList<>();
        for (var row : rows) {
            int pokemonId = Integer.parseInt(row[0]);
            if (pokemonId > 10000)
                continue;

            Pokemon pokemon = pokemonMap.get(pokemonId);
            Stat stat = statMap.get(Integer.parseInt(row[1]));
            if (pokemon == null || stat == null)
                continue;

            Integer baseValue = Integer.parseInt(row[2]);
            Integer effort = Integer.parseInt(row[3]);

            list.add(new PokemonStat(pokemon, stat, baseValue, effort));
        }
        pokemonStatRepository.saveAll(list);
        log.info("pokemon_stats - {} rows", list.size());
    }

    private void seedPokemonAbilities() throws Exception {
        if (pokemonAbilityRepository.count() > 0) {
            log.info("pokemon_abilities - skipped");
            return;
        }

        Map<Integer, Pokemon> pokemonMap = new HashMap<>();
        pokemonRepository.findAll().forEach(p -> pokemonMap.put(p.getPokeapiId(), p));

        Map<Integer, Ability> abilityMap = new HashMap<>();
        abilityRepository.findAll().forEach(a -> abilityMap.put(a.getPokeapiId(), a));

        var rows = readCsv("pokemon_abilities.csv");
        // columns: pokemon_id, ability_id, is_hidden, slot
        List<PokemonAbility> list = new ArrayList<>();
        for (var row : rows) {
            int pokemonId = Integer.parseInt(row[0]);
            if (pokemonId > 10000)
                continue;

            Pokemon pokemon = pokemonMap.get(pokemonId);
            Ability ability = abilityMap.get(Integer.parseInt(row[1]));
            if (pokemon == null || ability == null)
                continue;

            Integer slot = Integer.parseInt(row[3]);
            boolean hidden = row[2].equals("1");

            list.add(new PokemonAbility(pokemon, ability, slot, hidden));
        }
        pokemonAbilityRepository.saveAll(list);
        log.info("pokemon_abilities - {} rows", list.size());
    }

    private void seedPokemonMoves() throws Exception {
        if (pokemonMoveRepository.count() > 0) {
            log.info("pokemon_moves — skipped");
            return;
        }

        // Load all pokemon and moves into maps for fast lookup
        Map<Integer, Pokemon> pokemonMap = new HashMap<>();
        pokemonRepository.findAll().forEach(p -> pokemonMap.put(p.getPokeapiId(), p));

        Map<Integer, Move> moveMap = new HashMap<>();
        moveRepository.findAll().forEach(m -> moveMap.put(m.getPokeapiId(), m));

        var rows = readCsv("pokemon_moves.csv");
        // columns: pokemon_id, version_group_id, move_id, pokemon_move_method_id,
        // level, order
        Map<String, PokemonMove> seen = new LinkedHashMap<>();
        for (var row : rows) {
            int pokemonId = Integer.parseInt(row[0]);
            if (pokemonId > 10000)
                continue;
            int moveId = Integer.parseInt(row[2]);
            String method = row[3];
            String key = pokemonId + "_" + moveId + "_" + method;
            if (seen.containsKey(key))
                continue;

            Pokemon pokemon = pokemonMap.get(pokemonId);
            Move move = moveMap.get(moveId);
            if (pokemon == null || move == null)
                continue;

            Integer level = parseNullableInt(row[4]);
            seen.put(key, new PokemonMove(pokemon, move, method, level));
        }
        pokemonMoveRepository.saveAll(seen.values());
        log.info("pokemon_moves - {} rows", seen.size());
    }

    private void seedTypeEffectiveness() throws Exception {
        if (typeEffectivenessRepository.count() > 0) {
            log.info("type_efficacy - skipped");
            return;
        }

        // Load all types into a map for fast lookup
        Map<Integer, Type> typeMap = new HashMap<>();
        typeRepository.findAll().forEach(t -> typeMap.put(t.getPokeapiId(), t));

        var rows = readCsv("type_efficacy.csv");
        // columns: damage_type_id, target_type_id, damage_factor
        List<TypeEffectiveness> list = new ArrayList<>();
        for (var row : rows) {
            Type attacking = typeMap.get(Integer.parseInt(row[0]));
            Type defending = typeMap.get(Integer.parseInt(row[1]));
            if (attacking == null || defending == null)
                continue;

            BigDecimal multiplier = BigDecimal.valueOf(Integer.parseInt(row[2]))
                    .divide(BigDecimal.valueOf(100));

            list.add(new TypeEffectiveness(attacking, defending, multiplier));
        }
        typeEffectivenessRepository.saveAll(list);
        log.info("type_efficacy - {} rows", list.size());
    }
}