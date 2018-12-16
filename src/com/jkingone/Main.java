package com.jkingone;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        test10();
    }

    private static void test10() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n > 0) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            int max = Integer.MIN_VALUE;
            for (int i = a; i <= b; i++) {
                char[] chars = String.valueOf(i).toCharArray();
                StringBuilder stringBuilder = new StringBuilder();
                int isUp = -1;
                for (int j = 0; j < chars.length - 1; j++) {
                    if (chars[j] < chars[j + 1]) {
                        if (isUp == -1) {
                            isUp = 0;
                        } else if (isUp == 1) {
                            String tmp = String.valueOf(chars, j + 1, j + stringBuilder.length() - 1);
                            if (tmp.equals(stringBuilder.toString())) {
                                int count = 0;
                                for (int k = 0; k < stringBuilder.length(); k++) {
                                    count += chars[j + 1 + k] - '0';
                                }
                                max = Math.max(count, max);
                                j += stringBuilder.length();
                            } else {
                                stringBuilder.append(chars[j + 1]);
                            }
                            stringBuilder = new StringBuilder();
                            isUp = -1;
                        }
                        if (j == 0) {
                            stringBuilder.append(chars[j]);
                        }
                        if (isUp == 0) {
                            stringBuilder.append(chars[j + 1]);
                        }
                    } else if (chars[j] > chars[j + 1]) {
                        if (isUp == 0) {
                            isUp = 1;
                        }
                        if (isUp == 1) {
                            stringBuilder.append(chars[j + 1]);
                        }
                    } else {
                        if (isUp == 0) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(chars[j]);
                        } else if (isUp == 1) {
                            String tmp = String.valueOf(chars, j + 1, j + stringBuilder.length() - 1);
                            if (tmp.equals(stringBuilder.toString())) {
                                int count = 0;
                                for (int k = 0; k < stringBuilder.length(); k++) {
                                    count += chars[j + 1 + k] - '0';
                                }
                                max = Math.max(count, max);
                                j += stringBuilder.length();
                            } else {
                                stringBuilder.append(chars[j + 1]);
                            }
                            stringBuilder = new StringBuilder();
                            isUp = -1;
                        }
                    }
                }
            }
            System.out.println(max);
            n--;
        }
    }

    private static void test9() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        StringBuilder stringBuilder = new StringBuilder(16);
        while (n > 0) {
            int tmp = n % m;
            stringBuilder.append(tmp);
            n /= m;
        }
        System.out.println(stringBuilder.reverse().toString());
    }

    private static void test8() {
        Scanner scanner = new Scanner(System.in);
        String inputP = scanner.next();
        String inputS = scanner.next();
        char[] chars = inputS.toCharArray();
        int len = chars.length - 1;
        int pos = -1;
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == chars[len - i] && len - i > i) {
                pos = i;
            } else {
                break;
            }
        }

        int res = 0;

        if (pos != -1) {
            String inputS2 = inputS + inputS.substring(pos);
            res += solveTest8(inputP, inputS2);
            inputP = inputP.replaceAll(inputS2, "");
        }

        res += solveTest8(inputP, inputS);

        System.out.println(res);
    }

    private static int solveTest8(String inputP, String inputS) {
        int res = 0;
        int lenS = inputS.length();
        int start = 0;
        int tmp = 0;
        while (true) {
            int index = inputP.indexOf(inputS, start);
            if (index != -1) {
                if (index == start || start == 0) {
                    tmp += lenS;
                } else {
                    if (tmp != 0) {
                        res += tmp * tmp;
                    }
                    res += lenS * lenS;
                    tmp = 0;
                }
                start = index + lenS;
            } else {
                if (tmp != 0) {
                    res += tmp * tmp;
                }
                break;
            }
        }
        return res;
    }

    private static void test7() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        int start = -1;
        int end = -1;
        boolean isFirst = true;
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            if (a[i] == 1) {
                if (isFirst) {
                    start = i;
                    isFirst = false;
                }
                end = i;
            }
        }

        if (start == -1 || end == -1) {
            System.out.println(0);
            return;
        }

        if (start == end) {
            System.out.println(1);
        }

        int lastIndex = start;
        int count = 1;
        for (int i = start + 1; i <= end; i++) {
            if (a[i] == 1) {
                if (i - lastIndex > 1) {
                    count *= (i - lastIndex);
                }
                lastIndex = i;
            }
        }

        System.out.println(count);
    }

    private static void test6() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        while (n > 0) {
            int t = scanner.nextInt();
            int tmp = t;
            int sum = 0;
            while (tmp > 0) {
                sum += tmp % 10;
                tmp /= 10;
            }

            if (t % sum == 0) {
                System.out.println("Yes");
            } else {
                System.out.println("No");
            }
            n--;
        }
    }

    private static void test5() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int w = scanner.nextInt();
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = scanner.nextInt();
        }

        System.out.println(solveTest5(0, 0, n, v, w));


