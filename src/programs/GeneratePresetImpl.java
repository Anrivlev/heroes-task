package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
    private static final int MAX_UNIT_PER_TYPE = 11;

    private static final int ROWS = 3;

    private static final int UNITS_IN_A_ROW = 21;

    // Итоговая сложность: O(n * logn), где (O * logn) - сортировка, O(n) - выбор.
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        unitList.sort(Comparator.comparingDouble(unit ->
                -((double) (unit.getBaseAttack() + unit.getHealth()) / unit.getCost())
        ));

        int computerArmyPoints = 0;
        List<Unit> generatedUnits = new ArrayList<>();

        for (Unit unit : unitList) {
            int numberOfUnits = getNumberOfUnits(unit, maxPoints, computerArmyPoints);
            replenishArmy(unit, numberOfUnits, generatedUnits);
            computerArmyPoints += numberOfUnits * unit.getCost();
        }

        setPositionsToUnits(generatedUnits);

        Army computerArmy = new Army(generatedUnits);
        computerArmy.setPoints(computerArmyPoints);

        return computerArmy;
    }

    private void setPositionsToUnits(List<Unit> generatedUnits) {
        Set<String> occupiedPositions = new HashSet<>();
        Random random = new Random();

        for (Unit unit : generatedUnits) {
            int xCoordinate, yCoordinate;
            do {
                xCoordinate = random.nextInt(ROWS);
                yCoordinate = random.nextInt(UNITS_IN_A_ROW);
            } while (occupiedPositions.contains(xCoordinate + ":" + yCoordinate));

            occupiedPositions.add(xCoordinate + ":" + yCoordinate);
            unit.setxCoordinate(xCoordinate);
            unit.setyCoordinate(yCoordinate);
        }
    }

    private int getNumberOfUnits(Unit unit, int maxPoints, int currentPoints) {
        return Math.min(MAX_UNIT_PER_TYPE, (maxPoints - currentPoints) / unit.getCost());
    }

    private void replenishArmy(Unit unit, int numberOfUnits, List<Unit> generatedUnits) {
        for (int i = 0; i < numberOfUnits; i++) {
            Unit newUnit = new Unit(unit.getUnitType() + " " + i, unit.getUnitType(), unit.getHealth(),
                    unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                    unit.getAttackBonuses(), unit.getDefenceBonuses(), 0, 0);

            generatedUnits.add(newUnit);
        }
    }
}