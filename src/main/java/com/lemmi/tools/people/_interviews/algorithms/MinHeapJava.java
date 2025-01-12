package com.lemmi.tools.people._interviews.algorithms;

import java.util.PriorityQueue;

public class MinHeapJava {

    public static void main(String[] args) {
        var test = new MinHeapJava();
        var res = test.kClosest(new int[][]{{-1, 3}, {-2, 2}}, 1);
        System.out.println("RES: " + res[0][0] + " " + res[0][1]);
    }

    public int[][] kClosest(int[][] points, int k) {
        var heap = new PriorityQueue<Point>();

        for (int[] xy: points){
            var x = xy[0];
            var y = xy[1];
            var distance = (x * x) + ( y * y);
            System.out.println("distance: " + distance);
            var point = new Point(x, y, distance);
            heap.add(point);
        }
        Point point  = null;
        while (k > 0){
            point = heap.remove();
            k --;
        }

        return new int[][]{{point.x, point.y}};
    }

    static class Point implements Comparable<Point> {
        Integer distance;
        Integer x;
        Integer y;

        public Point(int x, int y, int distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        @Override
        public int compareTo(Point o) {
            return this.distance.compareTo(o.distance);
        }
    }
}
