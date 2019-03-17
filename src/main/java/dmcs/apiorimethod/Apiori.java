package dmcs.apiorimethod;

import org.apache.commons.math3.util.Combinations;

import java.util.*;
import java.util.stream.Collectors;

public class Apiori {

    private static final float FREQUENCY_TRESHOLD = 1;

    private Database database;

    private List<List<String>> sets;

    public static Apiori ofDatabase(Database database) {
        return new Apiori(database);
    }

    public void execute(int k) {
        sets = initFrequentSets();
        System.out.println(sets.stream().map(this::getFrequency).mapToDouble(p -> p).average().getAsDouble());
        for (int i = 1; i < k; i++) {
            apioriGen(k);
            System.out.println(sets.stream().map(this::getFrequency).mapToDouble(p -> p).average().getAsDouble());
        }
    }

    public List<List<String>> getSets() {
        return sets;
    }

    private Apiori(Database database) {
        this.database = database;
    }

    private List<List<String>> initFrequentSets() {
        return database.data.stream()
                .flatMap(Collection::stream)
                .distinct()
                .map(Collections::singletonList)
                .filter(set -> getFrequency(set) > FREQUENCY_TRESHOLD)
                .collect(Collectors.toList());
    }

    public float getFrequency(List<String> set) {
        float l = (float) database.data
                .stream()
                .filter(transaction -> transaction.containsAll(set))
                .count()
                / database.data.size();
        return l * 100;
    }

    private void apioriGen(int size) {
        Combinations combinations = new Combinations(sets.size(), 2);
        List<List<String>> newSets = new ArrayList<>();
        for (int[] combination : combinations) {
            connectIfContains(sets.get(combination[0]), sets.get(combination[1]))
                    .filter(set -> getFrequency(set) > FREQUENCY_TRESHOLD)
                    .ifPresent(newSets::add);
        }
        sets = newSets;
    }

    private Optional<List<String>> connectIfContains(List<String> one, List<String> second) {
        if (one.size() != second.size()) {
            throw new RuntimeException();
        }
        int guard = one.size() - 1;
        for (int i = 0; i < guard; i++) {
            if (!one.get(i).equals(second.get(i))) {
                return Optional.empty();
            }
        }
        List<String> result = new ArrayList<>(one);
        result.addAll(second);
        return Optional.of(result.stream().distinct().sorted().collect(Collectors.toList()));
    }
}
