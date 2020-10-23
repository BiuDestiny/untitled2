package com.company;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Solution solution = new Solution();
        System.out.println("正在计算水壶问题..........");
        solution.run(4, 3, 2);
    }
}

class Solution {
    //状态空间定义
    private class State {
        private int x;
        private int y;
        private int method;

        public State(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public State(int x, int y, int method) {
            this.x = x;
            this.y = y;
            this.method = method;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getMethod() {
            return method;
        }

        @Override
        public String toString() {
            return "State{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return x == state.x &&
                    y == state.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

    }

    //操作组
    private ArrayList<State> getNextStates(int currentX, int currentY, int x, int y) {
        ArrayList<State> list = new ArrayList<>();
        //1.外部加水，将X装满
        State state1 = new State(x, currentY, 1);
        //2.外部加水，将Y装满
        State state2 = new State(currentX, y, 2);

        //3.清空X
        State state3 = new State(0, currentY, 3);
        //4.清空Y
        State state4 = new State(currentX, 0, 4);

        //5.X -> Y，Y装满，X有剩
        State state5 = new State(currentX - (y - currentY), y, 5);
        //6.Y -> X，X装满，Y有剩
        State state6 = new State(x, currentY - (x - currentX), 6);

        //7.X -> Y，X倒完，Y没有满
        State state7 = new State(0, currentY + currentX, 7);
        //8.Y -> X，Y倒完，X没有满
        State state8 = new State(currentX + currentY, 0, 8);

        //---------------状态对应操作---------------
        //1.当前水量不满，才能加水
        if (currentX < x) {
            list.add(state1);
        }
        if (currentY < y) {
            list.add(state2);
        }

        //2.当前水量不为0，才能倒水
        if (currentX > 0) {
            list.add(state3);
        }
        if (currentY > 0) {
            list.add(state4);
        }

        //3.接水壶有剩余才倒，且倒水的水壶不为空
        if (currentX - (y - currentY) > 0 && currentY < y && currentX > 0) {
            list.add(state5);
        }
        if (currentY - (x - currentX) > 0 && currentX < x && currentY > 0) {
            list.add(state6);
        }

        //4.接水的壶装得下才倒，且倒水的水壶不为空
        if (currentX + currentY < y && currentX > 0) {
            list.add(state7);
        }
        if (currentX + currentY < x && currentY > 0) {
            list.add(state8);
        }
        return list;
    }

    public boolean run(int x, int y, int z) {

        State initState = new State(0, 0);
        Queue<State> queue = new LinkedList<State>();
        Set<State> visited = new HashSet<>();

        //初始化（0，0）
        queue.offer(initState);
        visited.add(initState);
        ArrayList<ArrayList<State>> historyStateList = new ArrayList<ArrayList<State>>();
        int count = 1;

        while (!queue.isEmpty()) {
            State currentState = queue.poll();

            int currentX = currentState.getX();
            int currentY = currentState.getY();

            ArrayList<State> nextStatesList = getNextStates(currentX, currentY, x, y);
            nextStatesList.add(0, currentState);

            historyStateList.add(nextStatesList);
            if (currentX == z) {
                myPrintOut(x, y, findBack(historyStateList, currentState), count++);
            }

            System.out.println(currentState + " => " + nextStatesList);
            for (State state : nextStatesList) {
                if (!visited.contains(state)) {
                    queue.offer(state);
                    visited.add(state);
                }
            }

        }
        return false;
    }

    private ArrayList<State> findBack(ArrayList<ArrayList<State>> lists, State goalState) {
        ArrayList<State> outPutList = new ArrayList<>();
        State currentState = goalState;
        ArrayList<State> currntList;
        for (int i = lists.size() - 1; i >= 0; i--) {
            currntList = lists.get(i);//当前状态列表
            if (currntList.contains(currentState)) {
                currentState = currntList.get(0);//当前状态
                outPutList.add(currentState);
                //System.out.println(currentState);
            }
        }
        return outPutList;
    }

    private void myPrintOut(int x, int y, ArrayList<State> stateArrayList, int n) {
        System.out.println("第" + n + "种解决方案");
        System.out.println(x+"升水壶"+"\t\t\t"+y+"升水壶"+"\t\t\t"+"所用操作");
        State state;
        int method;
        for (int i = stateArrayList.size() - 1; i >= 0; i--) {
            state = stateArrayList.get(i);
            method = state.getMethod();
            System.out.printf("%-16d%-16d%-16d\n",state.getX(),state.getY(),method);
        }
    }

}