//        int count = 0;
//        ArrayList<Integer> upList = new ArrayList<>();
//        ArrayList<Integer> downList = new ArrayList<>();
//        upList.add(0);
//        for (int i = 0; i < n; i++) {
//            System.out.println(i + " ===> " + upList.size());
//            System.out.println(upList);
//            for (int j = 0; j < upList.size(); j++) {
//                int value = upList.get(j);
//                int one = value + v[i];
//                int two = value;
//                if (one <= w && one >= 0) {
//                    downList.add(one);
//                } else {
//                    count++;
//                }
//                if (two <= w && two >= 0) {
//                    downList.add(two);
//                } else {
//                    count++;
//                }
//            }
//            upList.clear();
//            upList.addAll(downList);
//            downList.clear();
//        }
//        count = (int) (Math.pow(2, n) - count);
//        System.out.println(upList.size());

//        int[][] a = new int[n + 1][(int)Math.pow(2, n) + 1];
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < Math.pow(2, i + 1); j++) {
//                if (j % 2 == 0) {
//                    a[i + 1][j] = a[i][j / 2];
//                } else {
//                    a[i + 1][j] = a[i][j / 2] + v[i];
//                }
//                if (i == n - 1 && a[i + 1][j] > w) {
//                    count++;
//                }
//            }
//        }
//        count = (int) (Math.pow(2, n) - count);
//        System.out.println(count);
    }

    // 测试数据：21 1165911996 842104736 130059605 359419358 682646280 378385685 622124412 740110626 814007758
    // 557557315 40153082 542984016 274340808 991565332 765434204 225621097 350652062 714078666 381520025
    // 613885618 64141537 783016950
    private static int solveTest5(long sum, int depth, int n, int[] v, int w) {
        if (depth == n) {
            if (sum <= w) {
                return 1;
            }
            return 0;
        }

        long nextSum = sum + v[depth];

        if (nextSum <= w) {
            return solveTest5(nextSum, depth + 1, n, v, w)
                    + solveTest5(sum, depth + 1, n, v, w);
        } else if (sum <= w && nextSum > w) {
            return solveTest5(sum, depth + 1, n, v, w);
        }

        return 0;
    }

    private static void test4() {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt();
        while (t > 0) {
            int n = scanner.nextInt();
            String string = scanner.next();
            char[] chars = string.toCharArray();
            int count = 0;
            for (int i = 0; i < n; i++) {
                if (chars[i] == '.') {
                    if (i + 1 < n) {
                        chars[i + 1] = 'X';
                    }
                    if (i + 2 < n) {
                        chars[i + 2] = 'X';
                    }
                    count++;
                }
            }
            System.out.println(count);
            t--;
        }
    }


    private static void test3() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        int[] b = new int[n];
        Arrays.fill(b, 0);
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (a[i] <= a[j]) {
                    b[i] += 1;
                } else {
                    b[j] += 1;
                    break;
                }
            }
        }
        long max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long res = a[i] * b[i];
            max = Math.max(res, max);
        }
        System.out.println(max);

        String string = "";

    }

    private static void test1() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        List<Integer> queue = new ArrayList<>();
        queue.add(n);
        int count = 0;
        while (!queue.isEmpty()) {
            int top = queue.remove(0);
            if (top == 0) {
                count++;
            }
            for (int i = 1; i <= 6; i++) {
                if (top - i >= 0) {
                    queue.add(top - i);
                }
            }
        }
        System.out.println(count);
    }

    /**
     * 给你六种面额 1、5、10、20、50、100 元的纸币，假设每种币值的数量都足够多，
     * 编写程序求组成N元（N为0~10000的非负整数）的不同组合的个数。
     */
    private static void test2() {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int[] money = {1, 5, 10, 20, 50, 100};
        List<Integer> queue = new ArrayList<>();
        queue.add(n);
        int count = 0;
        while (!queue.isEmpty()) {
            int top = queue.remove(0);
            if (top == 0) {
                count++;
            }
            for (int i = 0; i < 6; i++) {
                if (top - money[i] >= 0) {
                    queue.add(top - money[i]);
                }
            }
        }
        System.out.println(count);
    }


}
