package ru.netology;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class Main {

    public static AtomicIntegerArray result = new AtomicIntegerArray(3);


    public static void main(String[] args) throws InterruptedException {

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                int l = text.length();
                int c = 1;
                for (int j = 0; j < l - 1; j++) {
                    if (text.charAt(j) == text.charAt(j + 1)) {
                        c++;
                    }
                }
                if (c == l) {
                    result.incrementAndGet(l - 3);
                }
            }
        });
        thread1.start();


        Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                int l = text.length();
                int c = 1;
                if (text.charAt(0) != text.charAt(l - 1)) {
                    for (int j = 0; j < l - 1; j++) {
                        if (text.charAt(j) == text.charAt(j + 1)
                                || text.charAt(j) < text.charAt(j + 1)) {
                            c++;
                        }
                    }
                    if (c == l) {
                        result.incrementAndGet(l - 3);
                    }
                }
            }
        });
        thread2.start();


        Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                int k = 0;
                int l = text.length();
                int r = l - 1;
                boolean c = false;
                char a = text.charAt(0);
                int i = 1;
                while (k <= r) {
                    if (text.charAt(k) == text.charAt(r)) {
                        if (a == text.charAt(k)) {
                            i++;
                        }
                        c = true;
                    } else {
                        c = false;
                        break;
                    }
                    k++;
                    r--;
                }
                if (c && i <= l / 2) {
                    result.incrementAndGet(l - 3);
                }
            }
        });
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        System.out.println("Красивых слов с длиной 3: " + result.get(0) + " шт.");
        System.out.println("Красивых слов с длиной 4: " + result.get(1) + " шт.");
        System.out.println("Красивых слов с длиной 5: " + result.get(2) + " шт.");
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}

