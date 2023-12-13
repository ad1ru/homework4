import java.util.Random;

public class Main {
    public static boolean thorStunChance;
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefence;
    public static int[] heroesHealth = {280, 270, 250, 300, 150, 200};
    public static int[] heroesDamage = {10, 20, 15, 0, 0, 0};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Medical", "Lucky", "Thor"};
    public static int roundNumber;

    public static void main(String[] args) {
        showStatistics();
        while (!isGameOver()) {
            playRound();
        }
    }

    public static void playRound() { // начало раунда
        roundNumber++;
        chooseBossDefence();
        thorStunChance = getRandomBoolean();

        if (thorStunChance) {
            System.out.println("Thor stunned the boss! Boss skips this round.");
            heroesAttack();
            medicHealing();
        } else {
            bossAttacks();
            heroesAttack();
            if (randomLucky()) {
                System.out.println("Lucky evaded the boss attack!");
                heroesHealth[4] += bossDamage;
            }
            medicHealing();
        }

        showStatistics();
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesHealth.length - 3); // исключаем медика, лаки и тора из выбора защиты для босса
        bossDefence = heroesAttackType[randomIndex];
    }

    public static void heroesAttack() { // атаки героев
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i].equals(bossDefence)) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2;
                    damage = heroesDamage[i] * coeff;
                    System.out.println("Critical damage: " + damage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static void medicHealing() { // действия медика
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesAttackType[i].equals("Medical") && heroesHealth[i] > 0) { // если у героя меньше 100 здоровья, и он не Medic
                int indexToHeal = findHeroToHeal();
                if (indexToHeal != -1) {
                    heroesHealth[indexToHeal] += 100;
                    System.out.println("Medical healed " + heroesAttackType[indexToHeal] + " by 100 health points.");
                }
            }
        }
    }

    public static int findHeroToHeal() { // поиск героя для помощи от медика
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] < 100 && !heroesAttackType[i].equals("Medical") && heroesHealth[i] > 0) {
                return i;
            }
        }
        return -1; // если нет героев с менее чем 100 здоровья, которых можно вылечить
    }

    public static void bossAttacks() { // атака босса по всем героям
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if (heroesHealth[i] - bossDamage < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossDamage;
                }
            }
        }
    }

    public static boolean isGameOver() { // проверка кто выиграл
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }

        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void showStatistics() { // выводит статистику
        System.out.println("ROUND " + roundNumber + " ---------------");
        System.out.println("Boss health: " + bossHealth + " damage: " + bossDamage + " defence: " +
                (bossDefence == null ? "No defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + " damage: " + heroesDamage[i]);
        }
    }

    public static boolean getRandomBoolean() { // рандом true or false
        Random random = new Random();
        return random.nextBoolean();
    }

    public static boolean randomLucky() { // рандом true or false
        Random random = new Random();
        return random.nextBoolean();
    }
}
