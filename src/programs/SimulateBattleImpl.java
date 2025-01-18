package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    // Сложность:  O(n * m), где
    // n - количество юнитов в армии игрока,
    // m - количество юнитов в армии компьютера
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        Set<Unit> playerUnits = new HashSet<>(playerArmy.getUnits());
        Set<Unit> computerUnits = new HashSet<>(computerArmy.getUnits());

        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
            attackWithAnArmy(playerUnits);
            attackWithAnArmy(computerUnits);
        }
    }

    private void attackWithAnArmy(Set<Unit> attackingArmy) throws InterruptedException {
        Iterator<Unit> iterator = attackingArmy.iterator();
        while (iterator.hasNext()) {
            Unit attackingUnit = iterator.next();
            if (!attackingUnit.isAlive()) {
                iterator.remove();
                continue;
            }

            Unit targetUnit = attackingUnit.getProgram().attack();
            if (targetUnit != null) {
                printBattleLog.printBattleLog(attackingUnit, targetUnit);
            }
        }
    }
}